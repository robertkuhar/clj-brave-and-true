(ns clj-brave-and-true.ch07)

(defmacro ignore-last-operand
  [function-call]
  (butlast function-call))

(defmacro infix
  [infixed]
  (list (second infixed)
        (first infixed)
        (last infixed)))
