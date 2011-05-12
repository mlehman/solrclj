(ns solrclj.documents
  (:import (org.apache.solr.common SolrInputDocument)))

(defn create-solr-document [document-map]
  (reduce 
   #(doto %1 (.addField (name (key %2)) (val %2)))
   (SolrInputDocument.) 
   document-map))

(defn add-document [solr-server solr-document]
  (.add solr-server solr-document))

(defn create-solr-documents [document-maps]
  (map create-solr-document document-maps))

(defn add-documents [solr-server solr-documents]
  (.add solr-server solr-documents))

(defn create-map-from-solr-document [document]
  (into {} (for [fld (.getFieldNames document)]
	     {(keyword fld) (.getFieldValue document fld)})))

(defn create-maps-from-solr-documents [documents]
  (map create-map-from-solr-document documents))