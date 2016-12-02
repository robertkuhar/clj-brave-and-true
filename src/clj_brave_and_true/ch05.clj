(ns clj-brave-and-true.ch05
  (:require [clojure.string :as s]))

(defn sum-v1
  ([vals] (sum-v1 vals 0))
  ([vals accumulating-total]
   (if (empty? vals)
     accumulating-total
     (sum-v1 (rest vals) (+ (first vals) accumulating-total)))))

(defn sum-v2
  ([vals] (sum-v2 vals 0))
  ([vals accumulating-total]
   (if (empty? vals)
     accumulating-total
     (recur (rest vals) (+ (first vals) accumulating-total)))))

(defn cbat-clean
  [text]
  (s/replace (s/trim text) #"lol" "LOL"))

(defn matts-clean
  "Thread First macro"
  [text]
  (-> text
      (s/trim)
      (s/replace #"lol" "LOL"))
  ;; BobK: I now know why I struggle with this, but I may sound like an idiot.
  ;; It feels wrong to explicity call a function with the wrong arity like I just
  ;; called (s/trim) when (doc s/trim) shows only a single argument arity.  Should
  ;; be an error, no?...but the threading macro hides that
  )

(def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength 4
                :dexterity 5}})

(def c-int (comp :intelligence :attributes))
(def c-str (comp :strength :attributes))
(def c-dex (comp :dexterity :attributes))

(defn spell-slots
  [char]
  (int (inc (/ (c-int char) 2))))

(def spell-slots-comp
  (comp int inc #(/ % 2) c-int))

(defn spell-slots-matt
  [char]
  (-> char
      (c-int)
      (#(/ % 2))
      (inc)
      (int)))