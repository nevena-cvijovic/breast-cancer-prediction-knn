(ns breast-cancer-prediction-knn.data-transformation-test
  (:require [midje.sweet :refer [fact throws =>]])
  (:require [breast-cancer-prediction-knn.data-transformation :refer [parse-value convert-dataset]])
  (:import (java.io FileNotFoundException)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; parse-value tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(fact (number? (parse-value "1")) => true)
(fact (number? (parse-value "1.2")) => true)
(fact (boolean? (parse-value "Yes")) => true)
(fact (boolean? (parse-value "No")) => true)
(fact (string? (parse-value "something")) => true)
(fact (parse-value nil) => (throws NullPointerException))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; convert-dataset tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(fact (seq? (convert-dataset "src/breast_cancer_prediction_knn/Cancerdata.csv")) => true)
(fact (seq? (convert-dataset "")) => (throws FileNotFoundException))
(fact (seq? (convert-dataset nil)) => (throws IllegalArgumentException))