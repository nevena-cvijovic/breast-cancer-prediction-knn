(ns breast-cancer-prediction-knn.data-normalization

  (:require [breast-cancer-prediction-knn.data-manipulation :as data-man]))

(defn normalize-data
  "Normalizes the data (excluding header)."
  [data]

  (let [headers (data-man/get-headers data)
        rows (rest data)
        ;; Transpose rows to get columns
        columns (apply map vector rows)

        normalized-columns
        (mapv
          (fn [col]
            (if (every? number? col) ;; Only if the column contains numeric values
              (let [min-val (apply min col)
                    max-val (apply max col)
                    range (- max-val min-val)]
                (mapv #(if (zero? range) 0.0
                                         (/ (- % min-val) range))
                      col))
              col)) ;; If not numeric, leave the column unchanged
          columns)
        ;; Transpose normalized columns back into rows
        normalized-rows (apply map vector normalized-columns)]
    ;; Add headers back to the normalized data
    (cons headers normalized-rows)
    ))
