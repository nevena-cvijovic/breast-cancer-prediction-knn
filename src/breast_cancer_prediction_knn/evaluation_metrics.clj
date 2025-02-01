(ns breast-cancer-prediction-knn.evaluation-metrics)



(defn true-positive
  "Calculates how many Benign predicted diagnosis are also Benign actual diagnosis."
  [actual-values predicted-values]
  (count (filter (fn [[actual predicted]] (and (= actual :B) (= predicted :B)))
                 (map vector actual-values predicted-values))))

(defn true-negative
  "Calculates how many Malignant predicted diagnosis are also Malignant actual diagnosis."
  [actual-values predicted-values]
  (count (filter (fn [[actual predicted]] (and (= actual :M) (= predicted :M)))
                 (map vector actual-values predicted-values))))

(defn false-positive
  "Calculates how many Benign predicted diagnosis are Malignant actual diagnosis."
  [actual-values predicted-values]
  (count (filter (fn [[actual predicted]] (and (= actual :M) (= predicted :B)))
                 (map vector actual-values predicted-values))))

(defn false-negative
  "Calculates how many Malignant predicted diagnosis are Benign actual diagnosis."
  [actual-values predicted-values]
  (count (filter (fn [[actual predicted]] (and (= actual :B) (= predicted :M)))
                 (map vector actual-values predicted-values))))

(defn calculate-measures
  "Calculates accuracy, precision, recall, and F1 score based on actual and predicted values."
  [actual predicted]
  (let [fp (false-positive actual predicted)
        tp (true-positive actual predicted)
        fn (false-negative actual predicted)
        tn (true-negative actual predicted)]
    (let [accuracy (double (/ (+ tp tn) (+ tp tn fp fn)))
          precision (double (if (zero? (+ tp fp)) 0 (/ tp (+ tp fp))))
          recall (double (if (zero? (+ tp fn)) 0 (/ tp (+ tp fn))))
          f1 (double (* 2 (/ (* precision recall) (+ precision recall))))]
      {:accuracy accuracy
       :precision precision
       :recall recall
       :f1 f1})))