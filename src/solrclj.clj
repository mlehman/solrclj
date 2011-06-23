(ns solrclj
  "A clojure library for Apache Solr."
  (:import [org.apache.solr.client.solrj SolrServer SolrQuery])
  (:use [solrclj.servers :only [create-solr-server]] 
	[solrclj.documents :only [add-document
				  add-documents
				  create-solr-document
				  create-solr-documents]]
	[solrclj.response :only [response-base]])
  (:import [org.apache.solr.common.params MultiMapSolrParams]))

(defn ^SolrServer solr-server
  "Constructs a SolrServer with a configuration map. 
    :type - The implementation of SolrServer to create.

   Additional parameters depending on type:
    :http - CommonsHttpSolrServer
     :host - The base URL of the Solr server. Defaults to 127.0.0.1.
     :port - The port of the Solr server. Defaults to 8080.
     :path - The path to the Solr to the index. Defaults to '/solr'.
     :core - Optional if using multi-core solr.

    :embedded - EmbeddedSolrServer
     :solr-config - The solr configuration file. Defaults to 'solr.xml'.
     :dir - The directory path. Defaults to './solr'
     :core - Required if using multi-core solr."
  [conf]
  (create-solr-server conf))

(defn add
  "Adds maps as documents to the SolrServer."
  [^SolrServer solr-server & document-maps]
  (response-base
   (if (map? document-maps)
     (add-document solr-server (create-solr-document document-maps))
     (add-documents solr-server (create-solr-documents document-maps)))))

(defn delete-by-id [solr-server id]
  "Delete one or many documents by id."
  (.deleteById solr-server id))

(defn delete-by-query [solr-server query]
  (.deleteByQuery solr-server query))

(defn commit [solr-server]
  (.commit solr-server))

(defn rollback [solr-server]
  (.rollback solr-server))

(defn optimize [solr-server]
  (.optimize solr-server))

(defn ping [solr-server]
  "Issues a ping request to the server for monitoring."
  (response-base (.ping solr-server)))

(defn format-param [p]
  (if (keyword? p)
    (name p)
    (str p)))

(defn format-values [v]
  (let [v (if (vector? v) v [v])]
    (into-array (map format-param v))))

(defn- create-solr-params [m]	 	
  (MultiMapSolrParams. 	 	
   (reduce  #(doto %1 	 	
	       (.put (format-param (key %2)) 	 	
		     (format-values (val %2)))) 
	    (java.util.HashMap.) m)))

(defn- create-query [query options] 
   (create-solr-params (assoc (merge options {})
			 "q" query)))

(defn query [solr-server query & options]
  (let [response (.query solr-server
			 (create-query query (apply hash-map options)))]
   (response-base response)))