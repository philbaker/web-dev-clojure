(ns html-templating.core
  (:require [selmer.parser :as selmer]
            [selmer.filters :as filters]
            [selmer.middleware :refer [wrap-error-page]]))

(defn renderer []
  (wrap-error-page
    (fn [template]
      {:status 200
       :body (selmer/render-file template {})})))

((renderer) "hello.html")
((renderer) "error.html")

(selmer/render "Hello, {{name}}" {:name "World"})

;; Define a custom tag
(selmer/add-tag!
  :image
  (fn [args context-map]
    (str "<img src=" (first args) "/>")))

(selmer/render "{% image \"http://placekitten.com/200/300\" %}" {})

(selmer/add-tag!
  :uppercase
  (fn [args context-map block]
    (.toUpperCase (get-in block [:uppercase :content])))
  :enduppercase)

(selmer/render
  "{% uppercase %}foo {{bar}} baz{% enduppercase %}"
  {:bar "injected"})
