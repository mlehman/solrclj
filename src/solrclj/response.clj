(ns solrclj.response
  (:import [org.apache.solr.client.solrj.response SolrResponseBase]
	   [org.apache.solr.common.util NamedList]))

(declare convert-named-list)

(defn convert-key
  [k]
  (if (string? k)
    (keyword k)
    k))

(defn convert-map-entry
  "Converts a MapEntry from a NamedList into a map. Nested NamedLists are recursively converted. "
  [map-entry]
  (let [k (convert-key (.getKey map-entry))
	v (.getValue map-entry)]
    (if (instance? NamedList v)
      {k (convert-named-list v)}
      {k v})))

(defn convert-named-list
   "Converts a NamedList into a map. Nested NamedLists are recursively converted. "
  [named-list]
  (reduce merge
	  (map convert-map-entry (iterator-seq (.iterator named-list)))))

(defn response-base
  "Convert a ResponseBase into a map."
  [^SolrResponseBase r]
  (convert-named-list (.getResponse r)))