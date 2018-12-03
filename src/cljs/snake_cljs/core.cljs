(ns snake-cljs.core
  (:require
    [reagent.core :as reagent]
    [snake-cljs.components :refer [app-component]]
    [snake-cljs.game :refer [create-game move-snake tick]]
    [cljs.core.async :refer [ timeout put! chan]]
    [goog.events :as events]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Css
;(defstyles board-grid [col-num row-num width height]
;           {:display "grid"
;            :grid-template-columns (grid-template col-num width)
;            :grid-template-rows (grid-template row-num height)
;            :background-color "black"})



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Vars

(defonce app-state
         (reagent/atom (create-game {:row-num 15
                                     :col-num 15
                                     :width 20
                                     :height 20})))

;(reset! app-state (create-game {:row-num 15 :col-num 15}))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Events

(defn keycode->direction [keycode]
  (case keycode
    39 :right
    37 :left
    48 :down
    38 :up))

(defn not-backwards [state dir]
  (case (:direction state)
    :left (not= dir :right)
    :right (not= dir :left)
    :up (not= dir :down)
    :down (not= dir :up)))

(defn key-down-event [e]
  (.preventDefault e)
  (let [key-code (.-keyCode e)
        direction (keycode->direction key-code)]
    ;(swap! app-state (fn [state]
    ;                   (move-snake state key-code)))
    (swap! app-state (fn [state]
                       ;TODO
                       (when (not-backwards state direction))
                       (assoc state :direction direction)))))


(defonce key-handler
         (events/listen js/window "keydown"
                        (fn [e] (key-down-event e))))

(defn handle-event [name data]
  (case name
    :assoc-key (swap! app-state (fn [state]
                                  (assoc state (:key data) (:value data))))
    nil))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(.setInterval js/window (fn [e]
                          (swap! app-state (fn [state]
                                             (tick state))))
              200)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Initialize App

(defn dev-setup []
  (when ^boolean js/goog.DEBUG
    (enable-console-print!)
    (println "dev mode")))

(defn reload []
  (reagent/render [app-component app-state handle-event]
                  (.getElementById js/document "app")))

(defn ^:export main []
  (dev-setup)
  (reload))
