(ns breast-cancer-prediction-knn.load-data

  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(defn load-data
  "Loads data from csv file"
  [csv-file]
  (with-open [reader (io/reader csv-file)]
    (->> (csv/read-csv reader)
         (mapv vec))
    )
  )
