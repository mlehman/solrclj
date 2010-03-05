(ns solrclj.documents
  (:import (org.apache.solr.common SolrInputDocument)))

(defn add-map-by-method [obj method map]
  (reduce #(doto %1 (method (name (key %2)) (val %2))) obj map))

(defn create-solr-document [document-map]
  (add-map-by-method
   (SolrInputDocument.) 
   #(.addField %1 %2 %3) 
   document-map))

(defn add-document [solr-server solr-document]
  (.add solr-server solr-document))

(defn create-solr-documents [document-maps]
  (map create-solr-document document-maps))

(defn add-documents [solr-server solr-documents]
  (.add solr-server solr-documents))

(defn create-map-from-solr-document [document]
  (apply merge 
	 (map #(hash-map % (.getFieldValue document %)) (.getFieldNames document))))

(defn create-maps-from-solr-documents [documents]
  (map create-map-from-solr-document documents))