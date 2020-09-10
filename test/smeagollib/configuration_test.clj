(ns ^{:doc    "Authentication functions."
      :author "Simon Brooke"}
 smeagollib.configuration-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [me.raynes.fs :refer [delete normalized]]
            [smeagollib.configuration :refer [*config-file-path* build-config 
                                              to-keyword transform-map]])
  (:import [java.io File]))

(defn dummy-configuration-wrapper
  "Use a known, dummy configuration file for each test."
  [f]
  (let [dummy {:content-dir     "resources/public/content"
                                        ;; where content is served from.
               :default-locale  "en-GB" ;; default language used for messages
               :extensions-from :local  ;; where to load JavaScript libraries
                                        ;; from: options are :local, :remote.
               :formatters      ;; formatters for processing markdown
                                        ;; extensions.
               {:backticks {:formatter "smeagol.formatting/process-backticks"
                            :scripts   {}
                            :styles    {}}
                :geocsv    {:formatter     "smeagol.extensions.geocsv/process-geocsv"
                            :scripts       {:core      {:local "vendor/node_modules/@simon_brooke/geocsv/js/geocsv.js"}
                                            :leaflet   {:local  "vendor/node_modules/leaflet/dist/leaflet.js"
                                                        :remote "https://unpkg.com/leaflet@1.6.0/dist/leaflet.js"}
                                            :papaparse {:local  "vendor/node_modules/papaparse/papaparse.js"
                                                        :remote "https://cdnjs.cloudflare.com/ajax/libs/PapaParse/5.1.0/papaparse.min.js"}
                                            :showdown  {:local  "vendor/node_modules/showdown/bin/showdown.js"
                                                        :remote "https://cdnjs.cloudflare.com/ajax/libs/showdown/1.9.1/showdown.min.js"}}
                            :styles        {:leaflet {:local  "vendor/node_modules/leaflet/dist/leaflet.css"
                                                      :remote "https://unpkg.com/leaflet@1.6.0/dist/leaflet.css"}}
                            :icon-url-base "map-pin/"}
                :mermaid   {:formatter "smeagol.extensions.mermaid/process-mermaid"
                            :scripts   {:core {:local  "vendor/node_modules/mermaid/dist/mermaid.min.js"
                                               :remote "https://cdnjs.cloudflare.com/ajax/libs/mermaid/8.4.6/mermaid.min.js"}}}
                :pswp      {:formatter "smeagol.extensions.photoswipe/process-photoswipe"
                            :scripts   {:core {:local  "vendor/node_modules/photoswipe/dist/photoswipe.min.js"
                                               :remote "https://cdnjs.cloudflare.com/ajax/libs/photoswipe/4.1.3/photoswipe.min.js"}
                                        :ui   {:local  "vendor/node_modules/photoswipe/dist/photoswipe-ui-default.min.js"
                                               :remote "https://cdnjs.cloudflare.com/ajax/libs/photoswipe/4.1.3/photoswipe-ui-default.min.js"}}
                            :styles    {:core {:local "vendor/node_modules/photoswipe/dist/photoswipe.css"}
                                        :skin {:local "vendor/node_modules/photoswipe/dist/default-skin/default-skin.css"}}}
                :test      {:formatter "smeagol.extensions.test/process-test"}
                :vega      {:formatter "smeagol.extensions.vega/process-vega"
                            :scripts   {:core  {:remote "https://cdnjs.cloudflare.com/ajax/libs/vega/5.9.1/vega.min.js"}
                                        :lite  {:remote "https://cdnjs.cloudflare.com/ajax/libs/vega-lite/4.1.1/vega-lite.min.js"}
                                        :embed {:remote "https://cdnjs.cloudflare.com/ajax/libs/vega-embed/6.2.2/vega-embed.min.js"}}}
                :vis       {:formatter "smeagol.extensions.vega/process-vega"
                            :scripts   {:core  {:remote "https://cdnjs.cloudflare.com/ajax/libs/vega/5.9.1/vega.min.js"}
                                        :lite  {:remote "https://cdnjs.cloudflare.com/ajax/libs/vega-lite/4.1.1/vega-lite.min.js"}
                                        :embed {:remote "https://cdnjs.cloudflare.com/ajax/libs/vega-embed/6.2.2/vega-embed.min.js"}}}}
               :log-level       :info           ;; the minimum logging level; one of
                                        ;; :trace :debug :info :warn :error :fatal
               :passwd          "resources/passwd"
                                        ;; where the password file is stored
               :site-title      "Smeagol"       ;; overall title of the site, used in
                                        ;; page headings
               :start-page      "Introduction"  ;; the page shown to a visitor to the
                                        ;; root URL.
               :thumbnails      {:small   64      ;; maximum dimension of thumbnails
                                        ;; stored in the /small directory
                                 :med     400       ;; maximum dimension of thumbnails
                                        ;; stored in the /med directory
                                 :map-pin 40    ;; stored in the /map-pin directory
                         ;; you can add as many extra keys and values as
                         ;; you like here for additional sizes of images.
                         ;; Images will only be scaled if their maximum
                         ;; dimension (in pixels) is greater than the value;
                         ;; only JPEG and PNG images will be scaled.
                                 }}
        file  (File/createTempFile "test-dummy" ".cfg")]
    (spit file dummy)
    (binding [*config-file-path* (normalized file)] (f))
    (delete file)
    ))

(use-fixtures :each dummy-configuration-wrapper)

(deftest build-config-test
  ;; TODO: thid id fsiling, because `dummy-configuration-wrapper` is failing
  ;; to rebind `*config-file-path•`. The temporary file *is* being created;
  ;; that is not the problem.
  (is (= (:default-locale (build-config)) "en-GB"))
  ;; TODO: ought also to test that correct environment vars are read and
  ;; incorporated into config but unfortunately that's not easy to do in
  ;; a controlled way.
  ;; there's a blog on how to set environment values from Java here:
  ;; https://blog.sebastian-daschner.com/entries/changing_env_java
  ;; nothing I can find on doing it in Clojure
  )

(deftest transform-map-tests
  (testing "string-to-keyword"
    (is (= (:test (transform-map
                   {:test "value"} [{:from      :test
                                     :to        :test
                                     :transform to-keyword}]))
           :value)))
  (testing "keyword-to-string, plus rename"
    (is (= (:changed (transform-map
                      {:test :value}
                      [{:from      :test
                        :to        :changed
                        :transform name}])) "value"))))