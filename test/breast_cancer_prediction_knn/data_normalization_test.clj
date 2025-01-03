(ns breast-cancer-prediction-knn.data-normalization-test
  (:require [midje.sweet :refer [fact throws =>]])
  (:require [breast-cancer-prediction-knn.load-data :refer [load-data]])
  (:require [breast-cancer-prediction-knn.data-normalization :refer [normalize-data]])
  (:import (java.io FileNotFoundException)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; normalize-data tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(fact (seq? (normalize-data (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") )) => true)
(fact (normalize-data (load-data "")) => (throws FileNotFoundException))
(fact (normalize-data (load-data nil)) => (throws IllegalArgumentException))