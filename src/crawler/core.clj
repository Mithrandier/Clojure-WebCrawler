(ns crawler.core
  (:gen-class)
  (:use crawler.logger crawler.crawler)
  )

(def root-url "https://www.google.com/")

(defn -main
  "Crawl specified url for others urls. And again.. and again.."
  [log-filename max-depth & args]
    (clear-log)
    (crawl root-url max-depth true)
    (save-log "output.txt"))
