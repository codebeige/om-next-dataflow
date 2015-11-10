(ns om-next-dataflow.parsers.atom)

(defn create-menu []
  (atom {:dishes {1 {:name "Chef Salad"
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

(defmulti serve (fn [env key params] key))

(defmethod serve :default [_ key _]
  (.warn js/console (str "Sorry, I do not understand " key)))

(defmethod serve :dish/number [{:keys [state]} _ params]
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

(defmethod serve :dish/name [{:keys [state]} _ params]
  (let [name (:om.next/refid params)]
    {:value (some-> @state :dishes vals
                    (find-by-name name)
                    (arrange (:aside params [])))}))
