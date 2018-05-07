(ns snake-cljs.css)

(defn grid-template [col-num width]
  (clojure.string/join
    (for [x (range col-num)]
      (str width "px "))))