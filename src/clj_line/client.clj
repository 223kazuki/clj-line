(ns clj-line.client
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [clojure.spec.alpha :as s]
            [clj-line.registry]))

(defn push-message
  [credentials body]
  {:pre [(s/valid? :clj-line.registry/credentials credentials)
         (s/valid? :clj-line.registry/push-request-body body)]}
  (let [{:keys [channel-access-token]} credentials]
    (client/post "https://api.line.me/v2/bot/message/push"
                 {:content-type :json
                  :headers {"Authorization" (str "Bearer " channel-access-token)}
                  :body (json/write-str body)})))

(defn reply-message
  [credentials body]
  {:pre [(s/valid? :clj-line.registry/credentials credentials)
         (s/valid? :clj-line.registry/reply-request-body body)]}
  (let [{:keys [channel-access-token]} credentials]
    (client/post "https://api.line.me/v2/bot/message/reply"
                 {:content-type :json
                  :headers {"Authorization" (str "Bearer " channel-access-token)}
                  :body (json/write-str body)})))

(defn multicast-message
  [credentials body]
  {:pre [(s/valid? :clj-line.registry/credentials credentials)
         (s/valid? :clj-line.registry/multicast-request-body body)]}
  (let [{:keys [channel-access-token]} credentials]
    (client/post "https://api.line.me/v2/bot/message/multicast"
                 {:content-type :json
                  :headers {"Authorization" (str "Bearer " channel-access-token)}
                  :body (json/write-str body)})))
