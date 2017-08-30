## Play Framework
* Install latest version of sbt: [SBT](http://www.scala-sbt.org/download.html)
* Clone this repository: `git clone https://github.ncsu.edu/ssrivas8/SOCP1`
* cd to `project1` folder: `cd SOCP1/project1`
* Type `sbt run` to run the application.
  * A running version of this application is also deployed on Heroku and can be accessed from here: [LocationServer](https://boiling-ravine-25465.herokuapp.com/)
* Press `Enter` key to stop the server.
* Type `sbt` to open the sbt console.
* There are following path mappings present in the web application:

| Type | mapping | description |
|---|---|---|
| GET | / | The home page |
| POST | /locationupdate | For tracking location updates. Accepts JSON data in the following format: ``` { "username":"Name", "timestamp":1504108761492, "latitude":51.68519, "longitude":96.13572 } ``` |
