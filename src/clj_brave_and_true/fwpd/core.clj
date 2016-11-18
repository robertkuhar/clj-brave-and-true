(ns clj-brave-and-true.fwpd.core)

;; CH04

(def filename "ch04/suspects.csv")

(def vamp-keys [:name :glitter-index])

(defn str->int [str]
  (Integer. str))

(def conversions {:name identity :glitter-index str->int})

(defn convert [vamp-key value]
  ((get conversions vamp-key) value))

(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))

(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}
  from an input seq of vectors [\"Edward Cullen\" \"10\"]"
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows))

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

(defn ex1 []
  (map
    :name
    (glitter-filter
      3
      (mapify
        (parse
          (slurp
            (clojure.java.io/resource "ch04/suspects.csv")))))))

(defn ex3-validate
  [map-of-keywords record]
  (and (:name record) (:glitter-index record)))

(defn ex2-append
  [coll x]
  (conj coll x))

(defn ex4
  [suspects]
  (->> suspects
       (map (juxt :name :glitter-index))
       (map (fn [x] (clojure.string/join "," x)))
       (clojure.string/join "\n")))
