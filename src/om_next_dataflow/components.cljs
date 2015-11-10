(ns om-next-dataflow.components
  (:require [cljs.pprint :refer [pprint]]
            [om.next :as om :refer-macros [defui]]
            [sablono.core :refer-macros [html]]))

(defn guest [character {:keys [text]}]
  (html [:.person [:h3 character ":"] [:blockquote text]]))

(defui Harry
  static om/IQuery
  (query [this]
    '[[:dish/number 3]])
  Object
  (render [this]
    (guest "Harry" (om/props this))))

(def harry (om/factory Harry))

(defui Sally
  static om/IQuery
  (query [this]
    '[([:dish/name "Chef Salad"] {:aside [:oil :vinegar]})
      [:dish/name "Vanilla"]])
  Object
  (render [this]
    (guest "Sally" (om/props this))))

(def sally (om/factory Sally))

(defui Diner
  static om/IQuery
  (query [this]
    (into [] (concat (om/get-query Harry) (om/get-query Sally))))
  Object
  (render [this]
    (html [:#diner

           [:h2 "Hi, what can I get ya?"]
           (harry {:text "I'll have a number three."})
           (sally {:text "I'd like the chef salad please with the oil and
                           vinegar on the side and the vanilla."})

           [:h2 "Your order:"]
           [:pre (with-out-str (pprint (om/props this)))]])))
