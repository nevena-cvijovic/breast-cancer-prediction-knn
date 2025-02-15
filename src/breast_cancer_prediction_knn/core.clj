(ns breast-cancer-prediction-knn.core
  (:require
    [breast-cancer-prediction-knn.data-transformation :as load-data]
    [breast-cancer-prediction-knn.data-manipulation :as data-man]
    [breast-cancer-prediction-knn.data-normalization :as data-norm]
    [breast-cancer-prediction-knn.train-and-test-split :as tts]
    [breast-cancer-prediction-knn.knn-model :as knn]
    [breast-cancer-prediction-knn.evaluation-metrics :as eval-met]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; LOADING DATA and DATA TRANSFORMATION
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(def csv-file "src/breast_cancer_prediction_knn/Cancerdata.csv")

(def cancer-data (load-data/convert-dataset csv-file))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; PRINTING and PRESENTING DATA
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;(println cancer-data)
(doseq [row (take 10 cancer-data)]
  (println row))

(println "Dataset Information:")
(println (data-man/dataset-info cancer-data))

(println "\nColumn 'diagnosis':")
(println (data-man/get-column cancer-data "diagnosis"))

(println "\nLast 5 rows of the dataset:")
(println (data-man/last-rows cancer-data 5))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; DATA MANIPULATION
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(def cleaned-data (data-man/drop-column cancer-data "id"))

(println "Cleaned data (column 'id' dropped):")
(println cleaned-data)

(println "Cleaned data (column 'id' dropped) first 10 rows:")
(doseq [row (take 10 cleaned-data)]
  (println row))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; DIAGNOSIS CHANGE FROM M or B TO 1 or 0
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(def encoded-data (data-man/encode-diagnosis cleaned-data)) ;; Encodes "diagnosis" column

(println "Encoded Data:")
(doseq [row (take 10 encoded-data)]
  (println row))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; DATA NORMALIZATION
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def normalized-data (data-norm/normalize-data encoded-data))

(println "Normalized data:")
(doseq [row (take 10 normalized-data)]
  (println row))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; TRAIN AND TEST DATA
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def split-data (tts/train-test-split (rest normalized-data) 0.7))

(println "Train data:" (:train split-data))
(println "Test data: " (:test split-data))


(def transformed-train-data (tts/transform-data (:train split-data)))
(def transformed-test-data (tts/transform-data (:test split-data)))

(println transformed-train-data)

(println transformed-test-data)



(def transformed-test-data-without-class
  (tts/transform-data-without-class transformed-test-data))
(println transformed-test-data-without-class)



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;; KNN model and training
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def train-data transformed-train-data)

(println train-data)

(def test-data transformed-test-data-without-class)

(doseq [cancer-patient test-data]
  (let [k 3
        predicted-class (knn/knn train-data (:attributes cancer-patient) k)]
    (println "Cancer patient characteristics:")
    (println cancer-patient)
    (println "#################################")
    (println "Prediction:")
    (if (= predicted-class :M)
        (println "Cancer type is Malignant.")
        (println "Cancer type is Benign."))
    (println "#################################")))

(def actual-data (map :cancer-type transformed-test-data))

(println "Actual data:")
(println actual-data)

(def predicted-data
 (map #(knn/knn train-data (:attributes %) 3) test-data))

(println "Predicted data:")
(println predicted-data)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;; Evaluation of predicted and actual results
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def evaluation-metrics
  (eval-met/calculate-measures actual-data predicted-data))

(println "#################################")
(println "Confusion matrix")
(println "#################################")

(println (get evaluation-metrics :confusion-matrix) "\n")



(println "#################################")
(println "Evaluation metrics")
(println "#################################")
(println "Accuracy: " (get evaluation-metrics :accuracy)
         "\nPrecision: " (get evaluation-metrics :precision)
         "\nRecall: " (get evaluation-metrics :recall)
         "\nF1: " (get evaluation-metrics :f1))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; Measuring performances
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(use 'criterium.core)


(criterium.core/with-progress-reporting (criterium.core/quick-bench (data-norm/normalize-data encoded-data)))
;Before improving performances
;Evaluation count : 264 in 6 samples of 44 calls.
;Execution time mean : 2.584228 ms
;Execution time std-deviation : 236.601760 µs
;Execution time lower quantile : 2.350471 ms ( 2.5%)
;Execution time upper quantile : 2.850131 ms (97.5%)
;Overhead used : 5.869545 ns

;After improving performances
;Evaluation count : 606 in 6 samples of 101 calls.
;Execution time mean : 1.070664 ms
;Execution time std-deviation : 72.381274 µs
;Execution time lower quantile : 1.005936 ms ( 2.5%)
;Execution time upper quantile : 1.181637 ms (97.5%)
;Overhead used : 6.080843 ns

(criterium.core/with-progress-reporting (criterium.core/quick-bench (tts/train-test-split (rest normalized-data) 0.7)))
;Evaluation count : 11136 in 6 samples of 1856 calls.
;Execution time mean : 52.068242 µs
;Execution time std-deviation : 1.125579 µs
;Execution time lower quantile : 50.281900 µs ( 2.5%)
;Execution time upper quantile : 53.291787 µs (97.5%)
;Overhead used : 5.869545 ns

(criterium.core/with-progress-reporting (criterium.core/quick-bench (map #(knn/knn train-data (:attributes %) 3) test-data)))
;Evaluation count : 49070064 in 6 samples of 8178344 calls.
;Execution time mean : 7.861978 ns
;Execution time std-deviation : 1.228778 ns
;Execution time lower quantile : 6.530154 ns ( 2.5%)
;Execution time upper quantile : 9.035747 ns (97.5%)
;Overhead used : 5.869545 ns


(criterium.core/with-progress-reporting (criterium.core/quick-bench (eval-met/calculate-measures actual-data predicted-data)))
;Before improving performances
;Evaluation count : 5532 in 6 samples of 922 calls.
;Execution time mean : 116.319294 µs
;Execution time std-deviation : 11.308627 µs
;Execution time lower quantile : 103.671029 µs ( 2.5%)
;Execution time upper quantile : 128.126995 µs (97.5%)
;Overhead used : 5.869545 ns

;After improving performances
;Evaluation count : 21804 in 6 samples of 3634 calls.
;Execution time mean : 28.734103 µs
;Execution time std-deviation : 977.740527 ns
;Execution time lower quantile : 27.663446 µs ( 2.5%)
;Execution time upper quantile : 30.075530 µs (97.5%)
;Overhead used : 6.202174 ns









