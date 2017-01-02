(ns clj-brave-and-true.ch06.the-divine-cheese-code.visualization.svg
  (:require [clojure.string :as s])
  (:refer-clojure :exclude [min max]))

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

(defn line
  [points]
  (str "<polyline points=\"" points "\" />"))


(defn comparator-over-maps
  [comparison-fn ks]
  (fn
    [maps]
    (zipmap
      ks
      (map
        (fn
          [k] ;; k is a Seq here, I think
          (apply
            comparison-fn
            ;; this map is leveraging that a keyword is a function, I think
            (map k maps)))
        ks))))

(def min (comparator-over-maps clojure.core/min [:lat :lng]))

(def max (comparator-over-maps clojure.core/max [:lat :lng]))

(defn translate-to-00 [locations]
  (let [mincoords (min locations)]
    (map #(merge-with - % mincoords) locations)))

(defn scale
  [width height locations]
  (let [maxcoords (max locations)
        ratio {:lat (/ height (:lat maxcoords))
               :lng (/ width (:lng maxcoords))}]
    (map #(merge-with * % ratio) locations)))

(defn transform
  "Just chains other functions"
  [width height locations]
  (->> locations
       translate-to-00
       (scale width height)))

(defn xml
  "svg 'template', which also flips the coordinate system"
  [width height locations]
  (str "<svg height=\"" height "\" width=\"" width "\">"
       ;; These two <g> tags flip the coordinate system
       "<g transform=\"translate(0," height ")\">"
       "<g transform=\"scale(1,-1)\">"
       (-> (transform width height locations)
           points
           line)
       "</g></g>"
       "</svg>"))
