(ns breast-cancer-prediction-knn.data-manipulation)

(defn get-headers
  "Get column names (header)"
  [data]

  (first data))


(defn get-column
  "Extract a specific column by its name"
  [data column-name]

  (let [headers (get-headers data)
        column-index (.indexOf headers column-name)]
    (if (>= column-index 0)
      (map #(nth % column-index nil) (rest data))
      (println (str "Column " column-name " not found!")))))


(defn dataset-info
  "Get general information about the dataset"
  [data]

  (let [headers (get-headers data)
        num-rows (count (rest data)) ;; Exclude header
        num-cols (count headers)]
    {:num-rows num-rows
     :num-cols num-cols
     :columns headers}))


(defn last-rows
  "Get the last N rows of dataset"
  [data n]

  (let [rows (rest data)] ;; Exclude header
    (take-last n rows)))

(defn drop-column
  "Drops a single column by column name"
  [data column-name]

  (let [headers (get-headers data) ;; Extract headers (first row)
        column-index (.indexOf headers column-name) ;; Find the index of the specified column
        ;; Filter out the specified column from each row
        filtered-data (if (>= column-index 0) ;; Only if column exists
                        (mapv
                          (fn [row]
                            (vec (keep-indexed
                                   (fn [i v]
                                     (when (not= i column-index)
                                       v))
                                   row)))
                          data)
                        data)] ;; If column doesn't exist, return data unchanged
    filtered-data))


(defn encode-diagnosis
  "Converts M to 1 and B to 0 in the diagnosis column"
  [data]

  (let [headers (get-headers data)
        column-index (.indexOf headers "diagnosis") ;; Find the diagnosis column index
        ;; Transform diagnosis column values
        transformed-data
        (mapv
          (fn [row]
            (if (= row headers)
              row ;; Keep headers unchanged
              (assoc row column-index
                         (if (= (nth row column-index) "M") "1" "0"))))
          data)]
    transformed-data))