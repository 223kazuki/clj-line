(ns clj-line.registry
  (:require [clojure.spec.alpha :as s]))

(defn regist-specs []
  ;; common
  (s/def ::not-empty-string (s/and string? #(not (empty? %))))
  (s/def ::http-url #(re-matches #"http://[\w/:%#\$&\?\(\)~\.=\+\-]+" %))
  (s/def ::https-url #(re-matches #"https://[\w/:%#\$&\?\(\)~\.=\+\-]+" %))
  (s/def :clj-line.registry.baseSize/width #(= 1040 %))
  (s/def :clj-line.registry.baseSize/height int?)

  ;; values
  (s/def ::text (s/and ::not-empty-string #(>= 2000 (count %))))
  (s/def ::originalContentUrl ::https-url)
  (s/def ::previewImageUrl ::https-url)
  (s/def ::duration pos-int?)
  (s/def ::title (s/and ::not-empty-string #(>= 100 (count %))))
  (s/def ::address (s/and ::not-empty-string #(>= 100 (count %))))
  (s/def ::latitude (s/and number? #(<= -90 % 90)))
  (s/def ::longitude (s/and number? #(<= -360 % 360)))
  (s/def ::packageId ::not-empty-string)
  (s/def ::stickerId ::not-empty-string)
  (s/def ::replyToken ::not-empty-string)
  (s/def :clj-line.registry.push/to ::not-empty-string)
  (s/def :clj-line.registry.multicast/to (s/coll-of ::not-empty-string))
  (s/def ::channel-access-token ::not-empty-string)
  (s/def ::baseUrl ::https-url)
  (s/def ::altText (s/and ::not-empty-string #(>= 400 (cont %))))
  (s/def ::baseSize (s/keys :req-un [::width ::height]))
  (s/def ::link-uri (s/or ::http-url ::https-url))
  (s/def ::x number?)
  (s/def ::y number?)
  (s/def ::width number?)
  (s/def ::height number?)
  (s/def ::area (s/keys [::x ::y ::width ::height]))

  ;; imagemap actions
  (s/def ::uri-action (s/and
                       #(= (:type %) "uri")
                       (s/keys :req-un [::type ::link-uri ::area])))
  (s/def ::message-action (s/and
                           #(= (:type %) "message")
                           (s/keys :req-un [::type ::text ::area])))
  (s/def ::imagemap-action (s/or :uri-action ::uri-action
                                 :message-action ::message-action))
  (s/def ::actions (s/coll-of ::imagemap-action))

  ;; messages
  (s/def ::text-message (s/and
                         #(= (:type %) "text")
                         (s/keys :req-un [::type ::text])))
  (s/def ::image-message (s/and
                          #(= (:type %) "image")
                          (s/keys :req-un [::type ::originalContentUrl ::previewImageUrl])))
  (s/def ::video-message (s/and
                          #(= (:type %) "video")
                          (s/keys :req-un [::type ::originalContentUrl ::previewImageUrl])))
  (s/def ::audio-message (s/and
                          #(= (:type %) "audio")
                          (s/keys :req-un [::type ::originalContentUrl ::duration])))
  (s/def ::location-message (s/and
                             #(= (:type %) "location")
                             (s/keys :req-un [::type ::title ::address ::latitude ::longitude])))
  (s/def ::sticker-message (s/and
                            #(= (:type %) "sticker")
                            (s/keys :req-un [::type ::packageId ::stickerId])))
  (s/def ::imagemap-message (s/and
                             #(= (:type %) "imagemap")
                             (s/keys :req-un [::type ::baseUrl ::altText ::baseSize ::actions])))
  (s/def ::message (s/or :text-message ::text-message
                         :image-messaage ::image-message
                         :video-message ::video-message
                         :audio-message ::audio-message
                         :location-message ::location-message
                         :sticker-message ::sticker-message))
  (s/def ::messages (s/coll-of ::message))

  (s/def ::credentials
    (s/keys :req-un [::channel-access-token]))
  (s/def ::push-request-body
    (s/keys :req-un [:clj-line.registry.push/to ::messages]))
  (s/def ::reply-request-body
    (s/keys :req-un [::replyToken ::messages]))
  (s/def ::multicast-request-body
    (s/keys :req-un [:clj-line.registry.multicast/to ::messages])))

(regist-specs)
