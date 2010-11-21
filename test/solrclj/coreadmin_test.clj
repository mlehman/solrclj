(ns solrclj.coreadmin-test
  (:use [solrclj]
	[solrclj.test-helpers])
  (:use [solrclj.coreadmin] :reload-all)
  (:use [clojure.test]))

(deftest test-status
  (let [solr-server (solr-server test-embedded-books-conf)]
    (testing "Check books status with embedded."
      (let [r (status solr-server "books")]
	(is (< 0 (get-in r [:status :books :uptime]))))))
  (let [solr-server (solr-server test-http-conf)]
    (testing "Check books status with http."
      (let [r (status solr-server "books")]
	(is (< 0 (get-in r [:status :books :uptime])))))))

(deftest test-create
  (let [solr-server (solr-server test-http-conf)]
    (testing "Create a new core."
      (let [r  (create solr-server "magazines" "magazines"
		       (map-path "test-solr/conf/solrconfig.xml")
		       (map-path "test-solr/conf/schema.xml"))]
	(is (= "magazines" (:core r))))))
  (delete-test-core "magazines"))

(defn test-ns-hook []
  (with-jetty-solr
    (test-status)
    (test-create)))