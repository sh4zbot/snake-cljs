(ns snake-cljs.first-card
  (:require-macros [devcards.core :refer [defcard-doc
                                          defcard-rg
                                          mkdn-pprint-source]])
  (:require [devcards.core]
            [reagent.core :as reagent]
            [snake-cljs.core :refer [app-state app-component]]))



;(defcard-rg page
;            [page app-state]
;            app-state
;            {:inspect-data true})

(defcard-rg app-component
            [app-component app-state]
            app-state
            {:inspect-data true})

;(defcard-rg board-component [board-component app-state]
;            (board-component app-state)
;            {:inspect-data false})
