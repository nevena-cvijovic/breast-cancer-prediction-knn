(ns breast-cancer-prediction-knn.core
  (:import [java.lang Integer]
           )
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [breast-cancer-prediction-knn.evaluation-metrics :as eval-met]

            ))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; LOADING DATA
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn load-data
  "Function for loading data from csv file"
  [csv-file]
  (with-open [reader (io/reader csv-file)]
    (->> (csv/read-csv reader)
         (mapv vec))
    )
  )

(def csv-file "src/breast_cancer_prediction_knn/Cancerdata.csv")

(def cancer-data-raw (load-data csv-file) )


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; DATA TRANSFORMATION
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn parse-value
  "Converts string from dataset into integer if the value has only digits in it and returns java.lang.Object"
  [number_value]
  (if (re-matches #"\d+" number_value)
    (Integer/parseInt number_value)
    (if (re-matches #"\d+.\d+" number_value)
      (Double/parseDouble number_value)
      (if (= "Yes" number_value)
        true
        (if (= "No" number_value)
          false
          number_value)))))

(defn parse-row
  "Parsing each element of row to belonging type"
  [row]
  (map parse-value row))
(defn convert-dataset
  "Converts value of each observation in dataset to true data type and returns converted dataset.
  Arguments:
      - dataset-url: URL to dataset from src folder
  Returns:
      - java.lang.LazySeq"
  [dataset-url]
  (map (fn [x] (parse-row x)) (load-data dataset-url)))

(def cancer-data (convert-dataset "src/breast_cancer_prediction_knn/Cancerdata.csv"))
;(println cancer-data)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; PRINTING and PRESENTING DATA
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(doseq [row (take 10 cancer-data)]
  (println row))

(defn get-headers
  "Get column names (header)"
  [data]
  (first data))


(defn get-column
  "Extract a specific column by its name"
  [data column-name]
  (let [headers (get-headers data)
        column-index (.indexOf headers column-name)]
    (if (>= column-index 0)
      (map #(nth % column-index nil) (rest data))
      (println (str "Column " column-name " not found!")))))


(defn dataset-info
  "Get general information about the dataset"
  [data]
  (let [headers (get-headers data)
        num-rows (count (rest data)) ;; Exclude header
        num-cols (count headers)]
    {:num-rows num-rows
     :num-cols num-cols
     :columns headers}))


(defn last-rows
  "Get the last N rows of dataset"
  [data n]
  (let [rows (rest data)] ;; Exclude header
    (take-last n rows)))


;(println "Dataset Information:")
;(println (dataset-info cancer-data))
;
;(println "\nColumn 'diagnosis':")
;(println (get-column cancer-data "diagnosis"))
;
;(println "\nLast 5 rows of the dataset:")
;(println (last-rows cancer-data 5))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; DATA MANIPULATION
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



(defn drop-column
  "Drops a single column by column name"
  [data column-name]
  (let [headers (get-headers data) ;; Extract headers (first row)
        column-index (.indexOf headers column-name) ;; Find the index of the specified column
        ;; Filter out the specified column from each row
        filtered-data (if (>= column-index 0) ;; Ensure column exists
                        (mapv
                          (fn [row]
                            (vec (keep-indexed
                                   (fn [i v]
                                     (when (not= i column-index)
                                       v))
                                   row)))
                          data)
                        data)] ;; If column doesn't exist, return data unchanged
    filtered-data))

;Id column is not needed for prediction
(def cleaned-data
  (drop-column cancer-data "id"))

;(println "Cleaned data (column 'id' dropped):")
;(println cleaned-data)
;
;(println "Cleaned data (column 'id' dropped) first 10 rows:")
;(doseq [row (take 10 cleaned-data)]
;  (println row))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; DIAGNOSIS CHANGE FROM M or B TO 1 or 0
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



(defn encode-diagnosis
  "Converts M to 1 and B to 0 in the diagnosis column"
  [data]
  (let [headers (get-headers data)
        column-index (.indexOf headers "diagnosis") ;; Find the diagnosis column index
        ;; Transform diagnosis column values
        transformed-data
        (mapv
          (fn [row]
            (if (= row headers)
              row ;; Keep headers unchanged
              (assoc row column-index
                         (if (= (nth row column-index) "M") "1" "0"))))
          data)]
    transformed-data))


(defn extract-y
  "Extract the target variable (y) as the encoded diagnosis column"
  [data]
  (map #(nth % (.indexOf (get-headers data) "diagnosis")) (rest data))) ;; Exclude headers


(defn drop-diagnosis-column
  "Remove the diagnosis column to create x_data"
  [data]
  (drop-column data "diagnosis"))


(def encoded-data (encode-diagnosis cleaned-data)) ;; Encodes "diagnosis" column

;; Encoded diagnosis data = y-data. Stores results of diagnosis (0 and 1)
(def y-data (extract-y encoded-data)) ;; Extracts encoded "diagnosis" column as y
(def x-data (drop-diagnosis-column encoded-data)) ;; Drops "diagnosis" column for x_data


;(println "Encoded Data:")
;(doseq [row (take 10 encoded-data)] (println row))
;
;(println "\nY Data:")
;(println y-data)
;
;(println "\nX Data:")
;(doseq [row (take 10 x-data)] (println row))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; DATA NORMALIZATION
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn normalize-data
  "Normalizes the data (excluding headers)."
  [data]
  (let [headers (get-headers data)
        rows (rest data)
        ;; Transpose rows to get columns
        columns (apply map vector rows)

        normalized-columns
        (mapv
          (fn [col]
            (if (every? number? col) ;; Ensure the column contains numeric values
              (let [min-val (apply min col)
                    max-val (apply max col)
                    range (- max-val min-val)]
                (mapv #(if (zero? range) 0.0
                                         (/ (- % min-val) range))
                      col))
              col)) ;; If not numeric, leave the column unchanged
          columns)
        ;; Transpose normalized columns back into rows
        normalized-rows (apply map vector normalized-columns)]
    ;; Add headers back to the normalized data
    (cons headers normalized-rows)))


(def normalized-data (normalize-data encoded-data))

(println "Normalized X Data:")
(doseq [row (take 10 normalized-data)] (println row))





;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; TRAIN AND TEST DATA
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



(defn train-test-split [data p]
  (let [n (count data)
        size (int (* n p))
        shuffled-data (shuffle data)]
    {:train (subvec shuffled-data 0 size)
     :test (subvec shuffled-data size n)}))

(def split-data
  (train-test-split (rest normalized-data) 0.7))


(println "Train data:" (:train split-data))
(println "Test data: " (:test split-data))




(defn transform-data [data]
  (mapv (fn [sample]
          (let [attributes (map #(Double/parseDouble (str %)) (subvec sample 1 31))
                cancer-type (if (= (nth sample 0) "1") :M :B)]
            {:attributes attributes
             :cancer-type cancer-type}))
        data))

(def transformed-train-data (transform-data (:train split-data)))
(def transformed-test-data (transform-data (:test split-data)))

(println transformed-train-data)

(println transformed-test-data)




(defn transform-data-without-class [data]
  (mapv (fn [sample]
          {:attributes (map #(Double/parseDouble (str %)) (:attributes sample))})
        data))
(def transformed-test-data-without-class
  (transform-data-without-class transformed-test-data))

(println transformed-test-data-without-class)



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;; KNN
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn euclidean-distance [first-vector second-vector]
  (Math/sqrt (reduce + (map #(* % %) (map - first-vector second-vector)))))

(defn nearest-neighbors [train-data new-data k]
  (take k
        (sort-by :distance
                 (map #(assoc % :distance (euclidean-distance (:attributes %) new-data)) train-data))))


(defn knn [train-data new-data k]
  (let [nearest-neighbors (nearest-neighbors train-data new-data k)
        classes (map :cancer-type nearest-neighbors)
        frequencies (frequencies classes)]
    (first (first (sort-by val > frequencies)))))

(def train-data transformed-train-data)
(println train-data)
(def test-data transformed-test-data-without-class)

(doseq [cancer-patient test-data]
  (let [k 3
        predicted-class (knn train-data (:attributes cancer-patient) k)]
    (println "Cancer patient characteristics:")
    (println cancer-patient)
    (println "----------------------------------------------")
    (println "Prediction:")
    (if (= predicted-class :M)
      (println "Cancer type is Malignant.")
      (println "Cancer type is Benign."))
    (println "----------------------------------------------")))

(def actual-data (map :cancer-type transformed-test-data))

(println actual-data)

(def predicted-data
  (map #(knn train-data (:attributes %) 3) test-data))

(println predicted-data)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;; Evaluation of predicted and actual results
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(eval-met/calculate-measures actual-data predicted-data)