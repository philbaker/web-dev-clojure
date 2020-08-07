(defproject guestbook "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :dependencies [[ch.qos.logback/logback-classic "1.2.3"]
                 [cheshire "5.9.0"]
                 [clojure.java-time "0.3.2"]
                 [com.h2database/h2 "1.4.200"]
                 [conman "0.8.4"]
                 [cprop "0.1.15"]
                 [expound "0.8.3"]
                 [funcool/struct "1.4.0"]
                 [luminus-http-kit "0.1.6"]
                 [luminus-migrations "0.6.6"]
                 [luminus-transit "0.1.2"]
                 [luminus/ring-ttl-session "0.3.3"]
                 [markdown-clj "1.10.1"]
                 [metosin/muuntaja "0.6.6"]
                 [metosin/reitit "0.3.10"]
                 [metosin/ring-http-response "0.9.1"]
                 [mount "0.1.16"]
                 [nrepl "0.6.0"]
                 [org.clojure/clojure "1.10.1"]
                 ;
                 [cljs-ajax "0.8.0"]
                 [org.clojure/clojurescript "1.10.520" :scope "provided"]
                 [reagent "0.8.1"]
                 [re-frame "0.10.6"]
                 [com.google.javascript/closure-compiler-unshaded "v20190618" :scope "provided"]
                 [org.clojure/google-closure-library "0.0-20190213-2033d5d9" :scope "provided"]
                 [thheller/shadow-cljs "2.8.39" :scope "provided"]
                 ;
                 [org.clojure/tools.cli "0.4.2"]
                 [org.clojure/tools.logging "0.5.0"]
                 [org.webjars.npm/bulma "0.8.0"]
                 [org.webjars.npm/material-icons "0.3.1"]
                 [org.webjars/webjars-locator "0.38"]
                 [ring-webjars "0.2.0"]
                 [ring/ring-core "1.8.0"]
                 [ring/ring-defaults "0.3.2"]
                 [selmer "1.12.18"]]

  :min-lein-version "2.0.0"

  ;
  :source-paths ["src/clj" "src/cljs" "src/cljc"]
  ;
  :test-paths ["test/clj"]

  :resource-paths ["resources" "target/cljsbuild"]
  :target-path "target/%s/"
  :main ^:skip-aot guestbook.core

  :plugins []
  :clean-targets ^{:protect false} [:target-path "target/cljsbuild" ".shadow-cljs"]

  :profiles
  {:uberjar       {:omit-source    true
                   :aot            :all
                   :uberjar-name   "guestbook.jar"
                   ;
                   :source-paths   ["env/prod/clj" "env/prod/cljc" "env/prod/cljs"]
                   :prep-tasks ["compile" ["run" "-m" "shadow.cljs.devtools.cli" "release" "app"]]
                   ;
                   :resource-paths ["env/prod/resources"]}

   :dev           [:project/dev :profiles/dev]
   :test          [:project/dev :project/test :profiles/test]

   :project/dev  {:jvm-opts ["-Dconf=dev-config.edn"]
                  :dependencies [[binaryage/devtools "0.9.10"] ;; cljs-devtools
                                 [pjstadig/humane-test-output "0.10.0"]
                                 [prone "2019-07-08"]
                                 [ring/ring-devel "1.8.0"]
                                 [ring/ring-mock "0.4.0"]
                                 [day8.re-frame/re-frame-10x "0.3.3-react16"]]
                  :plugins      [[com.jakemccrary/lein-test-refresh "0.24.1"]
                                 [jonase/eastwood "0.3.5"]]
                   ;
                   :source-paths   ["env/dev/clj" "env/dev/cljc" "env/dev/cljs"]
                   ;
                  :resource-paths ["env/dev/resources"]
                  :repl-options {:init-ns user}
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]}
   :project/test {:jvm-opts ["-Dconf=test-config.edn"]
                  :resource-paths ["env/test/resources"]}
   :profiles/dev {}
   :profiles/test {}})
