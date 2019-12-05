(ns arrival.core-test
  (:require [clojure.test :refer :all]
            [arrival.core :refer :all :as ac]))

(deftest is-empty-1
  (testing "Empty input case 1"
    (is (= (ac/parse-courses [])
           '()))))

(deftest is-empty-2
  (testing "Empty input case 2"
    (is (= (ac/parse-courses nil)
           '()))))

(deftest is-empty-3
  (testing "Empty input case 3"
    (is (= (ac/parse-courses {})
           '()))))

(deftest bad-imput-1
  (testing "Bad imput with no name key returns empty list"
    (is (= (ac/parse-courses {:id "476d1685-3de2-4bfa-bb01-64afbd0e28eb"
                              :result
                              {:learning-course-catalogue/code :be-000005/lcc-000001
                               :children
                               [{:db/id 17592186045643
                                 :no-name "Folder 1"
                                 :descr "Learning Course Folder 1"
                                 :seq 1
                                 :type :folder
                                 :children []}]}})
           '()))))

(deftest bad-imput-2
  (testing "Bad imput with no type key returns empty list"
    (is (= (ac/parse-courses {:id "476d1685-3de2-4bfa-bb01-64afbd0e28eb"
                              :result
                              {:learning-course-catalogue/code :be-000005/lcc-000001
                               :children
                               [{:db/id 17592186045643
                                 :name "Folder 1"
                                 :descr "Learning Course Folder 1"
                                 :seq 1
                                 :no-type :folder
                                 :children []}]}})
           '()))))

(deftest bad-imput-3
  (testing "Bad imput with no children key returns empty list"
    (is (= (ac/parse-courses {:id "476d1685-3de2-4bfa-bb01-64afbd0e28eb"
                              :result
                              {:learning-course-catalogue/code :be-000005/lcc-000001
                               :children
                               [{:db/id 17592186045643
                                 :name "Folder 1"
                                 :descr "Learning Course Folder 1"
                                 :seq 1
                                 :type :folder
                                 :no-children []}]}})
           '()))))

(deftest course-in-level-one-folder
  (testing "One course in level one folder"
    (is (= (ac/parse-courses {:id "476d1685-3de2-4bfa-bb01-64afbd0e28eb"
                              :result
                              {:learning-course-catalogue/code :be-000005/lcc-000001
                               :children
                               [{:db/id 17592186045643
                                 :name "Folder 1"
                                 :descr "Learning Course Folder 1"
                                 :seq 1
                                 :no-type :folder
                                 :children [{:db/id 17592186045648
                                             :name "Folder 1.1"
                                             :descr "Learning Course Folder 1.1"
                                             :seq 1
                                             :type :folder
                                             :children
                                             [{:name "Course 1"
                                               :descr "Some course 1 description."
                                               :seq 1
                                               :ref-id 17592186045674
                                               :type :course
                                               :course-id 17592186045616
                                               :db/id 17592186045674}]}]}]}})
           '({:course "Course 1", :path "Folder 1 / Folder 1.1"})))))

(deftest folder-without-course
  (testing "Folder with no courses"
    (is (= (ac/parse-courses {:id "476d1685-3de2-4bfa-bb01-64afbd0e28eb"
                              :result
                              {:learning-course-catalogue/code :be-000005/lcc-000001
                               :children
                               [{:db/id 17592186045643
                                 :name "Folder 1"
                                 :descr "Learning Course Folder 1"
                                 :seq 1
                                 :type :folder
                                 :children []}]}})
           '()))))

(deftest course-in-the-root
  (testing "One course in the root folder"
    (is (= (ac/parse-courses {:id "476d1685-3de2-4bfa-bb01-64afbd0e28eb"
                              :result
                              {:learning-course-catalogue/code :be-000005/lcc-000001
                               :children
                               [{:db/id 17592186045643
                                 :name "Course 1"
                                 :descr "Learning Course Folder 1"
                                 :seq 1
                                 :type :course
                                 :children []}]}})
           '({:course "Course 1", :path "/"})))))
