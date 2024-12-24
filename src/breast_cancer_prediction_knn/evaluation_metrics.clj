(ns breast-cancer-prediction-knn.evaluation-metrics)



(defn true-positive [actual-values predicted-values]
  (count (filter (fn [[actual predicted]] (and (= actual :B) (= predicted :B))) (map vector actual-values predicted-values))))

(defn true-negative [actual-values predicted-values]
  (count (filter (fn [[actual predicted]] (and (= actual :M) (= predicted :M))) (map vector actual-values predicted-values))))

(defn false-positive [actual-values predicted-values]
  (count (filter (fn [[actual predicted]] (and (= actual :M) (= predicted :B))) (map vector actual-values predicted-values))))

(defn false-negative [actual-values predicted-values]
  (count (filter (fn [[actual predicted]] (and (= actual :B) (= predicted :M))) (map vector actual-values predicted-values))))



(defn calculate-measures [actual predicted]
  (let [fp (false-positive actual predicted)
        tp (true-positive actual predicted)
        fn (false-negative actual predicted)
        tn (true-negative actual predicted)]
    (let [accuracy (double (/ (+ tp tn) (+ tp tn fp fn)))
          precision (double (if (zero? (+ tp fp)) 0 (/ tp (+ tp fp))))
          recall (double (if (zero? (+ tp fn)) 0 (/ tp (+ tp fn))))
          f1 (double (* 2 (/ (* precision recall) (+ precision recall))))]
      (println "Accuracy:" accuracy)
      (println "Precision:" precision)
      (println "Recall:" recall)
      (println "F1:" f1))))