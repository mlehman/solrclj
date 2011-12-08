(ns solrclj.response
  (:use [solrclj.documents :only [create-maps-from-solr-documents]])
  (:import [org.apache.solr.client.solrj.response SolrResponseBase]
       [org.apache.solr.common.util NamedList SimpleOrderedMap]
       [org.apache.solr.common SolrDocumentList]
       [java.util ArrayList]))

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
   (instance? SolrDocumentList v) (convert-solr-document-list v)
   (instance? NamedList v) (convert-named-list v)
   (instance? ArrayList v) (vec (map convert-value v))
   :else v))

(defn convert-map-entry
  "Converts a MapEntry from a NamedList into a vector with 2 values. Nested NamedLists are recursively converted. "
  [map-entry]
  (let [k (convert-key (.getKey map-entry))
        v (convert-value (.getValue map-entry))]
    [k v]))

(defn- uniquify-paired-seq
  "removes values when dup-key-in-paired-seq? returns true"
  [coll]
  (map #(first (val %1)) (group-by first coll)))

(defn convert-named-list
   "Converts a NamedList into a array-map. Nested NamedLists are recursively converted. "
  [named-list]
  (let [itrseq (iterator-seq (.iterator named-list))
        converted (map convert-map-entry itrseq)
        reduced   (uniquify-paired-seq converted)
        flattened (apply concat reduced)
        result    (apply array-map flattened)]
    result))

(defn response-base
  "Convert a ResponseBase into a map. Stores the ResponseBase as :response in the map's metadata."
  [^SolrResponseBase r]
  (with-meta (convert-named-list (.getResponse r)) {:response r}))
