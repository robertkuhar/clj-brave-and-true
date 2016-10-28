(defproject clj-brave-and-true "0.1.0-SNAPSHOT"
  :description "Clojure for the Brave and True"

  :dependencies [[org.clojure/clojure "1.8.0"]]

  :main ^:skip-aot clj-brave-and-true.core

  :target-path "target/%s"

  :profiles {:uberjar {:aot :all}})
