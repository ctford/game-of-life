(ns game-of-life.core-test
  (:require [midje.sweet :refer :all]
            [clojure.set :refer [union]]
            [game-of-life.core :refer [neighbours-of descends-from? generation]]))

(defn subsets [original]
  (reduce
    (fn [so-far element] (union so-far (map #(conj % element) so-far)))
    #{#{}}
    original))

(fact "The neighbours of a cell are all of the adjacent cells."
  (neighbours-of [1 1]) => #{[0 2] [1 2] [2 2]
                             [0 1]  , ,  [2 1]
                             [0 0] [1 0] [2 0]})

(fact "Live cells survive if they have two or three neighbours."
  (descends-from? #{[0 0] [1 0] [2 0]} [1 0]) => truthy 

  (descends-from? #{ , ,  [1 1]  , ,
                    [0 0] [1 0] [2 0]} [1 0]) => truthy

  (for [neighbours (->> (neighbours-of [1 1]) subsets (filter (comp #{2 3} count)))]
    (descends-from? (conj neighbours [1 1]) [1 1])) =>  (has every? truthy))

(fact "Live cells die if they have any other number of neighbours."
  (descends-from? #{ , ,  [1 2]  , ,
                    [0 1] [1 1] [2 1]
                     , ,  [1 0]  , ,} [1 1]) => falsey

  (for [neighbours (->> (neighbours-of [1 1]) subsets (filter (comp not #{2 3} count)))]
    (descends-from? (conj neighbours [1 1]) [1 1])) => (has every? falsey))

(fact "Dead cells sponteneously generate if they have three neighbours."
  (descends-from? #{ , ,  [1 1]  , ,
                    [0 0]  , ,  [2 0]} [1 0]) => truthy

  (for [neighbours (->> (neighbours-of [1 1]) subsets (filter (comp #{3} count)))]
    (descends-from? neighbours [1 1])) => (has every? truthy))

(fact "Dead cells stay dead if they have any other number of neighbours."
  (descends-from? #{ , ,  [1 2]  , ,
                    [0 1]  , ,  [2 1]
                     , ,  [1 0]  , ,} [1 1]) => falsey

  (for [neighbours (->> (neighbours-of [1 1]) subsets (filter (comp not #{3} count)))]
    (descends-from? neighbours [1 1])) => (has every? falsey))

(fact "Still-lifes remain constant."
  (generation 1
     #{[1 0] [1 1]
       [0 0] [0 1]}) => #{[1 0] [1 1]
                          [0 0] [0 1]})

(fact "Oscillators cycle."
  (generation 1
    #{ , ,   , ,   , ,
      [0 1] [1 1] [2 1]
       , ,   , ,   , , }) => #{ , ,  [1 2]  , ,
                                , ,  [1 1]  , ,
                                , ,  [1 0]  , , }
      
  (generation 1
    #{ , ,  [1 2]  , ,
       , ,  [1 1]  , ,
       , ,  [1 0]  , , }) => #{ , ,   , ,   , ,
                               [0 1] [1 1] [2 1]
                                , ,   , ,   , , })

(fact "Spaceships move across the grid."
  (generation 4
    #{ , ,   , ,  [2 3]
      [0 2]  , ,  [2 2]
       , ,  [1 1] [2 1]}) => #{ , ,   , ,  [3 2]
                               [1 1]  , ,  [3 1]
                                , ,  [2 0] [3 0]})

(fact "Diehard takes 130 generations to die out."
  (let [diehard 
         #{ , ,   , ,   , ,   , ,   , ,  , ,  [6 2]  , ,
           [0 1] [1 1]  , ,   , ,   , ,  , ,   , ,   , ,
            , ,  [1 0]  , ,   , ,   , , [5 0] [6 0] [7 0]}]

    (generation 129 diehard) =not=> #{}

    (generation 130 diehard) =>     #{}))
