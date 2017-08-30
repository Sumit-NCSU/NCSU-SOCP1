# Service Oriented Computing - Project 1

# How to Run

## Play Framework
* Install latest version of sbt: [SBT](http://www.scala-sbt.org/download.html)
* Clone this repository: `git clone https://github.ncsu.edu/ssrivas8/SOCP1`
* cd to `project1` folder: `cd SOCP1/project1`
* Type `sbt run` to run the application.
  * A running version of this application is also deployed on Heroku and can be accessed from here: [LocationServer](https://location-server-9x.herokuapp.com/)
* For stopping the server, Press the `Enter` key on keyboard.
* For opening the sbt console, type `sbt`from the command prompt.
* There are following path mappings present in the web application:

| Type | mapping | description |
|---|---|---|
| GET | / | The home page |
| POST | /locationupdate | For tracking location updates. Accepts JSON data in the following format: ``` { "username":"Name", "timestamp":1504108761492, "latitude":51.68519, "longitude":96.13572 } ``` |
 
 **Note: This project is created in windows environment.


## Android
* The android project is located in following directory: `SOCP1/LocationTracker`
* The apk for the application is located in the root of the android Project directory: `SOCP1/LocationTracker/LocationTracker.apk`
* Install the apk on Android device.
  * The application will request for location access when it is being installed. If this permission is not provided, then the application will not run.
  * The apk is configured to use the URL of the server deployed on Heroku by default.
  * If running on local machine and the android emulator, then `10.0.2.2:9000` is the default ip that can be used to access the server deployed on the local machine. This can be achieved by changing the `Host:` field in the android application.
* Enter your name in the `username` field in the android application.
  * Username is mandatory. If the username is not given then the `Start Tracking` button will be disabled.
* Press the toggle button labeled `Start tracking` to start sending the location data to the server.
  * `Start Tracking` will also disable the host and username fields so that the user cannot change them in between.
* The response from the server will be displayed on the application screen on the bottom half.
* Clicking on the toggle button which now says `Stop Tracking` will stop the location tracking and will stop sending the data to the server.
  * `Stop Tracking` will enable the host and username fields so that the user can change them again.
* The `Reset` button will reset the host name and also clear the bottom half of the application screen where results are displayed.
