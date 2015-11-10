(ns om-next-dataflow.core
  (:refer-clojure :exclude [name key])
  (:require cljsjs.react.dom
            [cljs.pprint :refer [pprint]]
            [om.next :as om :refer-macros [defui]]
            [sablono.core :refer-macros [html]]))

(enable-console-print!)

(def menu (atom {:dishes {1 {:name "Chef Salad"
                             :components
                             [:eggs :turkey :cheese :lettuce :oil :vinegar]}

                          2 {:name "Pie a la mode"
                             :aliases ["Apple a la mode" "Vanilla"]
                             :components
                             [:apple-pie :vanilla-ice-cream]}

                          3 {:name "Meatloaf Sandwich"
                             :components
                             [:meatloaf :lettuce :tomato :mayo :sourdough]}

                          4 {:name "Strawberry Sundae"
                             :components
                             [:strawberry-ice-cream :wafer :whipped-cream]}}}))

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

(defmulti order (fn [env key params] key))

(defmethod order :default [_ key _]
  (.warn js/console (str "Sorry, I do not understand " key)))

(defmethod order :dish/number [{:keys [state]} _ params]
  (let [number (:om.next/refid params)]
    {:value (some-> @state :dishes
                    (get number))}))

(defn- has-name? [item name]
  (let [names (conj (:aliases item []) (:name item))]
    (contains? (set names) name)))

(defn- find-by-name [collection name]
  (some (fn [item] (when (has-name? item name) item)) collection))

(defn- arrange [item aside]
  (-> item
      (update :components #(remove (partial contains? (set aside)) %))
      (merge (when-not (empty? aside) {:aside aside}))))

(defmethod order :dish/name [{:keys [state]} _ params]
  (let [name (:om.next/refid params)]
    {:value (some-> @state :dishes vals
                    (find-by-name name)
                    (arrange (:aside params [])))}))

(def waitress (om/parser {:read order}))

(defn main []
  (om/add-root! (om/reconciler {:state menu :parser waitress})
                Diner
                (.getElementById js/document "app")))

(main)
