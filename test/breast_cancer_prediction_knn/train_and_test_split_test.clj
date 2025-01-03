(ns breast-cancer-prediction-knn.train-and-test-split-test
  (:require [midje.sweet :refer [fact facts throws =>]])
  (:require [breast-cancer-prediction-knn.load-data :refer [load-data]])
  (:require [breast-cancer-prediction-knn.train-and-test-split :refer [train-test-split transform-data transform-data-without-class]])
  (:import (java.io FileNotFoundException)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; train-test-split test
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(facts "get-test-and-train-datasets testing"
       (let [dataset (train-test-split (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") 0.7)]
         (fact "if result is hash map" (map? dataset) => true)
         (fact "if contains :train" (contains? dataset :train) => true)
         (fact "if contains :test" (contains? dataset :test) => true)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; transform-data tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(fact (vector? (transform-data (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") )) => true)
(fact (transform-data (load-data "")) => (throws FileNotFoundException))
(fact (transform-data (load-data nil)) => (throws IllegalArgumentException))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; transform-data-without-class tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(fact (vector? (transform-data-without-class (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") )) => true)
(fact (transform-data-without-class (load-data "")) => (throws FileNotFoundException))
(fact (transform-data-without-class (load-data nil)) => (throws IllegalArgumentException))