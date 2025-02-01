(ns breast-cancer-prediction-knn.evaluation-metrics_test
  (:require [midje.sweet :refer [fact throws =>]]
            [breast-cancer-prediction-knn.evaluation-metrics :refer [true-positive true-negative false-positive false-negative calculate-measures]]))


(def actual [:M :B :B :B :M :M :M :B :M :B :B :B :M :M :M :B :B :M :B :B :B :M :M :B :M
             :B :B :M :B :B :M :B :M :B :M :B :B :B :B :B :B :B :B :B :B :B :M :B :M :B
             :B :B :B :B :B :B :B :B :B :M :B :M :B :M :B :B :M :B :B :B :B :B :B :M :B
             :M :M :M :B :M :B :B :B :M :B :M :M :B :B :M :M :B :B :B :B :B :M :B :M :M
             :B :B :M :M :B :B :B :M :M :M :B :B :M :M :B :M :M :B :M :M :B :B :B :B :B
             :B :B :B :M :M :B :M :B :B :B :M :M :B :B :B :B :B :B :M :B :B :B :M :M :B
             :M :M :B :B :B :B :B :B :M :M :B :B :B :B :B :B :B :B :B :M :B])
(def predicted [:M :B :B :B :M :M :M :B :M :B :B :B :M :M :M :B :B :M :B :B :B :M :M :B
                :M :B :B :M :B :B :M :B :M :B :M :B :B :B :B :B :B :B :B :B :B :B :M :B
                :M :B :B :B :B :B :B :B :B :B :B :M :B :M :B :M :B :B :M :B :B :B :B :B
                :B :M :B :M :B :M :B :M :B :B :B :M :B :M :M :B :B :M :M :B :B :B :B :B
                :M :B :M :M :B :B :M :M :B :B :B :M :M :M :B :B :M :M :B :B :M :B :M :M
                :B :B :B :B :B :B :B :B :M :M :B :M :B :B :B :M :M :B :B :B :B :B :B :M
                :B :B :B :M :M :B :M :M :B :B :M :B :B :B :M :M :B :B :B :B :B :B :B :B
                :B :M :B])



(fact "True positives are counted correctly."
      (true-positive actual predicted) => 111)

(fact "True negatives are counted correctly."
      (true-negative actual predicted) => 57)

(fact "False positives are counted correctly."
      (false-positive actual predicted) => 2)

(fact "False negatives are counted correctly."
      (false-negative actual predicted) => 1)



(fact "Calculate measures works correctly for accuracy, precision, recall, and F1."
      (calculate-measures actual predicted) =>
      {:accuracy 0.9824561403508772
       :precision 0.9823008849557522
       :recall 0.9910714285714286
       :f1 0.9866666666666667})