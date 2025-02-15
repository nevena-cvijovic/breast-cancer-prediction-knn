(ns breast-cancer-prediction-knn.data-normalization)

(defn min-max
  [col]
  (reduce (fn [[min-val max-val] x]
            [(min min-val x) (max max-val x)])
          [(first col) (first col)]
          col))

(defn normalize-column
  [col min-val max-val]
  (let [range (- max-val min-val)]
    (mapv #(if (zero? range) 0.0 (/ (- % min-val) range)) col)))


(defn normalize-data
  "Normalizes the data - parallel processing."
  [data]
  (let [headers (first data)
        rows (rest data)
        columns (apply map vector rows)
        normalized-columns (pmap (fn [col]
                                   (if (every? number? col)
                                     (let [[min-val max-val] (min-max col)]
                                       (normalize-column col min-val max-val))
                                     col))
                                 columns)
        normalized-rows (apply map vector normalized-columns)]
    (cons headers normalized-rows)))