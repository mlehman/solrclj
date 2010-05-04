# solrclj

A clojure library for Apache Solr.

Solr Documents are treated as maps. 

Use
----------

<pre>

;; Embedded Solr Example
(def server (create-solr-server {:type :embedded :core "mycore" :dir "/home-path"})

;; Http Solr Example
(def server (create-solr-server {:type :http :host "localhost"})

;; Adding Documents
(add server {:title "Don Quixote" :author "Miguel de Cervantes" :summary "..." })

;; Simple Query
(query server "la mancha")

::Paging
(query server "author:c*" :rows 100 : start 100)

;;Faceting
(query server "*:*" :rows 0 :facet true :facet.field :author)

;; Delete
(delete-by-query server "*:*")

;; Commit 
(commit server)

</pre>
