(ns solrclj.servers
  ^{:doc "A clojure library for Apache Solr."
      :author "Matt Lehman"}
  (:import [java.io File]
	   [org.apache.solr.client.solrj.embedded EmbeddedSolrServer]
	   [org.apache.solr.client.solrj.impl CommonsHttpSolrServer]
	   [org.apache.solr.core CoreContainer]))

(defmulti create-solr-server :type)

(def default-embedded-config 
     {:type :embedded
      :solr-config "solr.xml"
      :dir "./solr"})

(defmethod create-solr-server :default [config]
  (let [config (merge default-embedded-config config)
	{:keys [dir solr-config core]} config
	solr-config-file (File. (File. dir) solr-config)
	container (CoreContainer. dir solr-config-file)]
    (EmbeddedSolrServer. container core)))

(def default-http-config 
     {:type :http
      :host "127.0.0.1"
      :port 8080
      :path "/solr"})

(defn create-server-url [{:keys [host port path]}]
  (str "http://" host ":" port path))

(defmethod create-solr-server :http [config]
  (let [config  (merge default-http-config config)]
      (CommonsHttpSolrServer. (create-server-url config))))
