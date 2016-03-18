# massive-cljs
Thin Clojurescript wrapper for [MassiveJS](https://github.com/robconery/massive-js)
> Massive's goal is to help you get data from your database. This is not an ORM, it's a bit more than a query tool - our goal is to do just enough, then get out of your way. 

If you're wondering why you might want to use Massive over a traditional ORM in general, this [blog post](http://rob.conery.io/2015/03/13/bringing-the-power-of-postgres-to-nodejs/) is a good place to start. 

For Clojure(script) specifically, the drawbacks of using an ORM multiply: often the abstractions rely on extracting data into mutable objects, which are then edited and saved back to the database. Not an insurmountable problem, but a conflict of styles which rapidly [becomes clear](https://github.com/gtebbutt/sequelize-cljs) in development.

## Usage
### Require: 
```Clojure
(ns example.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [massive-cljs.core :as massive]
            [massive-cljs.query :as query]
            [cljs.core.async :refer [<!]]))
```

### Connect:
```Clojure
(massive/init! "http://localhost:5432")
```

### Query:
Query functions return a channel, which will receive a response of the format:
```Clojure
{:error? false :content [{:id 1 :name "Steve"} {:id 2 :name "Bill"}]}
```
If `:error?` is true, the content key will be absent and the error message will be present as `:msg`.

In general `query/db-fn` mirrors the syntax of Massive's database functions:
```Clojure
(go
  (let [response (<! (query/db-fn :users :find {:city "London"}))]
    (if (:error? response)
      (println (:msg response))

      (for [row (:content response)]
        (println row)))))
```

```Clojure
(query/db-fn :users :save {:email "new@example.com" :city "Paris"}) ;Insert a new user
(query/db-fn :users :save {:id 4 :city "New York"}) ;Update an existing user by including the PK as a parameter
(query/db-fn :users :find-one {:email "email@example.com"}) ;Returns a single result in :content, rather than a list
(query/db-fn :my-special-function [arg1 arg2]) ;Looks for db/mySpecialFunction.sql in project root
```

Raw SQL can be executed directly using `query/run`, with a list of parameters as the optional second argument:
```Clojure
(query/run "SELECT * FROM users WHERE id > $1" [min-id])
```

