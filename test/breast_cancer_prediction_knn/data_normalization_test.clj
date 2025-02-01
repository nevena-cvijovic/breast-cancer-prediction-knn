(ns breast-cancer-prediction-knn.data-normalization-test
  (:require [midje.sweet :refer [fact throws =>]]
            [breast-cancer-prediction-knn.load-data :refer [load-data]]
            [breast-cancer-prediction-knn.data-normalization :refer [normalize-data]])
  (:import (java.io FileNotFoundException)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; normalize-data tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(fact "Returns normalized data (values between [0,1])."
      (seq? (normalize-data (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") )) => true)
(fact "Throws FileNotFoundException if input is wrong."
      (normalize-data (load-data "")) => (throws FileNotFoundException))
(fact "Throws IllegalArgumentException if there is no input."
      (normalize-data (load-data nil)) => (throws IllegalArgumentException))