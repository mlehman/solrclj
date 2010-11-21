(ns solrclj
  "A clojure library for Apache Solr."
  (:import [org.apache.solr.client.solrj SolrServer SolrQuery])
  (:use [solrclj.servers] 
	[solrclj.documents]
	[solrclj.response :only [response-base]]
	[solrclj.util]))

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
  (if (second document-maps)
    (add-documents solr-server (create-solr-documents document-maps))
    (add-document solr-server (create-solr-document document-maps))))

(defn delete-by-id [solr-server id]
  "Delete one or many documents by id."
  (.deleteById solr-server id))

(defn delete-by-query [solr-server query]
  (.deleteByQuery solr-server query))

(defn commit [solr-server]
  (.commit solr-server))

(defn optimize [solr-server]
  (.optimize solr-server))

(defn ping [solr-server]
  "Issues a ping request to the server for monitoring."
  (response-base (.ping solr-server)))

(defn format-param [v]
  (if (keyword? v) (name v) (str v)))

(defn create-solr-params [m]
  (reduce #(let [k (key %2) v (val %2) vv (if (vector? v) v [v])]
    (doto %1 (.set k (into-array (map format-param vv)))))
	  (org.apache.solr.common.params.ModifiableSolrParams.)
	  m))

(defn create-query [query options] 
   (create-solr-params (assoc (merge options {})
			 "q" query)))

(defn- create-facet-map [facet]
  (hash-map :name (.getName facet) 
	    :values (reduce #(assoc %1 (.getName %2) (.getCount %2) ) 
			    {} (.getValues facet))))

(defn- create-facets-maps [facets]
  (map create-facet-map facets))

(defn query [solr-server query & options]
  (let [response (.query solr-server
			 (create-query query (apply hash-map options)))
	results (.getResults response)
	facets (.getFacetFields response)]
   {:num-found (.getNumFound results)
    :start (.getStart results)
    :max-score (.getMaxScore results)
    :results (set (create-maps-from-solr-documents results))
    :facets (create-facets-maps facets)}))