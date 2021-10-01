(ns nqueens.core
  (:require [nqueens.util :refer [queens]]
            [reagent.core :as reagent]
            [reagent.dom :refer [render]]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom nil))

(defonce temp-state (atom nil))

(defonce counter (atom 0))

(defonce canvas (.getElementById js/document "canvas"))

(defonce image (.getElementById js/document "source"))

(defonce ctx (.getContext canvas "2d"))

(defonce canvas-width js/window.innerWidth)

(defonce board-size (atom 8))

(defonce tile-size 40)

(defonce row-volume (atom 0))

(defn set-row-volume! [size]
  (swap! row-volume
         (fn [_]
           (let [len (count @temp-state)
                 board-width (* tile-size size)
                 guess (quot canvas-width board-width)]
             (cond (> guess len) len
                   (> (- canvas-width (* guess board-width)) (* (dec guess) 10)) guess
                   :else (dec guess))))))

(defonce offset (atom 0))

(defn set-offset! [size row-volume]
  (swap! offset
         (fn []
           (/ (- canvas-width (+ (* row-volume (* tile-size size)) (* 10 (dec row-volume)))) 2))))

(.setTransform ctx 1, 0, 0, 1, 0.5, 0.5)

(defn draw-tile! [x y color]
  (.setTransform ctx 1, 0, 0, 1, 0.5, 0.5)

  (.beginPath ctx)
  (.rect ctx x y tile-size tile-size)

  (set! (.-fillStyle ctx) color)
  (.fill ctx)

  (set! (.-lineWidth ctx) 0.5)
  (set! (.-strokeStyle ctx) "black")
  (.stroke ctx))

(defn tile-color [x y]
  (if (= (even? x) (even? y))
    "yellow"
    "darkgray"))

(defn draw-board! [px py w h]
;;  (set! (.-height (dom/getElement "canvas")) (+ 1 (* h tile-size)))
;;  (set! (.-width (dom/getElement "canvas")) (+ 1 (* w tile-size)))

  (mapv
	 (fn [y]
  	 (mapv
 	    (fn [x] (draw-tile! (+ px (* tile-size x)) (+ py (* tile-size y)) (tile-color x y)))
	   	(range 0 w)))
	 (range 0 h)))

(set! (.-globalAlpha ctx) 0.5)

(swap! app-state (fn [_] (queens @board-size)))

(defn draw-queens! [xorigin yorigin queens]
  (doseq [[x y] queens]
    (.drawImage ctx image
                (+ xorigin (* (dec x) tile-size))(+ yorigin (* (dec y) tile-size)) tile-size tile-size)))

(defn draw-merged! [x y queens]
  (draw-queens! x y queens)
  (draw-board! x y @board-size @board-size))

(defn display-result []
  (.clearRect ctx 0 0 (.-width canvas) (.-height canvas))
  (swap! app-state (fn [_] (apply vector @temp-state)))
  (if (zero? (count @app-state)) (js/alert "No arrangement meets the requirement")
      (doseq [i (range (count @app-state))]
        (let [row (quot i @row-volume)
              col (mod i @row-volume)]
          (draw-merged! (+ @offset (+ (* (* tile-size @board-size) col) (* 10 col)))
                        (+ (* (* tile-size @board-size) row) (* 10 row)) (first @app-state))
          (swap! app-state rest)
          (swap! counter inc)))))

(defn reset-app-state! [val]
  (let [size (-> val .-target .-value int)]
    (swap! counter (fn [_] 0))
    (swap! board-size (fn [_] size))
    (swap! temp-state (fn [_] (queens @board-size)))
    (set-row-volume! @board-size)
    (set-offset! @board-size @row-volume)
    (display-result)))

#_(js/setInterval (fn []
                  (if (empty? @app-state) nil
                      (let [row (quot @counter @row-volume)
                            col (mod @counter @row-volume)]
                        (draw-merged! (+ @offset (+ (* (* tile-size @board-size) col) (* 10 col)))
                                      (+ (* (* tile-size @board-size) row) (* 10 row)) (first @app-state))
                        (swap! app-state rest)
                        (swap! counter inc)))) 100)

;;(draw-board! 0 0 board-size board-size)

(defn operation-area []
  [:div {:align :center}
   [:div {:border :1px}
    [:label "Board size "]
    [:input.form-control {:type :text :placeholder "8" :on-change #(reset-app-state! %)}]
;    [:button {:on-click display-result} "Done"]
    ]])

(defn mount-component []
  (render [#'operation-area] (.getElementById js/document "operation")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  (swap! app-state (fn [_] (queens @board-size)))
  (swap! counter (fn [_] 0))
)

(defn init! []
  (mount-component))

(init!)
