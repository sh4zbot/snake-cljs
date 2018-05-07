(ns snake-cljs.game
  (:require [clojure.string :as string]))

(defn create-game
  ([{:keys [row-num col-num width height]}]
   {:row-num   (or row-num 10)
    :col-num   (or col-num 10)
    :width     (or width 20)
    :height    (or height 20)
    :direction :right
    :snake     5                                            ;(rand-int (* width height))
    ;:board     (add-player (create-vector width height))
    })
  ([] (create-game {})))

(defn snake->xy [state]
  {:x (mod (:snake state) (:width state))
   :y (quot (:snake state) (:height state))})

(defn change-dir [state direction]
  (assoc state :direction direction))

(defn check-collisions [state]
  state)

(defn move-snake [state dir]
  (let [row-num (:row-num state)
        col-num (:col-num state)
        wxh (* row-num col-num)]
    (update state :snake (fn [pos]
                           (case dir
                             ;right
                             39 (+ (- pos (mod pos row-num))
                                   (mod (inc pos) row-num))
                             ;left
                             37 (+ (- pos (mod pos row-num))
                                   (mod (dec pos) row-num))
                             ;down
                             40 (mod (+ pos col-num) wxh)
                             ;up
                             38 (mod (- pos col-num) wxh)
                             pos)
                           ))))

(defn tick [state]
  (-> state
      (check-collisions)
      ;(move-snake)
      ))

;(defn create-vector [width height]
;  (into [] (for [elements (range (* width height))]
;             {:obj :empty
;              ;x y just for testing mod/quot
;              :x   (mod elements width)
;              :y   (quot elements height)})))

;(defn create-grid [state]
;  (let [x (range (:width state))
;        y (range (:height state))
;        {s-x :x s-y :y} (snake->xy state)]
;    (for [y y]
;      (for [x x]
;        {:obj (if (and (= s-x x)
;                       (= s-y y))
;                :snake
;                :empty)
;         :key (str "board" x y)}))))

(move-snake (create-game) :left)