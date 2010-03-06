(ns solrclj
  "A clojure library for Apache Solr."
  (:import (org.apache.solr.client.solrj SolrQuery))
  (:use solrclj.servers 
	solrclj.documents
	solrclj.util))

;; Default configuration settings
(def default-config {:type :embedded
		     :solr-config "solr.xml"
		     :dir "./solr"})

(defn create-solr-server [config]
  (create-embedded-solr-server (merge default-config config)))

(defn add [solr-server map]
  (add-document solr-server (create-solr-document map)))

(defn delete-by-id [solr-server id]
  (.deleteById solr-server id))

(defn delete-by-query [solr-server query]
  (.deleteByQuery solr-server query))

(defn commit [solr-server]
  (.commit solr-server))

(defn ping [solr-server]
  (.ping solr-server))

(defn query [solr-server query]
  (let [results (.getResults (.query solr-server
			 (SolrQuery. query)))]
   {:num-found (.getNumFound results)
    :start (.getStart results)
    :max-score (.getMaxScore results)
    :results (set (create-maps-from-solr-documents results))}))