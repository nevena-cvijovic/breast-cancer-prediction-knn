(ns breast-cancer-prediction-knn.evaluation-metrics_test
  (:require [midje.sweet :refer [fact throws =>]]
            [breast-cancer-prediction-knn.evaluation-metrics :refer [build-confusion-matrix calculate-measures]]))


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



(fact "Confusion matrix is calculated correctly"
      (build-confusion-matrix actual predicted) => {:tp 111 :tn 57 :fp 2 :fn 1})

(fact "Calculate measures works correctly for accuracy, precision, recall, and F1."
      (calculate-measures actual predicted) =>
      {:accuracy 0.9824561403508772
       :precision 0.9823008849557522
       :recall 0.9910714285714286
       :f1 0.9866666666666667
       :confusion-matrix {:true-positive 111
                          :true-negative 57
                          :false-positive 2
                          :false-negative 1}})