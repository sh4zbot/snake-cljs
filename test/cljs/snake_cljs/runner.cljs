(ns snake-cljs.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [snake-cljs.core-test]))

(doo-tests 'snake-cljs.core-test)
