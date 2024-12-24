(ns breast-cancer-prediction-knn.knn-model)


(defn euclidean-distance
  "Calculates the Euclidean distance between two vectors"
  [first-vector second-vector]

  (Math/sqrt (reduce + (map #(* % %) (map - first-vector second-vector)))))

(defn nearest-neighbors
  "Finds k nearest neighbours"
  [train-data new-data k]

  (take k
        (sort-by :distance
                 (map #(assoc % :distance (euclidean-distance (:attributes %) new-data)) train-data))))


(defn knn
  "KNN classification algorithm"
  [train-data new-data k]

  (let [nearest-neighbors (nearest-neighbors train-data new-data k)
        classes (map :cancer-type nearest-neighbors)
        frequencies (frequencies classes)]
    (first (first (sort-by val > frequencies)))))
