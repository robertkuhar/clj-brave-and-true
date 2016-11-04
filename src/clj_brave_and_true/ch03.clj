(ns clj-brave-and-true.ch03)

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

(def asym-hobbit-body-parts [{:name "head" :size 3}
                             {:name "left-eye" :size 1}
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}])

(defn matching-part
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})

(defn symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (loop [remaining-asym-parts asym-body-parts
         final-body-parts []]
    (print "remaining-asym-parts:" remaining-asym-parts "\n")
    (print "final-body-parts:" final-body-parts "\n")
    (if (empty? remaining-asym-parts)
      final-body-parts
      (let [[part & remaining] remaining-asym-parts]
        ;; mind-blown...(set [part (matching-part part)]) gets evaluated
        ;; before the recurs invokes that "anonymous function set up by
        ;; loop.  The bindings loop captures do look alooooot like an arg list
        (recur remaining
               (into final-body-parts
                     (set [part (matching-part part)])))))))
