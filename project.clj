(defproject smeagollib "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[clj-jgit "1.0.0"]
                 [clj-yaml "0.4.0"]
                 [clojure.java-time "0.3.2"]
                 [com.cemerick/url "0.1.1"]
                 [com.fzakaria/slf4j-timbre "0.3.7"]
                 [com.stuartsierra/component "0.4.0"]
                 [com.taoensso/encore "2.92.0"]
                 [com.taoensso/timbre "4.10.0"]
                 [com.taoensso/tower "3.0.2" :exclusions [com.taoensso/encore]]
                 [crypto-password "0.2.0"]
                 [environ "1.1.0"]
                 [hiccup "1.0.5"]
                 [im.chit/cronj "1.4.4"]
                 [image-resizer "0.1.10"]
                 [instaparse "1.4.10"]
                 [lib-noir "0.9.9" :exclusions [org.clojure/tools.reader]]
                 [markdown-clj "1.10.2"]
                 [me.raynes/fs "1.4.6"]
                 [noir-exception "0.2.5"]
                 [org.clojars.simon_brooke/internationalisation "1.0.3"]
                 [org.clojure/clojure "1.10.0"]
                 [org.clojure/core.memoize "0.5.9"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/tools.logging "0.4.0"]
                 [org.clojure/tools.trace "0.7.10"]
                 [org.slf4j/slf4j-api "1.7.25"]
                 [org.slf4j/log4j-over-slf4j "1.7.25"]
                 [org.slf4j/jul-to-slf4j "1.7.25"]
                 [org.slf4j/jcl-over-slf4j "1.7.25"]
                 [prismatic/schema "1.1.9"]
                 [prone "1.1.4"]
                 ;;[ring/ring-anti-forgery "1.1.0"]
                 ;;[ring-server "0.4.0"]
                 [selmer "1.11.0"]]
  :repl-options {:init-ns smeagollib.core})