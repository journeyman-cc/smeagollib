(ns ^{:doc "Find (by doing a 302 redirect to) appropriate files; if no
      appropriate file is found return a 302 redirect to a default file."
      :author "Simon Brooke"}
  smeagollib.finder
  (:require [clojure.string :as cs]
            [me.raynes.fs :as fs]
            [ring.util.mime-type :refer [ext-mime-type]]
            [ring.util.response :as response]
            [smeagollib.util :refer [local-url-base content-dir]]
            [taoensso.timbre :as log]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; Smeagol: a very simple Wiki engine.
;;;;
;;;; This program is free software; you can redistribute it and/or
;;;; modify it under the terms of the GNU General Public License
;;;; as published by the Free Software Foundation; either version 2
;;;; of the License, or (at your option) any later version.
;;;;
;;;; This program is distributed in the hope that it will be useful,
;;;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;;;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;;;; GNU General Public License for more details.
;;;;
;;;; You should have received a copy of the GNU General Public License
;;;; along with this program; if not, write to the Free Software
;;;; Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
;;;; USA.
;;;;
;;;; Copyright (C) 2017 Simon Brooke
;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; See:
;; https://github.com/weavejester/compojure/wiki/Routes-In-Detail
;; https://github.com/weavejester/compojure/wiki/Destructuring-Syntax

(defn to-url
  "Given the absolute file path `fqn`, return the relative URL to that path
  within Smeagol, if any, else `nil`."
  [fqn]
  (let [f (when fqn (str fqn))
        l (str local-url-base)
        c (str content-dir)]
    (cond
      (nil? f) nil
      (cs/starts-with? f l) (subs f (count l))
      ;; content-dir may not be within local-url-base
      ;; TODO: potential bad bug: check that when uploads isn't within local-url-base
      ;; the right copies of files are actually getting served!
      (cs/starts-with? f c) (str "content/" (subs f (count c))))))


(defn find-file-on-path
  "Find a file with a name like this `n` on this `path` with
  one of these `extensions`. Question: should we recurse down
  the hierarchy?"
  [n path extensions]
  (let [ext (fs/extension n)
        basename (subs n 0 (- (count n) (count ext)))
        fqn (fs/absolute (fs/file path n))]
    (if (and (fs/exists? fqn) (fs/readable? fqn))
      fqn
      (first
        (remove
          nil?
          (map
            #(let [fqn' (fs/absolute (fs/file path (str basename %)))]
               (when (and (fs/exists? fqn') (fs/readable? fqn'))
                 fqn'))
            extensions))))))


(defn find-file-on-paths
  "Find a file with a name like this `n` on one of these `paths` with
  one of these `extensions`"
  [n paths extensions]
  (first
    (remove
      nil?
      (map
        #(find-file-on-path n % extensions)
        paths))))


(defn with-mime-type-for-file
  [response file]
  (assoc-in
    response
    [:headers "Content-Type"]
    (ext-mime-type (str file))))


(defn find-image
  "Return the first image file found on these `paths` with this
  `requested-name`, if available; this `default-file` otherwise."
  [requested-name default-file paths]
  (let [file (find-file-on-paths requested-name paths
                                 [".gif" ".png" ".jpg" ".jpeg" ".svg"])
        s (if file (str file) default-file)]
    (if file
      (log/info "Found image" requested-name "at" s)
      (log/warn "Failed to find image matching" requested-name))
    (with-mime-type-for-file
      (response/file-response s)
      s)))
