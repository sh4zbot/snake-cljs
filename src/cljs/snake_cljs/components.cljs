(ns snake-cljs.components
  (:require [cljss.reagent :refer-macros [defstyled]]
            [cljss.core :refer [defstyles]]
            [snake-cljs.css :refer [grid-template]]
            [snake-cljs.game :refer [paint]]))

(defstyled Board :div
           {:display               "grid"
            :grid-template-columns :columns
            :grid-template-rows    :rows
            ;:background-color "blue"
            })


(defstyled Square :div
           {:grid-column-start "auto"
            :width             :width
            :height            :height
            :background-color  (with-meta #(if % "red" "black") :snake?)
            :key               4})


(defstyles square-style [obj width height]
           {:grid-column-start "auto"
            :width             width
            :height            height
            :background-color  (if obj
                                 (case obj
                                   :snake "yellow"
                                   :apple "red")
                                 "black")
            :key               4})

(defn square [obj width height]
  [:div {:class (square-style obj width height)}])

(defn update-key-value [ratom trigger-event f key text]
  [:button
   {:on-click (fn [e]
                (trigger-event :assoc-key {:key   :col-num
                                           :value (f (key @ratom))}))}
   text])

(defn ui-component [ratom trigger-event]
  [:div
   (update-key-value ratom trigger-event inc :col-num "Add Column")
   (update-key-value ratom trigger-event dec :col-num "Remove Column")])

(defn game-component [ratom trigger-event]
  (let [state @ratom
        row-num (:row-num state)
        col-num (:col-num state)
        width (:width state)
        height (:height state)
        snake-seq (:snake state)
        apple (:apple state)]
    (Board {:columns (grid-template col-num width)
            :rows    (grid-template row-num height)}
           (for [x (range (* row-num col-num))]
             ^{:key x}
             [square (if (reduce (fn [a v]
                                   (if (= x v)
                                     (reduced true)))
                                 false
                                 snake-seq)
                       :snake
                       (if (= x apple)
                         :apple)) width height]))))


;(fn [snek? pos]
;                                         (if (= x pos)
;                                           (reduced :snake)
;                                           nil))

(defn app-component [ratom trigger-event]
  [:div
   (ui-component ratom trigger-event)
   (game-component ratom trigger-event)])
