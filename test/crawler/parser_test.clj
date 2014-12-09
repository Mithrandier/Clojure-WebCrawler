(ns crawler.parser-test
  (:require [clojure.test :refer :all]
            [crawler.parser :refer :all]))

(def html-sample "<html><head/><body><a href=\"http://172.0.0.1\"/><body><html/>")

(deftest parse-html-test
  (is (= :document (:type (parse-html html-sample)))))

(deftest extract-hrefs-test
  (is (= '("http://172.0.0.1") (extract-hrefs html-sample))))