(ns massive-cljs.core)

(defonce instance (atom nil))

(def db instance)

(defn massive-lib
  []
  (try
    (js/require "massive")
    (catch js/Object e nil)))

(defn connect!
  [uri]
  (if-let [lib (massive-lib)]
    (.connectSync lib #js {:connectionString uri})
    (throw (js/Error. "massive-js library not found"))))

(defn init!
  [uri]
  (reset! instance (connect! uri)))

(defn parse
  ([x] (parse x {:keywordize-keys false}))
  ([x & opts]
   (let [{:keys [keywordize-keys]} opts
         keyfn (if keywordize-keys keyword str)
         f (fn thisfn [x]
             (cond
              (satisfies? IEncodeClojure x)
              (-js->clj x (apply array-map opts))

              (seq? x)
              (doall (map thisfn x))

              (coll? x)
              (into (empty x) (map thisfn x))

              (array? x)
              (vec (map thisfn x))

              (instance? js/Date x)
              x

              (= (goog/typeOf x) "object")
              (into {} (for [k (js-keys x)]
                         [(keyfn k) (thisfn (aget x k))]))

              :else x))]
     (f x))))
