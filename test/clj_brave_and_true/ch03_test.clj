(ns clj-brave-and-true.ch03-test
  (:require [clojure.test :refer :all]
            [clj-brave-and-true.ch03 :refer :all]))

(deftest dec-maker-test
  (testing "dec-maker"
    (let [dec9 (dec-maker 9)]
      (is (= 1 (dec9 10)))
      (is (= 3 ((dec-maker 7) 10))))))

(deftest mapset-test
  (testing "mapset"
    (is (= #{2 3} (mapset inc [1 1 2 2])))))