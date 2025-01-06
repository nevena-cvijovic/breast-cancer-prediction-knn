(ns breast-cancer-prediction-knn.data-transformation
  (:import [java.lang Integer])
  (:require [breast-cancer-prediction-knn.load-data :as load-data]))


(defn parse-value
  "Converts string from dataset into integer if the value has only digits in it"
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
  "Converts the value of each observation in the dataset to the true data type and returns the converted dataset"
  [csv-file]

  (map (fn [x] (parse-row x)) (load-data/load-data csv-file)))
