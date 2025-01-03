(ns breast-cancer-prediction-knn.data-manipulation-test
  (:require [midje.sweet :refer [fact throws =>]])
  (:require [breast-cancer-prediction-knn.load-data :refer [load-data]])
  (:require [breast-cancer-prediction-knn.data-manipulation :refer [get-headers get-column dataset-info last-rows drop-column encode-diagnosis]])
  (:import (java.io FileNotFoundException)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; get-headers tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(fact (vector? (get-headers (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") )) => true)
(fact (get-headers (load-data "")) => (throws FileNotFoundException))
(fact (get-headers (load-data nil)) => (throws IllegalArgumentException))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; get-headers tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def column-name "randomname")

(fact (seq? (get-column (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") "diagnosis" )) => true)

(fact (get-column (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") column-name ) => nil)

(fact (get-column (load-data "") "diagnosis" ) => (throws FileNotFoundException))
(fact (get-column (load-data nil) "diagnosis" ) => (throws IllegalArgumentException))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; dataset-info tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(fact (map? (dataset-info (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") )) => true)
(fact (dataset-info (load-data "")) => (throws FileNotFoundException))
(fact (dataset-info (load-data nil)) => (throws IllegalArgumentException))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;last-rows tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(fact (seq? (last-rows (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") 10)) => true)
(fact (empty? (last-rows (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") 0)) => true)
(fact (empty? (last-rows (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") -10)) => true)
(fact (last-rows (load-data "") 10) => (throws FileNotFoundException))
(fact (last-rows (load-data nil) 10) => (throws IllegalArgumentException))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;drop-column tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(fact (vector? (drop-column (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") "diagnosis" )) => true)
(fact (drop-column (load-data "") "diagnosis" ) => (throws FileNotFoundException))
(fact (drop-column (load-data nil) "diagnosis" ) => (throws IllegalArgumentException))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;encode-diagnosis tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(fact (vector? (encode-diagnosis (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") )) => true)
(fact (encode-diagnosis (load-data "")) => (throws FileNotFoundException))
(fact (encode-diagnosis (load-data nil)) => (throws IllegalArgumentException))

