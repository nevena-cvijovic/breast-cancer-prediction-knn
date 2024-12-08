(ns breast-cancer-prediction-knn.core

  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))



(defn load-data
  "Function for loading data from csv file"
  [csv-file]
  (with-open [reader (io/reader csv-file)]
    (->> (csv/read-csv reader)
         (mapv vec))
    )
  )

(def csv-file "src/breast_cancer_prediction_knn/Cancerdata.csv")

;(load-data csv-file)
