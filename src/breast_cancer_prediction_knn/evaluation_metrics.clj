(ns breast-cancer-prediction-knn.evaluation-metrics)


(defn build-confusion-matrix
  "Builds a confusion matrix from actual and predicted values.
   The confusion matrix is represented as a map with keys :tp, :tn, :fp, :fn."
  [actual-values predicted-values]
  (reduce (fn [confusion [actual predicted]]
            (cond
              (and (= actual :B) (= predicted :B)) (update confusion :tp inc) ; True Positive
              (and (= actual :M) (= predicted :M)) (update confusion :tn inc) ; True Negative
              (and (= actual :M) (= predicted :B)) (update confusion :fp inc) ; False Positive
              (and (= actual :B) (= predicted :M)) (update confusion :fn inc) ; False Negative
              :else confusion))
          {:tp 0 :tn 0 :fp 0 :fn 0}
          (map vector actual-values predicted-values)))

(defn calculate-measures
  "Calculates accuracy, precision, recall, and F1 score based on the confusion matrix."
  [actual predicted]
  (let [{:keys [tp tn fp fn]} (build-confusion-matrix actual predicted)
        accuracy (double (/ (+ tp tn) (+ tp tn fp fn)))
        precision (double (if (zero? (+ tp fp)) 0 (/ tp (+ tp fp))))
        recall (double (if (zero? (+ tp fn)) 0 (/ tp (+ tp fn))))
        f1 (double (if (zero? (+ precision recall)) 0 (* 2 (/ (* precision recall) (+ precision recall)))))]
    {:accuracy accuracy
     :precision precision
     :recall recall
     :f1 f1
     :confusion-matrix {:true-positive tp
                        :true-negative tn
                        :false-positive fp
                        :false-negative fn}}))