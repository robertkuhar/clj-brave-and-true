(ns clj-brave-and-true.ch03
  (:require [clojure.tools.logging :as log]))

(defn announce-treasure-location
  ([lat lon & rest]
   ;; todo There has got to be a more clever way to format this string
   (print "Treasure lat:" lat "lon:" lon)
   (when (seq rest)
     (print " rest" rest))
   (print "\n"))
  ([{:keys [lat lon] :as full-coordinates}]
   ;; todo maybe I should remove lat and lon from the full-coordinates map
   (announce-treasure-location lat lon full-coordinates)))

;Good: (ch03/announce-treasure-location 1 2 100)
;Good: (ch03/announce-treasure-location {:lat 3 :lon 4 :elev 100})
;Hmmm: (ch03/announce-treasure-location 1 2 {:lat 10 :lon 20 :elev 100})
