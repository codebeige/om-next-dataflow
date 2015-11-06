(defproject om-next-dataflow "0.1.0-SNAPSHOT"
  :description "Demo project for illustrating dataflow in Om Next"
  :url "http://codebeige.net/talks/om-next-dataflow"
  :license {:name "The MIT License"
            :url "https://opensource.org/licenses/MIT"}

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.145"]
                 [org.omcljs/om "1.0.0-alpha14"]]

  :plugins [[lein-cljsbuild "1.1.0"]
            [lein-figwheel "0.4.1"]]

  :source-paths ["src"]
  :resource-paths ["resources" "target/cljs"]

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src"]

                        :compiler {:main om-next-dataflow.core
                                   :output-to "target/cljs/public/js/main.js"
                                   :output-dir "target/cljs/public/js/lib"
                                   :asset-path "js/lib"
                                   :optimizations :none
                                   :source-map-timestamp true}

                        :figwheel {:on-jsload "om-next-dataflow.core/main"}}]}

  :figwheel {
             ;; :http-server-root "public" ;; default and assumes "resources"
             ;; :server-port 3449 ;; default
             ;; :server-ip "127.0.0.1"

             :css-dirs ["resources/public/css"] ;; watch and update CSS

             ;; Start an nREPL server into the running figwheel process
             ;; :nrepl-port 7888

             ;; Server Ring Handler (optional)
             ;; if you want to embed a ring handler into the figwheel http-kit
             ;; server, this is for simple ring servers, if this
             ;; doesn't work for you just run your own server :)
             ;; :ring-handler hello_world.server/handler

             ;; To be able to open files in your editor from the heads up display
             ;; you will need to put a script on your path.
             ;; that script will have to take a file path and a line number
             ;; ie. in  ~/bin/myfile-opener
             ;; #! /bin/sh
             ;; emacsclient -n +$2 $1
             ;;
             :open-file-command "vimopen"

             ;; if you want to disable the REPL
             ;; :repl false

             ;; to configure a different figwheel logfile path
             :server-logfile "logs/figwheel.log"
             })
