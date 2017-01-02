(ns clj-brave-and-true.ch06.the-divine-cheese-code.core)
;; Ensure that the SVG code is evaluated
(require 'clj-brave-and-true.ch06.the-divine-cheese-code.visualization.svg)
(refer 'clj-brave-and-true.ch06.the-divine-cheese-code.visualization.svg)

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

(defn -main
  [& args]
  (println (points heists)))