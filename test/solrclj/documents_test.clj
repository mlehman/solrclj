(ns solrclj.documents-test
  (:use [solrclj.documents] :reload-all)
  (:use [clojure.test]))

(deftest create-solr-document-test
  (let [test-map {:title "My Document" :author "Joe Blogger" :comments 4}
	result-document (create-solr-document test-map)]
    (is result-document "Document created")
    (is (= (.getFieldValue result-document "title") "My Document") "String value set")
    (is (= (.getFieldValue result-document "comments") 4) "Int value set")))

(deftest create-map-from-solr-document-test
  (let  [test-map {:title "My Document" :author "Joe Blogger" :comments 4}
	 test-document (create-solr-document test-map)
	 result-map (create-map-from-solr-document test-document)]
    (is (= (result-map :author) "Joe Blogger"))))

