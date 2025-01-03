(ns breast-cancer-prediction-knn.knn-model-test
  (:require [midje.sweet :refer [fact throws =>]])
  (:require [breast-cancer-prediction-knn.load-data :refer [load-data]])
  (:require [breast-cancer-prediction-knn.train-and-test-split :refer [train-test-split transform-data transform-data-without-class]])
  (:require [breast-cancer-prediction-knn.knn-model :refer [euclidean-distance nearest-neighbors knn]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; euclidean-distance test
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def first-vector [1 2 3])
(def second-vector [4 5 6])

(fact "Euclidean distance between two vectors"
      (euclidean-distance first-vector second-vector) => 5.196152422706632)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; nearest-neighbors test
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def train-data
  [{:attributes [1 2 3] :cancer-type :M}
   {:attributes [4 5 6] :cancer-type :B}
   {:attributes [7 8 9] :cancer-type :M}])

(def new-data [2 3 4])

(fact "Finds k nearest neighbors"
      (let [neighbors (nearest-neighbors train-data new-data 2)] ; Find 2 nearest neighbors
        (count neighbors) => 2
        (:cancer-type (first neighbors)) => :M
        (:cancer-type (second neighbors)) => :B
        (every? #(contains? % :distance) neighbors) => true  ; Ensure every result has a :distance key
        ))

(fact "Handle case when k is greater than number of training data points"
      (let [neighbors (nearest-neighbors train-data new-data 10)] ; k > train-data size
        (count neighbors) => 3
        ))

(fact "Handles case when new data is identical to a training point"
      (let [new-data [1 2 3] ; Identical to the first point in train-data
            neighbors (nearest-neighbors train-data new-data 1)]
        (:cancer-type (first neighbors)) => :M
        ))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; knn test
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(fact "Classifies new data correctly using KNN"
      (knn train-data new-data 2) => :M  ;; We expect the result to be :M since most neighbors are of class :M

      (knn train-data new-data 3) => :M  ;; Even with k=3, the result should still be :M as it's the majority class
      )

(fact "Handles edge case with all neighbors of the same class"
      (let [train-data [{:attributes [1 2 3] :cancer-type :B}
                        {:attributes [4 5 6] :cancer-type :B}
                        {:attributes [7 8 9] :cancer-type :B}]
            new-data [5 6 7]]
        (knn train-data new-data 2) => :B  ;; All neighbors are of class :B, so it should return :B
        ))

(fact "Handles edge case with k = 1"
      (let [train-data [{:attributes [1 2 3] :cancer-type :M}
                        {:attributes [4 5 6] :cancer-type :B}]
            new-data [2 3 4]]
        (knn train-data new-data 1) => :M  ;; With k=1, it should return the class of the nearest neighbor, :M
        ))

(def dataset (train-test-split (load-data "src/breast_cancer_prediction_knn/Cancerdata.csv") 0.7))
(def train-data2 (transform-data (:train dataset)))
(def test-data2 (transform-data (:test dataset)))
(def transformed-test-data-without-class (transform-data-without-class test-data2))
(fact (seq? (map #(knn train-data2 (:attributes %) 3) transformed-test-data-without-class)) => true)


