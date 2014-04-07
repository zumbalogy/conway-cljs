(ns conway.views
  (:require
    [hiccup
      [page :refer [html5]]
      [page :refer [include-js]]]))

(defn square-board
  [size]
  (vec (into [:table]
             (replicate size (vec
                              (into [:tr] 
                                    (replicate size [:td "&nbsp;"])))))))

(defn index-page []
  (html5
    [:head
     [:style " 
        h1 {
          text-align: center; 
          color: white;
          font-family: mono;
        } 
        table {
          margin: auto; 
        } 
        td {
          width: 6px; 
          font-size: 6px;
          padding: 0;
        }
        body {
          background-color: #222;
        }
     "]
     [:title "Conway's Game of Life"]
     (include-js "/js/main.js")]
    [:body
      [:h1 "Conway's Game of Life"]
     (square-board 70)]))
