(ns nqueens.util
  (:require ))

(defonce empty-board nil)

(defn adjoin-position
  [row col rst]
  (cons [row col] rst))

(defn safe?
  [positions]
  (let [[x y] (first positions)
        rst (rest positions)]
    (reduce #(and % %2) true
            (map #(let [cx (first %)
                        cy (second %)]
                    (not (or (= cx x)
                             (= (- cx x) (- cy y))
                             (= (- cx x) (- y cy)))))
                 rst))))

(defn queens
  [board-size]
  ((fn queen-cols [k]
     (if (= k 0) (list empty-board)
         (filter
          safe?
          (mapcat
           (fn [rest-of-queens]
             (map #(adjoin-position % k rest-of-queens)
                  (range 1 (inc board-size))))
           (queen-cols (dec k)))))) board-size))
