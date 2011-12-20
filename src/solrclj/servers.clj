(ns solrclj.servers
  ^{:doc "A clojure library for Apache Solr."
      :author "Matt Lehman"}
  (:import [java.io File]
	   [org.apache.solr.client.solrj.impl CommonsHttpSolrServer]))

(defmulti create-solr-server :type)

(def default-embedded-config
     {:type :embedded
      :solr-config "solr.xml"
      :dir "./solr"})

(defn construct-via-reflection
  [class & params]
  (clojure.lang.Reflector/invokeConstructor
   (resolve (symbol class))
   (to-array params)))

(defn embedded-solr-server
  "Constructs an EmbeddedSolrServer"
  [container core]
  (construct-via-reflection "org.apache.solr.client.solrj.embedded.EmbeddedSolrServer"
			    container core))

(defn core-container
  "Constructs an CoreContainer"
  [dir config-file]
  (construct-via-reflection "org.apache.solr.core.CoreContainer"
			   dir config-file))

(defmethod create-solr-server :default [config]
  (let [config (merge default-embedded-config config)
	{:keys [dir solr-config core]} config
	solr-config-file (File. (File. dir) solr-config)
	container (core-container dir solr-config-file)]
    (embedded-solr-server container core)))

(def default-http-config
     {:type :http
      :host "127.0.0.1"
      :port 8080
      :path "/solr"})

(defn create-server-url [{:keys [host port path core]}]
  (str "http://" host ":" port path (if core (str "/" core))))

(defmethod create-solr-server :http [config]
  (let [config  (merge default-http-config config)]
      (CommonsHttpSolrServer. (create-server-url config))))
