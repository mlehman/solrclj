(defproject solrclj "0.1.0-SNAPSHOT"
  :description "A clojure library for using Apache Solr."
  :dependencies [[org.clojure/clojure "1.1.0"]
                 [org.clojure/clojure-contrib "1.1.0"]
		 [org.apache.solr/solr-solrj "1.4.0"]
		 [org.apache.solr/solr-core "1.4.0"]
		 [javax.servlet/servlet-api "2.5"]
		 [org.slf4j/slf4j-simple "1.5.5"]]
  :dev-dependencies [[leiningen/lein-swank "1.1.0"]])