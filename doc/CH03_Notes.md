# CH03:  DO THINGS: A CLOJURE CRASH COURSE

http://www.braveclojure.com/do-things/

## 2016-11-04, BobK, Functions

`(doc xxx)` is useful in a REPL to get the docs (p 51).  Another really useful resource is
https://clojuredocs.org/clojure.core/apply which is the docstring along with examples!

`(source xxx)` is useful too if you want to see the source of a function.

Also "Clojure Cheetsheet" http://clojure.org/api/cheatsheet for figuring out all the strange literal
things.

### Leading Questions

* Then a clojure guy tells you to map over a collection, what's he saying? (p 49)
* Higher Order Functions. (p 49)
  * What are they?
  * Are they really unique to Lisp(s)?
    * What other languages have them?
    * Is Java's [java.util.Collections.sort(List list, Comparator c)](https://docs.oracle.com/javase/7/docs/api/java/util/Collections.html#sort(java.util.List,%20java.util.Comparator)) the moral equivalent of higher order functions in Java?
* What makes Macro Calls and Special Forms "special"?  (p 50)
* What are the 3 different ways to define a function? (p 51, p 56)
  * Defining a function.  Def a Fun.  Defn...p 51
  * The other 2 are called "anonymous" on p 56
* Destructuring.  Do you get it? (p54)
  * My "aha" moment is encapsulated in his quote "...bind names to values within a collection."
* The "hash literals" are a little out of hand by p 62, no?
  * What is `#{`?  Does an example help? `#{:a :b}`
  * What is `#(`?  Does an example help? `#(* % %)`
  * What is `#"`?  Does an example help? `#"^left-"`
