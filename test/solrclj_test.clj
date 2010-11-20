(ns solrclj-test
  (:use [solrclj.data])
  (:use [solrclj] :reload-all)
  (:use [clojure.test])
  (:import [org.apache.solr.client.solrj.embedded EmbeddedSolrServer]
	   [org.apache.solr.client.solrj.impl CommonsHttpSolrServer]))

(def test-embedded-conf {:dir "./test-solr" :core "books"})
(def test-http-conf {:type :http})

(deftest test-solr-server
  (testing "Construct an EmbeddedServer"
    (is (instance? EmbeddedSolrServer (solr-server test-embedded-conf))))
  (testing "Construct a CommonsHttpSolrServer."
    (is (instance? CommonsHttpSolrServer (solr-server test-http-conf)))))


(deftest test-add
  (testing "Add documents to Solr"
    (let [s (solr-server test-embedded-conf)]
      (testing (= "" (apply add s top-selling-books))))))

