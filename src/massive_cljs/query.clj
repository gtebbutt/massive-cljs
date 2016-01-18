(ns massive-cljs.query)

(defmacro db-fn
  [fn-name table params]
  `(let [channel (cljs.core.async/chan)]
     (~(symbol (str "." (name fn-name))) (. (deref massive-cljs.core/instance) ~(symbol (str "-" (name table))))
       (cljs.core/clj->js ~params)
       (handler channel))
     channel))
