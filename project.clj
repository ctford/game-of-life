(defproject game-of-life "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :profiles {:dev
             {:plugins [[lein-midje "3.0.0"]]
              :dependencies [[midje "1.5.1"]]}}
  :dependencies [[org.clojure/clojure "1.5.1"]])
