(ns om-next-dataflow.parsers.datomic-server
  (:require [datomic.api :as datomic]))

(def kitchen "datomic:free://localhost:4334/kitchen")

(def schema
  [{:db/id #db/id[:db.part/db]
    :db/ident :dish/number
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc "For the ones who hate to spell out lengthy names"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :dish/name
    :db/valueType :db.type/string
    :db/fulltext true
    :db/cardinality :db.cardinality/one
    :db/doc "This is how it is actually called"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :dish/aliases
    :db/valueType :db.type/string
    :db/fulltext true
    :db/cardinality :db.cardinality/many
    :db/doc "If you love to come up with your own ways of expressing yourself"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :dish/components
    :db/valueType :db.type/keyword
    :db/cardinality :db.cardinality/many
    :db/doc "Yummy!"
    :db.install/_attribute :db.part/db}])

(def dishes
  [{:db/id #db/id[:db.part/user]
    :dish/number 1
    :dish/name "Chef Salad"
    :dish/components [:eggs :turkey :cheese :lettuce :oil :vinegar]}

   {:db/id #db/id[:db.part/user]
    :dish/number 2
    :dish/name "Pie a la mode"
    :dish/aliases ["Apple a la mode" "Vanilla"]
    :dish/components [:apple-pie :vanilla-ice-cream]}

   {:db/id #db/id[:db.part/user]
    :dish/number 3
    :dish/name "Meatloaf Sandwich"
    :dish/components [:meatloaf :lettuce :tomato :mayo :sourdough]}

   {:db/id #db/id[:db.part/user]
    :dish/number 4
    :dish/name "Strawberry Sundae"
    :dish/components [:strawberry-ice-cream :wafer :whipped-cream]}])

(defn create-menu []
  (datomic/delete-database kitchen)
  (datomic/create-database kitchen)
  (let [connection (datomic/connect kitchen)]
    @(datomic/transact connection schema)
    @(datomic/transact connection dishes)))

(defmulti prepare (fn [env key state] key))
