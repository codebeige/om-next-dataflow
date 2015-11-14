(ns om-next-dataflow.parsers.datascript
  (:require [datascript.core :as datascript]))

(defn create-menu []
  (doto (datascript/create-conn
         {:dish/aliases {:db/cardinality :db.cardinality/many}
          :dish/components {:db/cardinality :db.cardinality/many}})
    (datascript/transact!
     [{:dish/number 1
       :dish/name "Chef Salad"
       :dish/components [:eggs :turkey :cheese :lettuce :oil :vinegar]}

      {:dish/number 2
       :dish/name "Pie a la mode"
       :dish/aliases ["Apple a la mode" "Vanilla"]
       :dish/components [:apple-pie :vanilla-ice-cream]}

      {:dish/number 3
       :dish/name "Meatloaf Sandwich"
       :dish/components [:meatloaf :lettuce :tomato :mayo :sourdough]}

      {:dish/number 4
       :dish/name "Strawberry Sundae"
       :dish/components [:strawberry-ice-cream :wafer :whipped-cream]}])))

(defmulti serve (fn [env key params] key))

(defmethod serve :default [_ key _]
  (.warn js/console (str "Sorry, I do not understand " key)))

(defmethod serve :dish/number [{:keys [state]} _ params]
  {:value (datascript/q '[:find (pull ?e [*]) .
                          :in $ ?number
                          :where [?e :dish/number ?number]]
                        @state (:om.next/refid params))})

(defn- arrange [item aside]
  (if-not (empty? aside)
    (-> item
        (update :dish/components #(remove (partial contains? (set aside)) %))
        (merge {:aside aside}))
    item))

(defmethod serve :dish/name [{:keys [state]} _ params]
  {:value (-> (datascript/q '[:find (pull ?e [*]) .
                              :in $ % ?name
                              :where (has-name ?e ?name)]
                            @state
                            '[[(has-name ?e ?name) [?e :dish/name ?name]]
                              [(has-name ?e ?name) [?e :dish/aliases ?name]]]
                            (:om.next/refid params))
              (arrange (:aside params)))})
