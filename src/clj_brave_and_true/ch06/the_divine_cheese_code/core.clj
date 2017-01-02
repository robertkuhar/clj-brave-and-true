(ns clj-brave-and-true.ch06.the-divine-cheese-code.core
  (:require
    [clj-brave-and-true.ch06.the-divine-cheese-code.visualization.svg :refer [xml latlng->point points]]
    [clojure.java.browse :as browse]))
;; BobK usually sees this done in the namespace's (:require...) references, what?, directive?
;; Ensure that the SVG code is evaluated
;; (require 'clj-brave-and-true.ch06.the-divine-cheese-code.visualization.svg)
;; (refer 'clj-brave-and-true.ch06.the-divine-cheese-code.visualization.svg)

(def heists [{:location "Cologne, Germany"
              :cheese-name "Archbishop Hildebold's Cheese Pretzel"
              :lat 50.95
              :lng 6.97}
             {:location "Zurich, Switzerland"
              :cheese-name "The Standard Emmental"
              :lat 47.37
              :lng 8.55}
             {:location "Marseille, France"
              :cheese-name "Le Fromage de Cosquer"
              :lat 43.30
              :lng 5.37}
             {:location "Zurich, Switzerland"
              :cheese-name "The Lesser Emmental"
              :lat 47.37
              :lng 8.55}
             {:location "Vatican City"
              :cheese-name "The Cheese of Turin"
              :lat 41.90
              :lng 12.45}])

(defn what-does-that-refer-get-me []
  (let [lat-lng {:lat 1 :lng 2}
        point (latlng->point lat-lng)
        this-is-what-it-does (format "(latlng->point %s) => %s" lat-lng point)]
    (println this-is-what-it-does)
    this-is-what-it-does))

(defn url
  [filename]
  (str "file:///"
       (System/getProperty "user.dir")
       "/"
       filename))

(defn template
  [contents]
  (str "<style>polyline { fill:none; stroke:#5881d8; stroke-width:3}</style>"
       contents))

(defn exit
  "allows for an exit from lein run"
  [status msg]
  (println msg)
  (System/exit status))

(defn -main
  [& args]
  (println (points heists))
  (let [filename "target/map.html"]
    (->> heists
         (xml 50 100)
         template
         (spit filename))
    (browse/browse-url (url filename)))
  (exit 0 ""))

