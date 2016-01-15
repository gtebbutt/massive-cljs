(ns massive-cljs.query
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [>! chan close!]]
            [massive-cljs.core :refer [instance parse]]))

(defn run
  ([query]
   (run query []))
  ([query params]
   (let [channel (chan)]
     (.run @instance
           query
           (clj->js params)
           (fn [err results]
             (let [return {:error? (not (empty? err))}]
               (go (>! channel
                       (if (empty? err)
                         (assoc return :content (parse results))
                         (assoc return :msg err)))
                   (close! channel)))
             ))
     channel)))