(ns breast-cancer-prediction-knn.core
  (:import [java.lang Integer])
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]


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


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; PRINTING and PRESENTING DATA
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
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


(println "Dataset Information:")
(println (dataset-info cancer-data))

(println "\nColumn 'diagnosis':")
(println (get-column cancer-data "diagnosis"))

(println "\nLast 5 rows of the dataset:")
(println (last-rows cancer-data 5))



