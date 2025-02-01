(ns breast-cancer-prediction-knn.data-normalization
  (:require [breast-cancer-prediction-knn.data-manipulation :as data-man]))

(defn normalize-data
  "Normalizes the data (excluding header)."
  [data]
  (let [headers (data-man/get-headers data)
        rows (rest data)
        columns (apply map vector rows)
        normalized-columns (mapv (fn [col]
                                   (if (every? number? col)
                                     (let [min-val (apply min col)
                                           max-val (apply max col)
                                           range (- max-val min-val)]
                                       (mapv #(if (zero? range)
                                                0.0
                                                (/ (- % min-val) range))
                                             col))
                                     col))
                                 columns)
        normalized-rows (apply map vector normalized-columns)]
    (cons headers normalized-rows)))
