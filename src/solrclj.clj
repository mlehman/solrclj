(ns solrclj
  "A clojure library for Apache Solr."
  (:import (org.apache.solr.client.solrj SolrQuery))
  (:use solrclj.servers 
	solrclj.documents
	solrclj.util))

(defn add [solr-server map]
  (add-document solr-server (create-solr-document map)))

(defn delete-by-id [solr-server id]
  (.deleteById solr-server id))

(defn delete-by-query [solr-server query]
  (.deleteByQuery solr-server query))

(defn commit [solr-server]
  (.commit solr-server))

(defn optimize [solr-server]
  (.optimize solr-server))

(defn ping [solr-server]
  (.ping solr-server))

(defn format-param [v]
  (if (keyword? v) (name v) (str v)))

(defn- create-solr-params [m]
  (org.apache.solr.common.params.MapSolrParams. 
   (reduce  #(doto %1 
	       (.put (format-param (key %2)) 
		     (format-param (val %2)))) 
	    (java.util.HashMap.) m)))

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