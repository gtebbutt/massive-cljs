(ns massive-cljs.query
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [massive-cljs.query])
  (:require [cljs.core.async :refer [>! chan close!]]
            [massive-cljs.core :refer [instance parse]]))

(defn handler
  [channel]
  (fn [err results]
    (let [return {:error? (not (empty? err))}]
      (go (>! channel
              (if (empty? err)
                (assoc return :content (parse results :keywordize-keys true))
                (assoc return :msg err)))
          (close! channel)))
    ))

(defn run
  ([query]
   (run query []))
  ([query params]
   (let [channel (chan)]
     (.run @instance
           query
           (clj->js params)
           (handler channel))
     channel)))
