(ns breast-cancer-prediction-knn.core

  (:require
            [breast-cancer-prediction-knn.data-transformation :as load-data]
            [breast-cancer-prediction-knn.data-manipulation :as data-man]
            [breast-cancer-prediction-knn.data-normalization :as data-norm]
            [breast-cancer-prediction-knn.train-and-test-split :as tts]
            [breast-cancer-prediction-knn.knn-model :as knn]
            [breast-cancer-prediction-knn.evaluation-metrics :as eval-met]

            ))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; LOADING DATA and DATA TRANSFORMATION
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(def csv-file "src/breast_cancer_prediction_knn/Cancerdata.csv")

(def cancer-data (load-data/convert-dataset csv-file))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; PRINTING and PRESENTING DATA
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;(println cancer-data)
(doseq [row (take 10 cancer-data)]
  (println row))

(println "Dataset Information:")
(println (data-man/dataset-info cancer-data))

(println "\nColumn 'diagnosis':")
(println (data-man/get-column cancer-data "diagnosis"))

(println "\nLast 5 rows of the dataset:")
(println (data-man/last-rows cancer-data 5))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; DATA MANIPULATION
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;Id column is not needed for prediction
(def cleaned-data
  (data-man/drop-column cancer-data "id"))

(println "Cleaned data (column 'id' dropped):")
(println cleaned-data)

(println "Cleaned data (column 'id' dropped) first 10 rows:")
(doseq [row (take 10 cleaned-data)]
  (println row))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; DIAGNOSIS CHANGE FROM M or B TO 1 or 0
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(def encoded-data (data-man/encode-diagnosis cleaned-data)) ;; Encodes "diagnosis" column

(println "Encoded Data:")
(doseq [row (take 10 encoded-data)]
  (println row))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; DATA NORMALIZATION
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def normalized-data (data-norm/normalize-data encoded-data))

(println "Normalized data:")
(doseq [row (take 10 normalized-data)] (println row))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; TRAIN AND TEST DATA
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def split-data (tts/train-test-split (rest normalized-data) 0.7))

(println "Train data:" (:train split-data))
(println "Test data: " (:test split-data))


(def transformed-train-data (tts/transform-data (:train split-data)))
(def transformed-test-data (tts/transform-data (:test split-data)))

(println transformed-train-data)

(println transformed-test-data)

;test data without final results
(def transformed-test-data-without-class (tts/transform-data-without-class transformed-test-data))
(println transformed-test-data-without-class)



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;; KNN model and training
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def train-data transformed-train-data)
(println train-data)
(def test-data transformed-test-data-without-class)

(doseq [cancer-patient test-data]
  (let [k 3
        predicted-class (knn/knn train-data (:attributes cancer-patient) k)]
    (println "Cancer patient characteristics:")
    (println cancer-patient)
    (println "#################################")
    (println "Prediction:")
    (if (= predicted-class :M)
      (println "Cancer type is Malignant.")
      (println "Cancer type is Benign."))
    (println "#################################")))

(def actual-data (map :cancer-type transformed-test-data))

(println "Actual data:")
(println actual-data)

(def predicted-data
  (map #(knn/knn train-data (:attributes %) 3) test-data))

(println "Predicted data:")
(println predicted-data)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;; Evaluation of predicted and actual results
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(eval-met/calculate-measures actual-data predicted-data)