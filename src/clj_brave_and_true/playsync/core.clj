(ns clj-brave-and-true.playsync.core
  (:require [clojure.core.async :as a :refer [>! <! >!! <!! go chan buffer close! thread alts! alts!! timeout]]))

; Create a channel
(def echo-chan (chan))

; everything within the go block runs concurrently on a separate thread
; I'm pretty sure that this thing started, then immediately blocked waiting
; for something to arrive from the channel.
(go (println (<! echo-chan)))

; this put something on the channel
(>!! echo-chan "ketchup")

; this creates a buffered channel with 2 slots
; you can put 2 messages in it without blocking.  If you put in a 3rd
; message, you will block until some process starts taking
(def echo-buffer (chan 2))

