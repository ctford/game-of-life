(ns game-of-life.core
  (:require [clojure.set :refer [union]]))

(defn neighbours-of
  "All adjacent cells to [x y], not including [x y] itself."
  [[x y]]
  (set
    (for [dx [-1 0 1]
          dy [-1 0 1]
          :when (not= [dx dy] [0 0])]
      [(+ x dx) (+ y dy)])))

(defn descends-from?
  "Whether cell is alive in the next generation of grid."
  [grid cell]
  (let [crowding (->> (neighbours-of cell) (filter grid) count)]
    (or (= crowding 3)
        (and (grid cell) (= crowding 2)))))

(defn generate
  "The next generation of grid."
  [grid]
  (let [candidates (->> grid (map neighbours-of) (cons grid) (reduce union))]
    (->> candidates (filter (partial descends-from? grid)) set)))

(defn generation
  "What grid will become in n generations."
  [n grid]
  (nth (iterate generate grid) n))
