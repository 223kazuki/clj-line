(ns clj-line.oauth
  (:require [clj-line.registry :as reg]
            [clojure.spec.alpha :as s]
            [clj-http.client :as client]
            [clojure.data.json :as json]
            [clj-line.util :as util]))

(defn issue-channel-access-token
  [body]
  {:pre [(s/valid? ::reg/issue-channel-access-token-request-body body)]}
  (util/parse-response
   (client/post "https://api.line.me/v2/oauth/accessToken"
                {:headers {"Content-Type" "application/x-www-form-urlencoded"}
                 :form-params body})))

(defn revoke-channel-access-token
  [body]
  {:pre [(s/valid? ::reg/revoke-channel-access-token-request-body body)]}
  (client/post "https://api.line.me/v2/oauth/revoke"
               {:headers {"Content-Type" "application/x-www-form-urlencoded"}
                :form-params body}))
