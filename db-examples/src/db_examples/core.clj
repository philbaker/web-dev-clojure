(ns db-examples.core
 (:require [clojure.java.jdbc :as sql]
           [environ.core :refer [env]]))


(def db {:subprotocol (env :db-subprotocol)
         :subname (env :db-subname)
         :user (env :db-user)
         :password (env :db-password)})

(defn create-users-table! []
  (sql/db-do-commands db
    (sql/create-table-ddl
      :users
      [[:id "varchar(32) PRIMARY KEY"]
       [:pass "varchar(100)"]])))
; (create-users-table!)

(defn get-user [id]
  (first (sql/query db ["select * from users where id = ?" id])))
(get-user "foo") ; nil

(defn add-user! [user]
  (sql/insert! db :users user))
; (add-user! {:id "foo" :pass "bar"})
(get-user "foo") ; {:id "foo", :pass "bar"}

(defn add-users! [& users]
  (sql/insert-multi! db :users users))
; (add-users!
;   {:id "foo1" :pass "bar"}
;   {:id "foo2" :pass "bar"}
;   {:id "foo3" :pass "bar"})
(get-user "foo3") ; {:id "foo3", :pass "bar"}

; (sql/insert! db :users [:id] ["bar"])
(get-user "bar") ; {:id "bar", :pass nil}

; (sql/insert-multi! db :users [:id] [["bar1"] ["bar2"]])
(get-user "bar1") ; {:id "bar1", :pass nil}
(get-user "bar2") ; {:id "bar2", :pass nil}

(defn set-pass! [id pass]
  (sql/update!
    db
    :users
    {:pass pass}
    ["id=?" id]))

; (set-pass! "bar" "baz")

(defn remove-user! [id]
  (sql/delete! db :users ["id=?" id]))

(remove-user! "foo")
(get-user "foo") ; nil

; (sql/with-db-transaction [t-conn db]
;   (sql/update!
;     t-conn
;     :users
;     {:pass "foo"}
;     ["id=?" "bar"])
;   (sql/update!
;     t-conn
;     :users
;     {:pass "foo"}
;     ["id=?" "bar1"]))

(get-user "bar") ; {:id "bar", :pass "foo"}
(get-user "bar1") ; {:id "bar1", :pass "foo"}

;; this fails because Key (id)=(bar) already exists - the transaction
;; stops the whole command from running
; (sql/with-db-transaction [t-conn db]
;   (sql/update!
;     t-conn
;     :users
;     {:pass "ROLLBACK"}
;     ["id=?" "bar"])
;   (sql/insert!
;     t-conn
;     :users
;     {:id "bar"}))


(defn foo []
  (println "Hello world"))
