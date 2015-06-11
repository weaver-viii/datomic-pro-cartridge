(defproject datomicproapp "0.1.1-SNAPSHOT"

    :dependencies [[org.clojure/clojure "1.6.0"]
                   [http-kit "2.1.18"]
                   [com.datomic/datomic-pro "0.9.5173" :exclusions [joda-time]]]
    :repositories {"my.datomic.com" {:url "https://my.datomic.com/repo"
                                 }}
    :main webapp)
