(ns ^{:doc "Authentication functions."
      :author "Simon Brooke"}
 smeagollib.authenticate-test
  (:require [clojure.test :refer :all]
            [me.raynes.fs :refer [delete normalized]]
            [smeagollib.authenticate :refer [*password-file-path* add-user
                                             authenticate change-pass
                                             delete-user evaluate-password
                                             fetch-user-details
                                             get-admin get-email list-users]])
  (:import [java.io File]))

(defn dummy-passwords-wrapper
  "Use a known, dummy password file for each test."
  [f]
  (let [dummy {:admin {:admin true :password "admin"}
               :test {:email "test@journeyman.cc", :admin false
                      :password "$s0$f0801$DxFJImDpM0FgAZN89+UNxg==$+Ec4hV6zZYttKolYlw1b+tCpnhpLcziCzz1MECF2HoA="}}
        file (File/createTempFile "test-dummy" ".pass")]
    (spit file dummy)
    (binding [*password-file-path* (normalized file)] (f))
    (delete file)))

(use-fixtures :each dummy-passwords-wrapper)

(deftest add-user-test
  (is (thrown? Exception (add-user :not-a-string "test1@journeyman.cc" false)))
  (is (thrown? Exception (add-user "" "test1@journeyman.cc" false)))
  (is (nil? (fetch-user-details "test1")))
  (is (add-user "test1" "froboz001" "test1@journeyman.cc" false))
  (is (fetch-user-details "test1"))
  (is (not (get-admin "test1")))
  (is (nil? (fetch-user-details "test2")))
  (is (add-user "test2" "froboz002" "test2@journeyman.cc" "true"))
  (is (fetch-user-details "test2"))
  (is (get-admin "test2"))
  (is (add-user "test3" nil "test3@journeyman.cc" false)))

(deftest authenticate-test
  (testing "User authentication"
    (is (authenticate "admin" "admin")
        "Unencrypted passwords should authenticate, for bootstrapping.")
    (is (authenticate "test" "testtest")
        "Encrypted passwords should also authenticate!")
    (is (not (authenticate "froboz" "froboz"))
        "Users not present in the file should not authenticate.")
    (is (thrown? Exception (authenticate "admin" "wrongpassword"))
        "Wrong passwords should not authenticate.")
    (is (not (authenticate "test" "random nonsense"))
        "Wrong passwords should not authenticate.")))

(deftest change-pass-test
  (is (not (change-pass "non-existent-user" "anything" "nothing")) "Can't change password if we don't have a user.")
  (is (authenticate "test" "testtest") "Before change of password.")
  (is (change-pass "test" "testtest" "froboz001"))
  (is (not (authenticate "test" "testtest")) "After change of password.")
  (is (authenticate "test" "froboz001")  "After change of password."))

(deftest delete-user-test
  (is (:test (set (map keyword (list-users)))) 
      "Before deletion: test user exists")
  (is (delete-user "test"))
  (is (not (:test (set (map keyword (list-users)))))
      "After deletion: test user no longer exists.")
  )

(deftest evaluate-password-test
  (is (= (evaluate-password "testtest" "froboz001") :chpass-bad-match))
  (is (= (evaluate-password "test" "test") :chpass-too-short))
  (is (evaluate-password "testtest" "testtest"))
  (is (= (evaluate-password "test") :chpass-too-short))
  (is (evaluate-password "froboz001")))

(deftest get-admin-test
  (is (get-admin :admin))
  (is (get-admin "admin"))
  (is (not (get-admin :test)))
  (is (not (get-admin "test")))
  (is (not (get-admin "froboz"))))

(deftest get-email-test
  (is (= (get-email "test") "test@journeyman.cc"))
  (is (not (get-email "admin"))
      "Admin user doesn't have an email address in dummy file.")
  (is (not (get-email "froboz"))))

(deftest fetch-user-test
  (is (= (fetch-user-details "admin") {:admin true :password "admin"}))
  (is (= (fetch-user-details :admin) {:admin true :password "admin"}))
  (is (nil? (fetch-user-details "froboz")))
  (is (nil? (fetch-user-details "")))
  (is (nil? (fetch-user-details nil))))

(deftest list-users-test
  (is (= (list-users) '("admin" "test"))))