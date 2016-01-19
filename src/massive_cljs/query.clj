(ns massive-cljs.query
  (:require [clojure.string]))

(defn camel-case
  "Converts kebab-case to camelCase"
  [s]
  (clojure.string/replace s #"-(\w)" (comp clojure.string/upper-case second)))

(defmacro db-fn
  [fn-name table params]
  `(let [channel (cljs.core.async/chan)]
     (-> (deref massive-cljs.core/instance)
         (~(symbol (str ".-" (name table))))
         (~(symbol (str "." (camel-case (name fn-name))))
           (cljs.core/clj->js ~params)
           (handler channel)))
     channel))
