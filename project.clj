(defproject massive-cljs "0.2.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :profiles
  {:default [:cljs-shared]

   :cljs-shared
   {:dependencies [[org.clojure/clojure "1.9.0"]
                   [org.clojure/clojurescript "1.9.946"]]

    :plugins [[lein-cljsbuild "1.1.7"]]

    :cljsbuild
    {:builds [{:id "default"
               :source-paths ["src"]
               :compiler
               {:target :nodejs
                :output-to "index.js"
                :optimizations :simple
                :language-in :ecmascript5
                :language-out :ecmascript5}}]}}})

