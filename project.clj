(defproject solrclj "0.3.0-SNAPSHOT"
  :description "A clojure library for using Apache Solr."
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.apache.solr/solr-solrj "3.6.2"]
                 [org.slf4j/slf4j-nop "1.6.6"]]
  :profiles {:dev {:dependencies [[org.apache.solr/solr-core "3.6.2"]
                     [javax.servlet/servlet-api "2.5"]
                     [commons-io/commons-io "2.4"]
                     [lein-clojars "0.9.1"]
                     [org.mortbay.jetty/jetty "6.1.26"]
                     [org.mortbay.jetty/jsp-2.1-jetty "6.1.26"]
                     [org.mortbay.jetty/jsp-api-2.1 "6.1.14"]]}})