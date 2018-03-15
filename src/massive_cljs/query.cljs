(ns massive-cljs.query
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [massive-cljs.query])
  (:require [cljs.core.async :refer [>! chan close!]]
            [massive-cljs.core :refer [instance parse]]))

(defn handler
  [prom channel]
  (-> prom
      (.then (fn [results]
               (go (>! channel {:error? false
                                :content (parse results :keywordize-keys true)})
                   (close! channel))))
      (.catch (fn [err]
                (go (>! channel {:error? true
                                 :msg err})
                    (close! channel))))))

(defn run
  ([query]
   (run query []))
  ([query params]
   (let [channel (chan)]
     (-> @instance
         (.query query (clj->js params))
         (handler channel))
     channel)))
