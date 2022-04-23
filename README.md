# FitNotes2Fit Converter

Status: working proof of concept
---

This project allows for converting workouts recorded on the [FitNotes](https://play.google.com/store/apps/details?id=com.github.jamesgay.fitnotes&hl=en_US&gl=US) app to be made into `.fit` format, applicable in Garmin Connect and Strava for example.

FitNotes has the option to export all workouts into a csv file. So **FitNotes2Fit** parses the CSV file, and encodes a `.fit` file using the [**FitSDK**](https://developer.garmin.com/fit/overview/) for Java, containing the sets, reps and weights for each exercise recorded.

> **_NOTE:_** Currently this also generates a list of heart-rate records with average heart-rate of ~105 (hardcoded, with a relatively nice smooth noise function), to make use of Strava's relative effort metric.

## Building for yourself

> **_NOTE:_** Requires [Maven](https://maven.apache.org/install.html) and Java JDK. If you don't have those, just get a _Release_ version.

1. Clone the repo
2. Go into repo folder `cd fitnotes2fit`.
3. Run `mvn clean install` to install dependencies.
4. Run `mvn clean compile assembly:single` to create a jar with all the dependencies in it. This will create a uber-jar at `target/fitnotes2fit.jar`.

## Get it from one of the releases

This repository contains [Releases](https://github.com/tihawk/fitnotes2fit/releases), from which you can directly download the uber-jar, and run it with [Java JRE](https://www.java.com/en/download/).

## Running

```java -jar target/fitnotes2fit.jar```

This will show you the usage.

More documentation to come.

## TODO

 - [ ] The mapping of exercises to fit categories is incomplete.
 - [X] Make selection of csv file possible.
 - [ ] Make generation of additional data optional.
 - [ ] Upload directly to garmin connect.
 - [X] Document installation
 - [ ] Document usage
 - [ ] Document code
 - [ ] Host online

 ## Links

 [FitNotes App](https://play.google.com/store/apps/details?id=com.github.jamesgay.fitnotes&hl=en_US&gl=US)

 [FitSDK](https://developer.garmin.com/fit/overview/)

 [OpenCSV docs](http://opencsv.sourceforge.net/#reading_into_beans)

 [Additional Fit docs](https://apizone.suunto.com/fit-description)