(ns crawler.core
  (:gen-class)
  (:use crawler.logger crawler.crawler))

(defn -main
  [log-filename max-depth]
    (clear-log)
    (crawl root-url max-depth)
    (save-log "output.txt"))
