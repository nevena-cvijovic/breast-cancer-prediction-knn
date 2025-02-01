(ns breast-cancer-prediction-knn.data-manipulation-test
  (:require [midje.sweet :refer [fact throws =>]]
            [breast-cancer-prediction-knn.load-data :refer [load-data]]
            [breast-cancer-prediction-knn.data-manipulation :refer [get-headers get-column dataset-info last-rows drop-column encode-diagnosis]])
  (:import (java.io FileNotFoundException)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; get-headers tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(fact "Returns header of data in a vector."
      (vector? (get-headers (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") )) => true)
(fact "Throws FileNotFoundException if input is wrong."
      (get-headers (load-data "")) => (throws FileNotFoundException))
(fact "Throws IllegalArgumentException if there is no input."
      (get-headers (load-data nil)) => (throws IllegalArgumentException))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; get-headers tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def column-name "randomname")

(fact "Returns diagnosis column from data in a sequence."
      (seq? (get-column (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") "diagnosis" )) => true)

(fact "Prints there is Column randomname not found! and returns nil."
      (get-column (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") column-name ) => nil)

(fact "Throws FileNotFoundException if input is wrong."
      (get-column (load-data "") "diagnosis" ) => (throws FileNotFoundException))
(fact "Throws IllegalArgumentException if there is no input."
      (get-column (load-data nil) "diagnosis" ) => (throws IllegalArgumentException))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; dataset-info tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(fact "Returns number of rows, number of columns and name of columns from data in a map."
      (map? (dataset-info (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") )) => true)
(fact "Throws FileNotFoundException if input is wrong."
      (dataset-info (load-data "")) => (throws FileNotFoundException))
(fact "Throws IllegalArgumentException if there is no input."
      (dataset-info (load-data nil)) => (throws IllegalArgumentException))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;last-rows tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(fact "Returns 10 last rows from data."
      (seq? (last-rows (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") 10)) => true)
(fact "Returns empty list."
      (empty? (last-rows (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") 0)) => true)
(fact "Returns empty list for a negative number."
      (empty? (last-rows (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") -10)) => true)
(fact "Throws FileNotFoundException if input is wrong."
      (last-rows (load-data "") 10) => (throws FileNotFoundException))
(fact "Throws IllegalArgumentException if there is no input."
      (last-rows (load-data nil) 10) => (throws IllegalArgumentException))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;drop-column tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(fact "Returns data without diagnosis column."
      (vector? (drop-column (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") "diagnosis" )) => true)
(fact "Throws FileNotFoundException if input is wrong."
      (drop-column (load-data "") "diagnosis" ) => (throws FileNotFoundException))
(fact "Throws IllegalArgumentException if there is no input."
      (drop-column (load-data nil) "diagnosis" ) => (throws IllegalArgumentException))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;encode-diagnosis tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(fact "Returns data where diagnosis column have values 1 and 0 instead of 'M' and 'B'."
      (vector? (encode-diagnosis (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") )) => true)
(fact "Throws FileNotFoundException if input is wrong."
      (encode-diagnosis (load-data "")) => (throws FileNotFoundException))
(fact "Throws IllegalArgumentException if there is no input."
      (encode-diagnosis (load-data nil)) => (throws IllegalArgumentException))

