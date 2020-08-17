(defproject db-examples "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [com.layerware/hugsql "0.4.9"]
                 [org.clojure/java.jdbc "0.7.9"]
                 [org.postgresql/postgresql "42.2.6"]
                 [environ "1.2.0"]]
  :plugins [[lein-environ "1.2.0"]]
  :repl-options {:init-ns db-examples.core}
  :resource-paths ["resources"]
  :main db-examples.core/foo)

