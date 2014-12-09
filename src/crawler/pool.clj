(ns crawler.pool)

(def ^:dynamic futures [])

(defn wait-for-all-done []
  "Wait for all stream to finish"
  (while (not (every? #(future-done? %) futures)) ()))

(defn async [action]
  (def futures 
      (conj futures 
        (future (action)))))

(defn sync-wise [action]
  (def futures [])
  (action)
  (wait-for-all-done))
