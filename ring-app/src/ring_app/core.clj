(ns ring-app.core
  (:require 
    [reitit.ring :as reitit]
    [muuntaja.middleware :as muuntaja]
    [ring.adapter.jetty :as jetty]
    [ring.util.http-response :as response]
    [ring.middleware.reload :refer [wrap-reload]]))

(defn wrap-nocache [handler]
  (fn [request]
    (-> request
        handler
        (assoc-in [:headers "Pragma"] "no-cache"))))

(defn html-handler [request-map]
  (response/ok
    (str "<html><body> your IP is: "
         (:remote-addr request-map)
         "</body></html>")))

(defn json-handler [request]
  (response/ok
    {:result (get-in request [:body-params :id])}))

(defn wrap-formats [handler]
  (-> handler
      (muuntaja/wrap-format)))

(defn response-handler [request]
  (response/ok
    (str "<html><body> your IP is: "
         (:remote-addr request)
         "</body></html")))

(def routes
  [["/" {:get response-handler
         :post response-handler}]
   ["/echo/:id"
    {:get
     (fn [{{:keys [id]} :path-params}]
       (response/ok (str "<p>the value is: " id "</p>")))}]
   ["/api"
    {:middleware [wrap-formats]}
    ["/multiply"
     {:post
      (fn [{{:keys [a b]} :body-params}]
        (response/ok {:result (* a b)}))}]]])

(def handler
  (reitit/routes
    (reitit/ring-handler
      (reitit/router routes))
    (reitit/create-resource-handler
      {:path "/"})
    (reitit/create-default-handler
      {:not-found
       (constantly (response/not-found "404 - page not found"))
       :method-not-allowed
       (constantly (response/method-not-allowed "405 - not allowed"))
       :not-acceptable
       (constantly (response/not-acceptable "406 - not acceptable"))})))

(defn -main []
  (jetty/run-jetty
    (-> #'handler wrap-nocache wrap-reload) 
    {:port 3000
     :join? false}))
