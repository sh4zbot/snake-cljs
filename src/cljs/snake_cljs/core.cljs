(ns snake-cljs.core
  (:require
    [reagent.core :as reagent]
    [snake-cljs.game :refer [create-game
                             move-snake]]
    [snake-cljs.css :refer [grid-template]]
    [cljs.core.async :refer [timeout put! chan]]
    [cljss.core :refer [defstyles]]
    [cljss.reagent :refer-macros [defstyled]]
    [goog.events :as events]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Css
;(defstyles board-grid [col-num row-num width height]
;           {:display "grid"
;            :grid-template-columns (grid-template col-num width)
;            :grid-template-rows (grid-template row-num height)
;            :background-color "black"})

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
            :background-color  (with-meta #(if % "red" "black") :snake?)})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Vars

(defonce app-state
         (reagent/atom {}))

(reset! app-state (create-game))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Events

(defonce key-handler
         (events/listen js/window "keydown"
                        (fn [e]
                          (.preventDefault e)
                          (let [key-code (.-keyCode e)]
                            (swap! app-state (fn [state]
                                               (move-snake state key-code)))))))

(defn handle-event [name data]
  (case name
    :assoc-key (swap! app-state (fn [state]
                                  (assoc state (:key data) (:value data))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Page

(defn assoc-key [ratom f key text]
  [:button {:on-click (fn [e]
                        (handle-event :assoc-key {:key   :col-num
                                                  :value (f (key @ratom))}))}
   text])

(defn ui [ratom]
  [:div
   (assoc-key ratom inc :col-num "Add Column")
   (assoc-key ratom dec :col-num "Remove Column")])



(defn game [ratom]
  (let [state @ratom
        row-num (:row-num state)
        col-num (:col-num state)
        width (:width state)
        height (:height state)
        snake (:snake state)]
    (Board {:columns (grid-template col-num width)
            :rows    (grid-template row-num height)}
           (for [x (range (* row-num col-num))]
             (Square {:width  width
                                :height height
                                :snake? (= x snake)
                                :key x})))))

(defn app-component [ratom]
  [:div
   (ui ratom)
   (game ratom)])


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Initialize App

(defn dev-setup []
  (when ^boolean js/goog.DEBUG
    (enable-console-print!)
    (println "dev mode")))

(defn reload []
  (reagent/render [app-component app-state]
                  (.getElementById js/document "app")))

(defn ^:export main []
  (dev-setup)
  (reload))
