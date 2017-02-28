(ns clj-brave-and-true.ch09)

;; Notes from Esma, Fri, Feb 24, Ch09: The Sacred Art of Concurrent and Parallel Programming (18 pages)

;; FUTURES ;;;;;;;;;;;;;;;;;
; Futures examples
(future (Thread/sleep 4000)
        (println "I'll print after 4 seconds"))

(println "I'll print immediately")

; Dereferencing that future
(let [result (future (println "this prints once")
                     (+ 1 1))]
  (println "deref: " (deref result))
  (println "@: " @result))

; Dereferencing blocked by future still running
(let [result (future (Thread/sleep 3000)
                     (+ 1 1))]
  (println "The result is: " @result)
  (println "It will be at least 3 seconds before I print"))

; Dereferencing with a time limit to aoid being blocked
; tells deref to return the value 5 if the future doesn’t
; return a value within 10 milliseconds
(deref (future (Thread/sleep 1000) 0) 10 5)

; Interrogate the future
(realized? (future (Thread/sleep 1000)))
; => false

(let [f (future)]
  @f
  (realized? f))

;; DELAYS ;;;;;;;;;;;;;;;;;;;;;
; ex:
(def jackson-5-delay
  (delay (let [message "Just call my name and I'll be there"]
           (println "First deref:" message)
           message)))

; then, force evaluation
(force jackson-5-delay)

;; so future us a macro and so is delay,
;; so another way to do this would be?

(def jackson-5-delay-other
  (let [message "Just call my name and I'll be there"]
    (println "First deref:" message)
    message))

; doing it this way gives you back a delay obj with status info?
(delay (jackson-5-delay-other))

; this isn't actually forcing the delay to evaluate- we'd have to capture that
; delay obj we get from (delay (jackson-5-delay-other)) and force THAT
(force jackson-5-delay)

;; capture delay obj
(def delay-captured (delay (jackson-5-delay-other)))
delay-captured

; other fun things we can do :
(delay? delay-captured)

; note that now this code looks a lot more like the book's example but
; is just more verbose
(force delay-captured)
; ClassCastException java.lang.String cannot be cast to clojure.lang.IFn
; user/fn--1706 (form-init4572997958549428883.clj:1)

; wait - what? So Delay always expect a fn returned from what we are delaying
; Delay uses Java's Delay deref(), which needs this to be a fn
(def jackson-5-delay-other-other
  (let [message "Just call my name and I'll be there"]
    (println "First deref:" message)
    (fn [] message)))
(def delay-captured (delay (jackson-5-delay-other-other)))
delay-captured

(force delay-captured) ; Ah!!
; so why does wrapping delay around the let work then??
(type (let [x "what type is let?"]
        (println "blah")
        "foo"))


;; PROMISES ;;;;;;;;;;;;;;;;;;;;;;;;;
(def my-promise (promise))
(deliver my-promise (+ 1 2))
@my-promise


;; more examples
(def yak-butter-international
  {:store "Yak Butter International"
   :price 90
   :smoothness 90})
(def butter-than-nothing
  {:store "Butter Than Nothing"
   :price 150
   :smoothness 83})
;; This is the butter that meets our requirements
(def baby-got-yak
  {:store "Baby Got Yak"
   :price 94
   :smoothness 99})

(defn mock-api-call
  [result]
  (Thread/sleep 1000)
  result)

(defn satisfactory?
  "If the butter meets our criteria, return the butter, else return false"
  [butter]
  (and (<= (:price butter) 100)
       (>= (:smoothness butter) 97)
       butter))

; no promises and futures
(time (some (comp satisfactory? mock-api-call)
            [yak-butter-international butter-than-nothing baby-got-yak]))

; life with promise and a future
(time
  (let [butter-promise (promise)]
    (doseq [butter [yak-butter-international butter-than-nothing baby-got-yak]]
      (future (if-let [satisfactory-butter (satisfactory? (mock-api-call butter))]
                (deliver butter-promise satisfactory-butter))))
    (println "And the winner is:" @butter-promise)))

; if none of the yak butter is satisfactory the dereference would block forever
; and tie up the thread. To avoid that, you can include a timeout


; but what happens if we getmore than one promise delivered?
(def some-promise (promise))
(deliver some-promise "I told you so")
(deliver some-promise "I never saw it coming")
(deref some-promise 10 "timedout")
; that wasn't really expected..

; Also, is it just good practice then to always use deref and specify timeout
; just as you would with any sane api call?


;; THE QUEUE ;;;;;;

(defmacro wait
  "Sleep `timeout` seconds before evaluating body"
  [timeout & body]
  `(do (Thread/sleep ~timeout) ~@body))

(let [saying3 (promise)]
  (future (deliver saying3 (wait 100 "Cheerio!")))
  @(let [saying2 (promise)]
     (future (deliver saying2 (wait 400 "Pip pip!")))
     @(let [saying1 (promise)]
        (future (deliver saying1 (wait 200 "'Ello, gov'na!")))
        (println @saying1)
        saying1)
     (println @saying2)
     saying2)
  (println @saying3)
  saying3)


;; or better yet:
(defmacro enqueue
  ([q concurrent-promise-name concurrent serialized]
   `(let [~concurrent-promise-name (promise)]
      (future (deliver ~concurrent-promise-name ~concurrent))
      (deref ~q)
      ~serialized
      ~concurrent-promise-name))
  ([concurrent-promise-name concurrent serialized]
   `(enqueue (future) ~concurrent-promise-name ~concurrent ~serialized)))

(-> (enqueue saying (wait 200 "'Ello, gov'na!") (println @saying))
    (enqueue saying (wait 400 "Pip pip!") (println @saying))
    (enqueue saying (wait 100 "Cheerio!") (println @saying)))



;;; REAL WORLD ;;;;;;;;;;;;;;;;
; this is loosely based on a talk on parallelism from Leon Barrett https://www.youtube.com/watch?v=BzKjIk0vgzE

(def sample-data-points [1.0 3.4 5.2 1.2 -2 0.3 -0.8])
; A fn that maps a fn across a sequence and gets an average

(defn compute
  [data-points]
  (->> (map  (fn [x] (+ x 2.4)) data-points)
       (reduce +)
       (* (/ (count data-points)))))

; A fn that utilizes future for work what can not be parallized
; As Leon puts it, we will do this in the background
(defn compute
  [data-points]
  (let [scale (future (/ (count data-points)))]
    (->> (map  (fn [x] (+ x 2.4)) data-points)
         (reduce +)
         (* @scale))))

;; clojure has only one threadpool, all futures run in this threadpool
;; pool has "unlimited" threads, but technically N threads (max integer)
;; idle thread lifetime of 60 secs

;; limitations for future
; don't use future for small tasks when cost is less than overhead
;; every time you do, you touch the thread pool and it'll later with deref
; be accessing the value (at least a few fn calls, 10s of ms)

;; exceptions down work normally in future
;; you'll want to control/orhestrate the concurrent threads yourself
