(ns webapp
  (:use org.httpkit.server)
  (:require [datomic.api :as d]))

(def dbip
  (get (System/getenv) "OPENSHIFT_DATOMIC_HTTP_IP" "localhost"))

(def dbport
  (if (get (System/getenv) "OPENSHIFT_INTERNAL_IP")
    "24334" "4334")
)

(def uri (str "datomic:dev://" dbip  ":" dbport "/example"))
(d/create-database uri)
(def conn (d/connect uri))

(d/transact
  conn
  [[:db/add
    (d/tempid :db.part/user)
    :db/doc
    "Hello! From Datomic!"]])

(def db (d/db conn))

;; query input and result are data
(def q-result (d/q '[:find ?e .
                    :where [?e :db/doc  "Hello! From Datomic!"]]
                  db))

(def ent (d/entity db q-result))
 
(def query-result (str (:db/doc (d/touch ent))
                       " If you see this message it means that you are connected to the database."))


(defn app [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body  (or query-result "Hello world")})

(defn -main [& args]
  (let [port (Integer/parseInt (get (System/getenv) "OPENSHIFT_DATOMIC_HTTP_PORT" "8080"))
        ip (get (System/getenv) "OPENSHIFT_DATOMIC_HTTP_IP" "0.0.0.0")]
    (run-server app {:ip ip :port port})))
