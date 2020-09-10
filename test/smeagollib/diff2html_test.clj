(ns ^{:doc    "Authentication functions."
      :author "Simon Brooke"}
 smeagollib.diff2html-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [smeagollib.diff2html :refer [mung-line diff2html]])
  (:import [java.io File]))

(def side-bar-changes "@@ -1,3 +1,12 @@
-This is the side bar. There's nothing in it yet. You could [edit](edit?page=_side-bar) it to provide internal navigation or branding.
+* [[Introduction]]^M
+* [[Change log]]^M
+* [[User Documentation]]^M
+* [[Configuring Smeagol]]^M
+* [[Deploying Smeagol]]^M
+* [[Developing Smeagol]]^M
 
-If you don't like it on the left, float it to the right (or do something entirely different) by editing the [stylesheet](/edit-css?page=stylesheet).
+<hr/>^M
+^M
+This is the side bar. You could [edit](edit?page=_side-bar) it to provide internal navigation or branding.^M
+^M
+If you don't like it on the left, float it to the right (or do something entirely different) by editing the [stylesheet](edit-css?page=stylesheet).^M")

(def htmlified-changes "<div class='change'><p><ins>* [[Configuring Smeagol]]^M</ins></p>\n<p><ins>* [[Deploying Smeagol]]^M</ins></p>\n<p><ins>* [[Developing Smeagol]]^M</ins></p>\n<p> </p>\n<p><del>If you don't like it on the left, float it to the right (or do something entirely different) by editing the [stylesheet](/edit-css?page=stylesheet).</del></p>\n<p><ins><hr/>^M</ins></p>\n<p><ins>^M</ins></p>\n<p><ins>This is the side bar. You could [edit](edit?page=_side-bar) it to provide internal navigation or branding.^M</ins></p>\n<p><ins>^M</ins></p>\n<p><ins>If you don't like it on the left, float it to the right (or do something entirely different) by editing the [stylesheet](edit-css?page=stylesheet).^M</ins></p></div>")

(deftest mung-line-test
  (is (= (mung-line "+ test line") "<p><ins> test line</ins></p>"))
  (is (= (mung-line "- test line") "<p><del> test line</del></p>"))
  (is (= (mung-line "@@ -1,20 +1,20 @@") "</div><div class='change'>"))
  (is (= (mung-line "\\ test line") "<p class='warn'> test line</p>"))
  (is (= (mung-line " the quick brown fox jumped over the lazy dog") 
         "<p> the quick brown fox jumped over the lazy dog</p>"))
  (is (= (mung-line "") "<p></p>"))
  )

(deftest diff2html-test
  (is (= (diff2html side-bar-changes) htmlified-changes)))
