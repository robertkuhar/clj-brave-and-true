(ns clj-brave-and-true.ch10)

(def fred (atom {:cuddle-hunger-level 0 :percent-deteriorated 0}))

(defn increase-cuddle-hunger-level
  [zombie-state increase-by]
  (merge-with + zombie-state {:cuddle-hunger-level increase-by}))

(defn shuffle-speed
  [zombie]
  (* (:cuddle-hunger-level zombie)
     (- 100 (:percent-deteriorated zombie))))

(defn shuffle-alert
  "Watch functions take four arguments
  key - a value
  watched - the atom being watched
  old-state - the state of the atom before its update
  new-state - the state of the atom after its update."
  [key watched old-state new-state]
  (let [sph (shuffle-speed new-state)]
    (if (> sph 5000)
      (do
        (println "Run, you fool!")
        (println "The zombie's SPH is now " sph)
        (println "This message brought to your courtesy of " key))
      (do
        (println "All's well with " key)
        (println "Cuddle hunger: " (:cuddle-hunger-level new-state))
        (println "Percent deteriorated: " (:percent-deteriorated new-state))
        (println "SPH: " sph)))))

(defn percent-deteriorated-validator
  [{:keys [percent-deteriorated]}]
  (and (>= percent-deteriorated 0)
       (<= percent-deteriorated 100)))

(def bobby (atom {:cuddle-hunger-level 0 :percent-deteriorated 0}
                 :validator percent-deteriorated-validator))

(def sock-varieties
  #{"darned" "argyle" "wool" "horsehair" "mulleted"
    "passive-aggressive" "striped" "polka-dotted"
    "athletic" "business" "power" "invisible" "gollumed"})

(defn sock-count
  [sock-variety count]
  {:variety sock-variety
   :count   count})

(defn generate-sock-gnome
  "Create an initial sock gnome state with no socks"
  [name]
  {:name  name
   :socks #{}})

(defn steal-sock
  [gnome dryer]
  (dosync
    ; asks the dryer...give me something that has 2?
    ; todo: consider making this some mod 2 == 0 thing.  Side effects in updated-count?
    (when-let [pair (some #(if (= (:count %) 2) %) (:socks @dryer))]
      (let [updated-count (sock-count (:variety pair) 1)]
        (alter gnome update-in [:socks] conj updated-count)
        (alter dryer update-in [:socks] disj pair)
        (alter dryer update-in [:socks] conj updated-count)))))

(defn similar-socks
  [target-sock sock-set]
  (filter #(= (:variety %) (:variety target-sock)) sock-set))


