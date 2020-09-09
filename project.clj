(defproject smeagollib "0.1.0-SNAPSHOT"
  :cloverage {:output "docs/cloverage"}
  :codox {:metadata {:doc "**TODO**: write docs"
                     :doc/format :markdown}
          :output-path "docs/codox"
          :source-uri "https://github.com/journeyman-cc/smeagollib/blob/master/{filepath}#L{line}"}
  :dependencies [[clj-jgit "1.0.0"]
                 [clj-yaml "0.4.0"]
                 [clojure.java-time "0.3.2"]
                 [com.cemerick/url "0.1.1"]
                 [com.fzakaria/slf4j-timbre "0.3.19"]
                 [com.stuartsierra/component "1.0.0"]
                 [com.taoensso/encore "2.127.0"]
                 [com.taoensso/timbre "4.10.0"]
                 [com.taoensso/tower "3.0.2" :exclusions [com.taoensso/encore]]
                 [crypto-password "0.2.1"]
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
                 [org.clojure/clojure "1.10.1"]
                 [org.clojure/core.memoize "1.0.236"]
                 [org.clojure/data.json "1.0.0"]
                 [org.clojure/tools.logging "1.1.0"]
                 [org.clojure/tools.trace "0.7.10"]
                 [org.slf4j/slf4j-api "1.7.30"]
                 [org.slf4j/log4j-over-slf4j "1.7.30"]
                 [org.slf4j/jul-to-slf4j "1.7.30"]
                 [org.slf4j/jcl-over-slf4j "1.7.30"]
                 [prismatic/schema "1.1.12"]
                 [prone "2020-01-17"]
                 [selmer "1.12.28"]]
  :description "The working parts of the Smeagol Wiki engine, stripped out as a library."
  :license {:name "GNU General Public License,version 2.0 or (at your option) any later version"
            :url "https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html"}
  :plugins [[lein-cloverage "1.1.2"]
            [lein-codox "0.10.7"]]
  :url "https://github.com/journeyman-cc/smeagollib")
