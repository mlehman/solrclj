(defproject solrclj "0.1.1"
  :description "A clojure library for using Apache Solr."
  :dependencies [[org.clojure/clojure "1.3.0"]
         [org.apache.solr/solr-solrj "1.4.0"]
         [org.slf4j/slf4j-nop "1.5.5"]]
  :dev-dependencies [[org.apache.solr/solr-core "1.4.0"]
             [javax.servlet/servlet-api "2.5"]
             [org.clojure/clojure-contrib "1.2.0"] ; this is for java-utils, and should be updated
             [swank-clojure "1.3.0"]
             [lein-clojars "0.5.0"]
             [org.mortbay.jetty/jetty "6.1.15"]
             [org.mortbay.jetty/jsp-2.1-jetty "6.1.15"]
             [org.mortbay.jetty/jsp-api  "2.1-6.0.1"]])

