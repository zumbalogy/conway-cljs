(ns conway.hello)

(defn cell
  "Returns the cell at the given coordinates"
  [x y]
  (.item (.-cells (.item (.getElementsByTagName js/document "tr") x)) y))

(defn table-size []
  (.-length (.getElementsByTagName js/document "tr")))

(defn get-cell-value [x y]
  (if (not= (.-bgColor (cell x y)) "gray")
    1
    0))

; (defn set-cell-value [x y value]
;   (let [color (if (= 0 value) "gray" "cyan")]
;     (set! (.-bgColor (cell x y)) color)))


; for speed, maybe just make an array of first 100 or so primes
; and check against that. 
; also, would make more sense as spiral sieve thing.
; also, should seed with only primes (or variation of).


(defn prime [n]
   (= 2 (reduce +
                (for [i (range 1 (inc n))]
                  (if (= 0 (mod n i)) 1 0)))))

(defn set-cell-value [x y value]
  (let [color (cond (and (< 0 value) (prime (+ x (* y 40))))
                        "white"
                    (< 0 value)
                        "cyan"
                    :else
                        "gray"
                      )]
    (set! (.-bgColor (cell x y)) color)))


; (cond (<= x 10)
;         "x is a small number"
;       (<= 11 x 100)
;         "x is a medium-sized number"
;       (<= 101 x 1000)
;         "x is a big number"
;       :else
;         "x is a REALLY big number")


(defn num-alive [nbs]
  (reduce + 0 nbs))

(defn valid-coords [board x y]
  (filter #(and (>= (first %) 0)
                (>= (last %) 0)
                (>= (- (count board) 1) (first %))
                (>= (- (count board) 1) (last %)))
          (map #(vector (+ (first %) x) (+ (last %) y))
               [[-1 -1] [-1 1] [1 -1] [1 1] [0 1] [1 0] [-1 0] [0 -1]])))

(defn apply-state [state]
  (doseq [x (range 0 (count state)) y (range 0 (count state))]
    (set-cell-value x y (get-in state [x y]))))

(defn nabes [state x y]
  (map #(get-in state [(first %) (last %)]) (valid-coords state x y)))

(defn initialize-board [n f]
  (vec (for [x (range 0 n)]
    (vec (for [y (range 0 n)] (f x y))))))

(defn random-board [n]
  (initialize-board n (fn [x y] (rand-int 2) )))

(defn zero-board [n]
  (initialize-board n (constantly 0)))

(defn current-board []
  (initialize-board (table-size) (fn [x y] (get-cell-value x y))))

(defn alive-cell-next-state
  [alive-nabes]
  (cond (> 2 alive-nabes) 0
        (= 2 alive-nabes) 1
        (= 3 alive-nabes) 1
        (< 3 alive-nabes) 0))

(defn next-state-for [state x y]
  (let [alive-cnt (num-alive (nabes state x y))]
    (if (= (get-in state [x y]) 1)
      (alive-cell-next-state alive-cnt)
      (if (= alive-cnt 3)
        1
        0))))

(defn next-tick [current-state]
  (initialize-board (count current-state) (partial next-state-for current-state)))

(defn tick []
  (apply-state (next-tick (current-board))))

(defn pb [board]
  (doseq [row board] (prn row)))

(defn setup []
  (apply-state (random-board (table-size)))
  (.setInterval js/window "conway.hello.tick()" 300))

;; Run this function when the window has loaded
(set! (.-onload js/window) setup)
