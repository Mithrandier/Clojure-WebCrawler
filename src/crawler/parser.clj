(ns crawler.parser
  (:require [clj-http.client :as client])
  (:require [hickory.select :as hick])
  (:use hickory.core))

(defn parse-html [html]
  "Parse raw html"
  (-> html parse as-hickory))

(defn extract-hrefs [html]
  "Extract from raw html"
  (let [tree (parse-html html)]
    (let [links (hick/select (hick/tag :a) tree)]
      (map #(-> % :attrs :href) links))))

(defn get-hrefs [url]
  "Extract from url"
  (let [http-resp (client/get url)]
    (if (= (:status http-resp) 200)
      (extract-hrefs (:body http-resp))
      (:status http-resp))))
