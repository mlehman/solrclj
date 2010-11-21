(ns solrclj-test
  (:use [solrclj.data]
	[solrclj.test-helpers])
  (:use [solrclj] :reload-all)
  (:use [clojure.test])
  (:import [org.apache.solr.client.solrj.embedded EmbeddedSolrServer]
	   [org.apache.solr.client.solrj.impl CommonsHttpSolrServer]))

(deftest test-solr-server
  (testing "Construct an EmbeddedServer"
    (is (instance? EmbeddedSolrServer (solr-server test-embedded-books-conf))))
  (testing "Construct a CommonsHttpSolrServer."
    (is (instance? CommonsHttpSolrServer (solr-server test-http-conf)))))

(deftest test-ping
  (let [s (solr-server test-http-books-conf)
	r (ping s)]
    (is (= "OK" (:status r)))))

(deftest test-add
  (testing "Add documents to Solr"
    (let [s (solr-server test-embedded-books-conf)]
      (testing (= "" (apply add s top-selling-books))))))

(defn test-ns-hook []
  (test-solr-server)
  (with-jetty-solr
    (test-ping)
    (test-add)))