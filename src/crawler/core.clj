(ns crawler.core
  (:gen-class)
  (:use crawler.logger crawler.crawler)
  (:require [clojure.java.io :as io]))

(declare readlines-from)

(def output-filename "output.txt")

(defn perform [urls max-depth]  
  (clear-log)
  (doseq [url urls]  
    (crawl url max-depth))
  (save-log output-filename))

(defn -main
  [urls-filename max-depth]
  (let [urls (readlines-from urls-filename)]
    (perform urls max-depth)))

(defn readlines-from [filename]
  (with-open [rdr (io/reader filename)]
    (doall (line-seq rdr))))
