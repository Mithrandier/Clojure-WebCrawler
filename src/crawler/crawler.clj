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

(defn log-cur-state [depth url count]  
  "Add current url info to results"
  (let [prefix (apply str (repeat depth prefix-pattern))]
    (let [rec (apply str [prefix url " " count " links"])]
      (println rec)
      (printlog rec))))

(defn crawl-iter [url cur-depth]
  "Process url and "
  (def parsing-result (get-hrefs url))  
  (if (isa? parsing-result Exception)
    (let [hrefs parsing-result]
      (log-cur-state cur-depth url (count hrefs))
      (when (< cur-depth max-depth)   
        (process-hrefs hrefs (inc cur-depth))))))

(defn crawl [url max-d]
  "Use url as a start for collecting hrefs. max-d - depth of search"
  (def max-depth max-d)
  (sync-wise
    #(crawl-iter url 0)))
