(defproject massive-cljs "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :profiles
  {:default [:cljs-shared]

   :cljs-shared
   {:dependencies [[org.clojure/clojure "1.7.0"]
                   [org.clojure/clojurescript "1.7.48"]]

    :plugins [[lein-cljsbuild "1.1.0"]]

    :cljsbuild
    {:builds [{:id "default"
               :source-paths ["src"]
               :compiler
               {:target :nodejs
                :output-to "index.js"
                :optimizations :simple
                :language-in :ecmascript5
                :language-out :ecmascript5}}]}}})

