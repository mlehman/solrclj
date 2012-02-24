(ns solrclj-test
  (:use [solrclj.data]
        [solrclj.test-helpers])
  (:use [solrclj] :reload-all)
  (:use [clojure.test])
  (:import [org.apache.solr.client.solrj.embedded EmbeddedSolrServer]
           [org.apache.solr.client.solrj.impl CommonsHttpSolrServer]
           [org.apache.solr.client.solrj.response QueryResponse]))

(deftest test-solr-server
  (testing "Construct an EmbeddedServer"
    (is (instance? EmbeddedSolrServer (solr-server test-embedded-books-conf))))
  (testing "Construct a CommonsHttpSolrServer."
    (is (instance? CommonsHttpSolrServer (solr-server test-http-conf))))
  (testing "Construct a set of EmbeddedServer instances using a single core-container"
    (let [cores (solr-server {:type :embedded-multi :dir "test-solr"})]
      (is (map? cores))
      (every? #(is (instance? EmbeddedSolrServer %)) (vals cores)))))

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

(defn reload-books
  [s]
  (delete-by-query s "*:*")
      (commit s)
      (apply add s top-selling-books)
      (commit s))

(deftest test-query
  (testing "Query Solr"
    (let [s (solr-server test-embedded-books-conf)]
      (reload-books s)
      (is (= 15 (get-in (query s "*:*") [:response :numFound])))
      (is (= 3 (get-in (query s "title:s*") [:response :numFound])))
      (is (= 3 (get-in (query s "title:s*" :rows 1) [:response :numFound])))
      (is (= 1 (get-in (query s "rank:1") [:response :numFound])))
      (is (= 15 (get-in (query s "*:*") [:response :numFound])))
      (is (= 10 (get-in (query s "*:*" :fq "language:en") [:response :numFound])))
      (is (= 2 (get-in (query s "*:*" :fq "published:[2000 TO *]") [:response :numFound])))
      (is (= 7 (get-in (query s "*:*" :fq ["language:en"
                                           "published:[1900 TO *]"]) [:response :numFound])))
      (is (= 1 (get-in (query s "*:*" :fq ["language:zh"
                                            "published:[1900 TO *]"]) [:response :numFound]))))))

(deftest test-response-contains-metadata
  (testing "Response metadata"
    (let [s (solr-server test-embedded-books-conf)
          _ (reload-books s)
          response (query s "*:*")]
      (is (= (class (-> response meta :response)) QueryResponse)))))

(deftest test-facet-order-by-count
  (testing "Facet order by count"
    (let [s (solr-server test-embedded-books-conf)
          _ (reload-books s)
          response (query s "*:*" :facet.sort "count" :facet true :facet.field ["language" "codes"])
          ff (-> response :facet_counts :facet_fields)
          code-counts (-> ff :codes vals)
          lang-counts (-> ff :language vals)]
      (is (= code-counts) [7, 6, 5, 5, 4, 3, 2, 2, 1, 1, 1, 1, 1])
      (is (= lang-counts [10, 2, 1, 1, 1])))))

(deftest test-facet-order-by-index
  (testing "Facet order by index"
    (let [s (solr-server test-embedded-books-conf)
          _ (reload-books s)
          response (query s "*:*" :facet.sort "index" :facet true :facet.field ["language" "codes"])
          ff (-> response :facet_counts :facet_fields)
          code-keys (-> ff :codes keys)
          lang-keys (-> ff :language keys)]
      (is (= code-keys) '(:A :B :C :D :F :G :I :L :P :Q :X :Y :Z))
      (is (= lang-keys '(:de :en :fr :pt :zh))))))

(deftest test-delete
  (testing "Delete documents in Solr"
    (let [s (solr-server test-embedded-books-conf)]
      (testing nil))))

(defn test-ns-hook []
  (test-solr-server)
  (with-jetty-solr
    (test-ping)
    (test-add)
    (test-query)
    (test-facet-order-by-count)
    (test-facet-order-by-index)
    (test-response-contains-metadata)))