(ns ^{:doc "Very simple extension for testing the extension processing flow."
      :author "Simon Brooke"}
  smeagollib.extensions.test)


(def process-test-return-value "<!-- The test extension has run and this is its output -->")

(defn process-test
    [^String _ ^Integer _]
    process-test-return-value)
