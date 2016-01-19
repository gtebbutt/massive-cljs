(ns massive-cljs.query
  (:require [clojure.string]))

(defn camel-case
  "Converts kebab-case to camelCase"
  [s]
  (clojure.string/replace s #"-(\w)" (comp clojure.string/upper-case second)))

(defmacro db-fn
  ([fn-name params]
   ;Hidden macro arguments &form &env must be passed explicitly
   (db-fn &form &env fn-name nil params))
  ([fn-name table params]
   `(let [channel# (cljs.core.async/chan)]
      (-> (deref massive-cljs.core/instance)
          (~(if table (symbol (str ".-" (name table))) 'identity))
          (~(symbol (str "." (camel-case (name fn-name))))
            (cljs.core/clj->js ~params)
            (handler channel#)))
      channel#)))
