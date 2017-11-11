(ns clj-line.client
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [clojure.spec.alpha :as s]
            [clj-line.registry :as reg]
            [clj-line.util :as util]))

(defn reply-message
  "https://developers.line.me/en/docs/messaging-api/reference/#send-reply-message"
  [credentials body]
  {:pre [(s/valid? ::reg/credentials credentials)
         (s/valid? ::reg/reply-request-body body)]}
  (let [{:keys [channel-access-token]} credentials]
    (util/parse-response
     (client/post "https://api.line.me/v2/bot/message/reply"
                  {:content-type :json
                   :headers {"Authorization" (str "Bearer " channel-access-token)}
                   :body (json/write-str body)}))))

(defn push-message
  "https://developers.line.me/en/docs/messaging-api/reference/#send-push-message"
  [credentials body]
  {:pre [(s/valid? ::reg/credentials credentials)
         (s/valid? ::reg/push-request-body body)]}
  (let [{:keys [channel-access-token]} credentials]
    (util/parse-response
     (client/post "https://api.line.me/v2/bot/message/push"
                  {:content-type :json
                   :headers {"Authorization" (str "Bearer " channel-access-token)}
                   :body (json/write-str body)}))))


(defn multicast-message
  "https://developers.line.me/en/docs/messaging-api/reference/#send-multicast-messages"
  [credentials body]
  {:pre [(s/valid? ::reg/credentials credentials)
         (s/valid? ::reg/multicast-request-body body)]}
  (let [{:keys [channel-access-token]} credentials]
    (util/parse-response
     (client/post "https://api.line.me/v2/bot/message/multicast"
                  {:content-type :json
                   :headers {"Authorization" (str "Bearer " channel-access-token)}
                   :body (json/write-str body)}))))

(defn get-profile
  "https://developers.line.me/en/docs/messaging-api/reference/#get-profile"
  [credentials user-id]
  {:pre [(s/valid? ::reg/credentials credentials)
         (s/valid? ::reg/not-empty-string user-id)]}
  (let [{:keys [channel-access-token]} credentials]
    (util/parse-response
     (client/get (format "https://api.line.me/v2/bot/profile/%s" user-id)
                 {:headers {"Authorization" (str "Bearer " channel-access-token)}}))))

(defn get-group-member-profile
  "https://developers.line.me/en/docs/messaging-api/reference/#get-group-member-profile"
  [credentials user-id group-id]
  {:pre [(s/valid? ::reg/credentials credentials)
         (s/valid? ::reg/not-empty-string group-id)
         (s/valid? ::reg/not-empty-string user-id)]}
  (let [{:keys [channel-access-token]} credentials]
    (util/parse-response
     (client/get (format "https://api.line.me/v2/bot/group/%s/member/%s" group-id user-id)
                 {:headers {"Authorization" (str "Bearer " channel-access-token)}}))))

(defn get-group-member-user-ids
  "https://developers.line.me/en/docs/messaging-api/reference/#get-group-member-user-ids"
  ([credentials group-id]
   (get-group-member-profile credentials group-id nil))
  ([credentials group-id start]
   {:pre [(s/valid? ::reg/credentials credentials)
          (s/valid? ::reg/not-empty-string group-id)
          (s/valid? (s/or :nil nil?
                          :not-empty-string ::reg/not-empty-string) start)]}
   (let [{:keys [channel-access-token]} credentials]
     (util/parse-response
      (client/get (str (format "https://api.line.me/v2/bot/group/%s/members/ids" group-id)
                       (when start (format "?start=%s" start)))
                  {:headers {"Authorization" (str "Bearer " channel-access-token)}})))))

(defn leave-group
  "https://developers.line.me/en/docs/messaging-api/reference/#leave-group"
  [credentials group-id]
  {:pre [(s/valid? ::reg/credentials credentials)
         (s/valid? ::reg/not-empty-string group-id)]}
  (let [{:keys [channel-access-token]} credentials]
    (util/parse-response
     (client/post (format "https://api.line.me/v2/bot/group/%s/leave" group-id)
                  {:headers {"Authorization" (str "Bearer " channel-access-token)}}))))

(defn get-room-member-profile
  "https://developers.line.me/en/docs/messaging-api/reference/#get-room-member-profile"
  [credentials user-id room-id]
  {:pre [(s/valid? ::reg/credentials credentials)
         (s/valid? ::reg/not-empty-string user-id)
         (s/valid? ::reg/not-empty-string room-id)]}
  (let [{:keys [channel-access-token]} credentials]
    (util/parse-response
     (client/post (format "https://api.line.me/v2/bot/room/%s/member/%s" room-id user-id)
                  {:headers {"Authorization" (str "Bearer " channel-access-token)}}))))

(defn get-room-member-user-ids
  "https://developers.line.me/en/docs/messaging-api/reference/#get-room-member-user-ids"
  ([credentials room-id]
   (get-room-member-user-ids credentials room-id nil))
  ([credentials room-id start]
   {:pre [(s/valid? ::reg/credentials credentials)
          (s/valid? ::reg/not-empty-string room-id)
          (s/valid? (s/or :nil nil?
                          :not-empty-string ::reg/not-empty-string) start)]}
   (let [{:keys [channel-access-token]} credentials]
     (util/parse-response
      (client/get (str (format "https://api.line.me/v2/bot/room/%s/members/ids" room-id)
                       (when start (format "?start=%s" start)))
                  {:headers {"Authorization" (str "Bearer " channel-access-token)}})))))

(defn leave-eoom
  "https://developers.line.me/en/docs/messaging-api/reference/#leave-room"
  [credentials room-id]
  {:pre [(s/valid? ::reg/credentials credentials)
         (s/valid? ::reg/not-empty-string room-id)]}
  (let [{:keys [channel-access-token]} credentials]
    (util/parse-response
     (client/post (format "https://api.line.me/v2/bot/room/%s/leave" room-id)
                  {:headers {"Authorization" (str "Bearer " channel-access-token)}}))))
