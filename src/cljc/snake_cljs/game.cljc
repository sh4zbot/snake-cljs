(ns snake-cljs.game
  (:require [clojure.string :as string]
    ;[clojure.core.async :as a :refer  [>! <! >!! <!! go chan buffer close! thread
    ;                                   alts! alts!! timeout]]
            ))

(defn create-game
  ([{:keys [row-num col-num width height]}]
   {:row-num   (or row-num 10)
    :col-num   (or col-num 10)
    :width     (or width 20)
    :height    (or height 20)
    :direction :right
    :snake     '(5)
    :apple     22
    ;:board     (add-player (create-vector width height))
    })
  ([] (create-game {})))


;(defn snake->xy [state]
;  {:x (mod (:snake state) (:width state))
;   :y (quot (:snake state) (:height state))})

(defn change-dir [state direction]
  (assoc state :direction direction))

(defn move-snake [state dir]
  (let [row-num (:row-num state)
        col-num (:col-num state)
        wxh (* row-num col-num)
        dir (or dir (:direction state))]
    (update state :snake (fn [snake-seq]
                           (let [pos (first snake-seq)]
                             (cons (case dir
                                     (:right 39) (+ (- pos (mod pos row-num))
                                                    (mod (inc pos) row-num))
                                     (:left 37) (+ (- pos (mod pos row-num))
                                                   (mod (dec pos) row-num))
                                     (:down 40) (mod (+ pos col-num) wxh)
                                     (:up 38) (mod (- pos col-num) wxh)
                                     pos)
                                  (drop-last snake-seq)))))))

(defn check-collisions [state]
  (if (= (first (:snake state)) (:apple state))
    (let [snek-tail (last (:snake state))]
      (-> state
          (assoc :apple (rand-int (* (:col-num state) (:row-num state))))
          (move-snake nil)
          (update :snake (fn [snake-seq]
                           (conj snake-seq snek-tail)))))
    state))

(defn tick [state]
  (-> state
      (check-collisions)
      (move-snake nil)))

(defn paint
  {:test (fn []
           (paint 24 '(23 25) 24)
           )}
  [pos snek-seq apple]
  (or (reduce (fn [acc val]
                (if (= val pos)
                  (reduced :snake)))
              snek-seq)
      (if (= pos apple)
        :apple)))



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

