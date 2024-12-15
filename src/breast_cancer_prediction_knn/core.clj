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
(println cancer-data)
