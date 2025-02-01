(ns breast-cancer-prediction-knn.load-data-test
  (:require [midje.sweet :refer [fact throws =>]]
            [breast-cancer-prediction-knn.load-data :refer [load-data]])
  (:import (java.io FileNotFoundException)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; load-data tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(fact "Returns vector of vectors."
      (vector? (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv")) => true)

(fact "Throws FileNotFoundException if input is wrong."
      (vector? (load-data "")) => (throws FileNotFoundException))

(fact "Throws IllegalArgumentException if there is no input."
      (vector? (load-data nil)) => (throws IllegalArgumentException))
