(ns breast-cancer-prediction-knn.train-and-test-split-test
  (:require [midje.sweet :refer [fact facts throws =>]])
  (:require [breast-cancer-prediction-knn.load-data :refer [load-data]])
  (:require [breast-cancer-prediction-knn.train-and-test-split :refer [train-test-split transform-data transform-data-without-class]])
  (:import (java.io FileNotFoundException)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; train-test-split test
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(facts "Train and test split data testing"
       (let [dataset (train-test-split (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") 0.7)]
         (fact "True if result is hash map" (map? dataset) => true)
         (fact "True if contains :train" (contains? dataset :train) => true)
         (fact "True if contains :test" (contains? dataset :test) => true)
         ))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; transform-data tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(fact "Transforms dataset into a structured format - attributes and cancer type (diagnosis column of dataset)"
      (vector? (transform-data (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") )) => true
      )
(fact "Throws FileNotFoundException if input is wrong"
      (transform-data (load-data "")) => (throws FileNotFoundException)
      )
(fact "Throws IllegalArgumentException if there is no input"
      (transform-data (load-data nil)) => (throws IllegalArgumentException)
      )

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; transform-data-without-class tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(fact "Extracting only attributes from structured dataset"
      (vector? (transform-data-without-class (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") )) => true
      )
(fact "Throws FileNotFoundException if input is wrong"
      (transform-data-without-class (load-data "")) => (throws FileNotFoundException)
      )
(fact "Throws IllegalArgumentException if there is no input"
      (transform-data-without-class (load-data nil)) => (throws IllegalArgumentException)
      )