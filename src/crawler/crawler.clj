(ns crawler.crawler
  (:use crawler.parser)
  (:use crawler.logger)
  (:use crawler.pool))
(declare crawl crawl-iter)

(def ^:dynamic max-depth 0)
(def prefix-pattern "   ")

(defn process-hrefs [hrefs depth]
  "Parallelization of url processing"
  (doseq [suburl hrefs]       
    (async #(crawl-iter suburl depth))))

(defn write-log-prefixed [text depth]
  (let [prefix (apply str (repeat depth prefix-pattern))]
    (println prefix text)
    (printlog prefix text)))

(defn log-cur-state [depth url count]  
  "Add current url info to results"
  (let [rec (apply str [url " " count " links"])]
    (write-log-prefixed rec depth)))

(defn process-error [depth url http-resp]
  (let [rec (apply str [url " " (:message http-resp)])]
    (write-log-prefixed rec depth)))

(defn crawl-iter [url cur-depth]
  "Process url"
  (def parsing-result (get-hrefs url))  
  (if (:success parsing-result)
    (let [hrefs (:hrefs parsing-result)]
      (log-cur-state cur-depth url (count hrefs))
      (when (< cur-depth max-depth)   
        (process-hrefs hrefs (inc cur-depth))))
    (process-error cur-depth url parsing-result)))

(defn crawl [url max-d]
  "Use url as a start for collecting hrefs. max-d - depth of search"
  (def max-depth max-d)
  (sync-wise
    #(crawl-iter url 0)))
