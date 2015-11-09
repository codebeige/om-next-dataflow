(ns om-next-dataflow.core
  (:require cljsjs.react.dom
            [om.next :as om :refer-macros [defui]]
            [sablono.core :as html :refer-macros [html]]))

(defui Diner
  Object
  (render [this]
          (html [:#om-next-diner [:h2 "Yes, we are open!"]])))

(def diner (om/factory Diner))

(defn main []
  (.render js/ReactDOM (diner) (.getElementById js/document "app")))

(defonce app (main))
