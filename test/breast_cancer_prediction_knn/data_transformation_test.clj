(ns breast-cancer-prediction-knn.data-transformation-test
  (:require [midje.sweet :refer [fact throws =>]])
  (:require [breast-cancer-prediction-knn.data-transformation :refer [parse-value convert-dataset]])
  (:import (java.io FileNotFoundException)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; parse-value tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(fact "Test for integer number"
      (number? (parse-value "7")) => true
      )
(fact "Test for real number"
      (number? (parse-value "7.5")) => true
      )
(fact "Test for string"
      (string? (parse-value "some string")) => true
      )
(fact "Test for boolean if it is true/yes"
      (boolean? (parse-value "Yes")) => true
      )
(fact "Test for boolean if it is false/no"
      (boolean? (parse-value "No")) => true
      )

(fact "Throws NullPointerException if there is no input"
      (parse-value nil) => (throws NullPointerException)
      )

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; convert-dataset tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(fact "Returns dataset with the true data type for each column"
      (seq? (convert-dataset "src/breast_cancer_prediction_knn/Cancerdata.csv")) => true
      )
(fact "Throws FileNotFoundException if input is wrong"
      (seq? (convert-dataset "")) => (throws FileNotFoundException)
      )
(fact "Throws IllegalArgumentException if there is no input"
      (seq? (convert-dataset nil)) => (throws IllegalArgumentException)
      )