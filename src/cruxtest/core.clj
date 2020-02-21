(ns cruxtest.core
  (:gen-class)
  (:require [clojure.pprint :as pprint])
  )
  (require '[crux.api :as crux])
  (import (crux.api ICruxAPI))
  
(defn p [i]
  (pprint/pprint i)
)

(def ^crux.api.ICruxAPI node
  (crux/start-node {:crux.node/topology :crux.standalone/topology
                    :crux.node/kv-store "crux.kv.memdb/kv"
                    :crux.kv/db-dir "data/db-dir-1"
                    :crux.standalone/event-log-dir "data/eventlog-1"
                    :crux.standalone/event-log-sync? true
                    :crux.standalone/event-log-kv-store "crux.kv.memdb/kv"}))

(defn -main
  [& args]
    (def my-document
      {:crux.db/id :some/fancy-id
        :arbitrary-key ["an untyped value" 123]
        :nested-map {"and values" :can-be-arbitrarily-nested}}
    )
      
      (p (crux/submit-tx node [[:crux.tx/put my-document]]))
      (def db (crux/db node))
      (p (crux/entity db :some/fancy-id))
        (.close node)
        (println "DB killed...")
  )


