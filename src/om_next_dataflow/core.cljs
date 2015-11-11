(ns om-next-dataflow.core
  (:refer-clojure :exclude [name key])
  (:require cljsjs.react.dom
            [om.next :as om]
            [om-next-dataflow.components :refer [Diner]]
            ;; [om-next-dataflow.parsers.atom :refer [create-menu serve]]
            [om-next-dataflow.parsers.datascript :refer [create-menu serve]]))

(enable-console-print!)

(defonce menu (create-menu))

(def waitress (om/parser {:read serve}))

(defn main []
  (om/add-root! (om/reconciler {:state menu :parser waitress})
                Diner
                (.getElementById js/document "app")))

(main)
