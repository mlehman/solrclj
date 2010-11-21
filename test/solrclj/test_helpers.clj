(ns solrclj.test-helpers
  (:use [clojure.contrib.java-utils])
  (:import [org.mortbay.jetty.webapp WebAppContext]
	   [org.mortbay.jetty Connector Server Request Response Handler]
	   [org.mortbay.jetty.nio SelectChannelConnector]
	   [org.mortbay.jetty.handler AbstractHandler HandlerCollection]
           [org.mortbay.jetty.bio SocketConnector]
           [org.mortbay.jetty.security SslSocketConnector]
	   [java.io File]))

(def test-solr-root "test-solr")
(def test-embedded-books-conf {:dir test-solr-root :core "books"})
(def test-http-conf {:type :http :port 2345})
(def test-http-books-conf (assoc test-http-conf :core "books"))

(defn map-path
  "Helper to map relative paths to absolute paths."
  ([path] (.getAbsolutePath (File. path)))
  ([root path] (.getAbsolutePath (File. root path))))

(defn create-web-app-context [path descriptor war]
  (doto (WebAppContext.)
    (.setContextPath path)
    (.setWar war)
    (.setDefaultsDescriptor descriptor)))

(defn create-connector [port]
  (doto (SelectChannelConnector.)
    (.setPort port)))

(defn create-handler-collection [handlers]
  (doto (HandlerCollection.)
    (.setHandlers (into-array Handler handlers))))

(defn create-solr-web-app [root]
  (System/setProperty "solr.solr.home" "test-solr")
  (create-web-app-context
	  "/solr"
	  (map-path root "etc/webdefault.xml")
	  (map-path root "webapps/solr.war")))

(defn start-jetty
  []
  (doto (Server.)
    (.addConnector (create-connector 2345))
    (.setHandler (create-solr-web-app "test-jetty"))
    (.start)))
       
(defn stop-jetty [#^Server jetty-server]
  (.stop jetty-server))

(defmacro with-jetty-solr
  [& body]
  `(let [j# (start-jetty)]
     (try
       ~@body
       (finally
	(stop-jetty j#)
	(.join j#)))))

(defn delete-test-core
  [core]
  (delete-file-recursively (map-path test-solr-root core)))
