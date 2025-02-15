(defproject breast-cancer-prediction-knn "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/data.csv "1.1.0"]
                 [midje "1.10.9"]
                 [criterium "0.4.6"]
                 [net.mikera/core.matrix "0.62.0"]]
  :plugins [[lein-midje "3.2.1"]]
  :repositories [["clojars" "https://clojars.org/repo/"]
                 ["central" "https://repo1.maven.org/maven2/"]]
  :repl-options {:init-ns breast-cancer-prediction-knn.core})
