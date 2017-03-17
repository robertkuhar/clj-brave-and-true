(defproject clj-brave-and-true "0.1.0-SNAPSHOT"
  :description "Clojure for the Brave and True"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.3.441"]
                 [org.clojure/tools.logging "0.3.1"]
                 [pegthing/pegthing "0.1.0-SNAPSHOT"]]

  :target-path "target/%s"

  :main nil

  :profiles {:core {:main clj-brave-and-true.core}
             :the-devine-cheese {:main clj-brave-and-true.ch06.the-divine-cheese-code.core}
             :uberjar {:aot :all}})
