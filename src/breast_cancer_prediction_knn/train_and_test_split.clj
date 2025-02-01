(ns breast-cancer-prediction-knn.train-and-test-split)


(defn train-test-split
  "Splits dataset into test and train datasets."
  [data p]
  (let [n (count data)
        size (int (* n p))
        shuffled-data (shuffle data)]
    {:train (subvec shuffled-data 0 size)
     :test (subvec shuffled-data size n)}))

(defn transform-data
  "Transforms dataset into a structured format - attributes and cancer type (diagnosis column of dataset)."
  [data]
  (mapv (fn [sample]
          (let [attributes (map #(Double/parseDouble (str %)) (subvec sample 1 31))
                cancer-type (if (= (nth sample 0) "1") :M :B)]
            {:attributes attributes
             :cancer-type cancer-type}))
        data))

(defn transform-data-without-class
  "Extracting only attributes from structured dataset."
  [data]
  (mapv (fn [sample]
          {:attributes (map #(Double/parseDouble (str %)) (:attributes sample))})
        data))