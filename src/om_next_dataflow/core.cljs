(ns om-next-dataflow.core)

(defn init []
 (enable-console-print!)
 {:message "Hello from ClojureScript!!!"})

(defonce config (init))

(defn main []
  (println (:message config))
  nil)

(defonce app (main))
