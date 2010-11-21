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
    (let [s (solr-server test-embedded-books-conf)
	  r (apply add s top-selling-books)]
      (commit s)
      (is (= 0 (get-in r [:responseHeader :status])))
      (is (= 15 (get-in (query s "*:*") [:response :numFound])))
      )))

(deftest test-query
  (testing "Query Solr"
    (let [s (solr-server test-embedded-books-conf)]
      (delete-by-query s "*:*")
      (apply add s top-selling-books)
      (is (= 15 (get-in (query s "*:*") [:response :numFound])))
      (is (= 3 (get-in (query s "title:s*") [:response :numFound])))
      (is (= 1 (get-in (query s "rank:1") [:response :numFound]))))))

(deftest test-delete
  (testing "Delete documents in Solr"
    (let [s (solr-server test-embedded-books-conf)]
      (testing nil))))

(defn test-ns-hook []
  (test-solr-server)
  (with-jetty-solr
    (test-ping)
    (test-add)
    (test-query)))