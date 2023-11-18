(ns arrival.core
  (:require
    [clojure.edn :as edn])
  (:gen-class))

(defn- find-branches
  "The function recursively searches for the course titles in vector and returns the full
   parent folder path of each found course. If it is something wrong with input data 
   the function will return the empty vector."
  [xs]
  (if (vector? xs)
    (map (fn [x]
           (let [name (:name x)
                 type (:type x)
                 sub (find-branches (:children x))
                 not-empty (filter some? sub)
                 nested (map #(into [{:name name :type type}] %) not-empty)]
             (if (seq nested)
               nested
               [{:name name :type type}])))
         xs)
    []))

(defn- decorate
  "Helps to represent separate course path parts as backslashed full path."
  [x]
  (let [name-part (last x)
        path-part (take (- (count x) 1) x)
        path (if (empty? path-part)
               "/"
               (apply str (interpose " / " path-part)))]
    (into {} {:course name-part :path path})))

(defn parse-courses
  "For the given hash-map with the tree-like data structure of folders and courses,
   the function will return the list of hash-maps with the course title and full parent
   folders path to it. For root level courses the path will be just a backslash symbol."
  [xs]
  (let [raw (find-branches (:children (:result xs)))
        flat (map flatten raw)
        filtered (filter (fn [x]
                           (not (nil? (seq (filter #(= :course (:type %)) x)))))
                         flat)
        data-rows (map (fn [x] (map #(:name %) x)) filtered)]
    (map decorate data-rows)))

(defn -main
  "Main application entry point."
  [& args]
  (->> "data.edn"
       slurp
       edn/read-string
       parse-courses))
