(ns solrclj.servers
  (:import (java.io File)
	   (org.apache.solr.client.solrj.embedded EmbeddedSolrServer)
	   (org.apache.solr.core CoreContainer)))

(defn create-embedded-solr-server [{:keys [dir solr-config core]}]
  (let [solr-config-file (File. (File. dir) solr-config)
	container (CoreContainer. dir solr-config-file)]
    (EmbeddedSolrServer. container core)))