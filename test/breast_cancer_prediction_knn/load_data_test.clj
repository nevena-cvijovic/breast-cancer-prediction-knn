(ns breast-cancer-prediction-knn.load-data-test
  (:require [midje.sweet :refer [fact throws =>]])
  (:require [breast-cancer-prediction-knn.load-data :refer [load-data]])
  (:import (java.io FileNotFoundException)))


(fact "return vector of vectors" (vector? (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv")) => true)

(fact "fail if wrong input" (vector? (load-data "")) => (throws FileNotFoundException))

(fact "NullPointerException if no input is set" (vector? (load-data nil)) => (throws IllegalArgumentException))
