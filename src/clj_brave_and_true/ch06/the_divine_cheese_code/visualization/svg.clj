(ns clj-brave-and-true.ch06.the-divine-cheese-code.visualization.svg)

(defn latlng->point
  "Convert lat/lng map to comma-separated string"
  [latlng]
  (str (:lng latlng) "," (:lat latlng)))

(defn points
  "BobK:  This is what drives me crazy about clojure.  What are the
  expectations of the locations input parameter?  How is a reader
  supposed to know the input locations is a Sequence of Maps?"
  [locations]
  (clojure.string/join " " (map latlng->point locations)))
