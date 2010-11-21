(ns solrclj.response
  (:use [solrclj.documents :only [create-maps-from-solr-documents]])
  (:import [org.apache.solr.client.solrj.response SolrResponseBase]
	   [org.apache.solr.common.util NamedList]
	   [org.apache.solr.common SolrDocumentList]))

(declare convert-named-list)

(defn convert-key
  [k]
  (if (string? k)
    (keyword k)
    k))

(defn convert-solr-document-list
  [l]
  {:maxScore (.getMaxScore l)
   :numFound (.getNumFound l)
   :start (.getStart l)
   :docs (create-maps-from-solr-documents (iterator-seq (.iterator l)))})

(defn convert-value
  [v]
  (cond
     (instance? NamedList v) (convert-named-list v)
     (instance? SolrDocumentList v) (convert-solr-document-list v)
     :else v))

(defn convert-map-entry
  "Converts a MapEntry from a NamedList into a map. Nested NamedLists are recursively converted. "
  [map-entry]
  (let [k (convert-key (.getKey map-entry))
	v (.getValue map-entry)]
    {k (convert-value v)}))

(defn convert-named-list
   "Converts a NamedList into a map. Nested NamedLists are recursively converted. "
  [named-list]
  (reduce merge
	  (map convert-map-entry (iterator-seq (.iterator named-list)))))

(defn response-base
  "Convert a ResponseBase into a map."
  [^SolrResponseBase r]
  (convert-named-list (.getResponse r)))