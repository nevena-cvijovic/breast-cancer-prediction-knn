(ns breast-cancer-prediction-knn.data-manipulation)

(defn get-headers
  "Get column names (header)."
  [data]
  (first data))


(defn get-column
  "Extract a specific column by its name."
  [data column-name]
  (let [headers (get-headers data)
        column-index (.indexOf headers column-name)]
    (if (>= column-index 0)
      (map #(nth % column-index nil) (rest data))
      (println (str "Column " column-name " not found!")))))


(defn dataset-info
  "Get general information about the dataset."
  [data]
  (let [headers (get-headers data)
        num-rows (count (rest data))
        num-cols (count headers)]
    {:num-rows num-rows
     :num-cols num-cols
     :columns headers}))


(defn last-rows
  "Get the last N rows of dataset."
  [data n]
  (let [rows (rest data)]
    (take-last n rows)))


(defn drop-column
  "Drops a single column by column name."
  [data column-name]
  (let [headers (get-headers data)
        column-index (.indexOf headers column-name)
        filtered-data (if (>= column-index 0)
                        (mapv (fn [row] (vec (keep-indexed (fn [i v] (when (not= i column-index) v)) row))) data)
                        data)]
    filtered-data))


(defn encode-diagnosis
  "Converts M to 1 and B to 0 in the diagnosis column."
  [data]
  (let [headers (get-headers data)
        column-index (.indexOf headers "diagnosis")
        transformed-data
        (mapv (fn [row]
                (if (= row headers)
                  row
                  (assoc row column-index (if (= (nth row column-index) "M") "1" "0"))))
              data)]
    transformed-data))