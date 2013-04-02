(defproject solrclj3 "0.1.2"
  :description "A clojure library for using Apache Solr."
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.apache.solr/solr-solrj "1.4.0"]
                 [org.slf4j/slf4j-nop "1.5.5"]]
  :profiles {:dev {:dependencies [[org.apache.solr/solr-core "1.4.0"]
                     [javax.servlet/servlet-api "2.5"]
                     [commons-io/commons-io "2.4"]
                     [swank-clojure "1.3.0"]
                     [org.clojars.oskarkv/lein-vimclojure "1.0.0-SNAPSHOT"]
                     [lein-clojars "0.5.0"]
                     [org.mortbay.jetty/jetty "6.1.26"]
                     [org.mortbay.jetty/jsp-2.1-jetty "6.1.26"]
                     [org.mortbay.jetty/jsp-api-2.1 "6.1.14"]]}})

