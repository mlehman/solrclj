# solrclj

A clojure library for Apache Solr.

Solr Documents are treated as maps. 

Use
----------

<pre>

;; Embedded Solr Example
(def server (create-solr-server {:core "mycore" :dir "/home-path"})

;; Adding Documents
(add server {:title "Don Quixote" :author "Miguel de Cervantes" :summary "..." })

;; Query
(query server "la mancha")

;; Delete
(delete-by-query server "*:*)

;; Commit 
(commit server)

</pre>
