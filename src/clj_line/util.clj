(ns clj-line.util
  (:require [clojure.data.json :as json]))

(defn parse-response
  [response]
  (some-> response
          :body
          (json/read-str :key-fn keyword)))
