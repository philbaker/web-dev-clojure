(ns db-examples.hugsql
  (:require [db-examples.core :refer [db]]
            [clojure.java.jdbc :as sql]
            [hugsql.core :as hugsql]))

(hugsql/def-db-fns "users.sql")

(add-user! db {:id "hug2" :pass "sql"})
(add-user-returning! db {:id "hug4" :pass "sql"})
(add-users! db {:users [["bob" "Bob"] ["alice" "Alice"]]})
(declare find-user)
(find-user db {:id "bob"})
(find-user db {:id "iona"})
(declare find-users)
(find-users db {:ids ["alice" "bob" "nobody"]})

(defn add-user-transaction [user]
  (sql/with-db-transaction [t-conn db]
    (if-not (find-user t-conn {:id (:id user)})
      (add-user! t-conn user))))

(add-user-transaction {:id "cheryl" :pass "Cheryl"})
(add-user-transaction {:id "alice" :pass "Alice"})
