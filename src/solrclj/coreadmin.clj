(ns solrclj.coreadmin
  "The CoreAdminHandler is a special SolrRequestHandler that is used to manage existing cores.
To enable dynamic core configuration, make sure the adminPath attribute is set in solr.xml. If this attribute is absent, the CoreAdminHandler will not be available."
  (:use [solrclj.response :only [response-base]])
  (:import [org.apache.solr.client.solrj SolrServer]
	   [org.apache.solr.client.solrj.request CoreAdminRequest]))


(defn status
  "Get the status for a given core. If using an EmbeddedSolrServer, returns only the instance's core."
  [^SolrServer s ^String core]
  (let [core-response (CoreAdminRequest/getStatus core s)
	response (response-base core-response)]
    response))

(defn create
  "Creates a new core based on preexisting instanceDir/solrconfig.xml/schema.xml, and registers it.
If persistence is enabled (persist=true), the configuration for this new core will be saved in 'solr.xml'.
If a core with the same name exists, while the 'new' created core is initalizing, the 'old' one will continue
to accept requests. Once it has finished, all new request will go to the 'new' core, and the 'old' core will be unloaded."
  [^SolrServer s ^String name ^String instanceDir ^String configFile ^String schemaFile]
  (let [core-response (CoreAdminRequest/createCore name instanceDir s configFile schemaFile)
	response (response-base core-response)]
    response))

(defn reload
  "Load a new core from the same configuration as an existing registered core. While the \"new\" core is initalizing, the \"old\" one will continue to accept requests. Once it has finished, all new request will go to the \"new\" core, and the \"old\" core will be unloaded."
  [^SolrServer s ^String core]
  (let [core-response (CoreAdminRequest/reloadCore core s)
	response (response-base core-response)]
    response))