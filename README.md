# FitNotes2Fit Converter

Status: working proof of concept
---

This project allows for converting workouts recorded on the [FitNotes](https://play.google.com/store/apps/details?id=com.github.jamesgay.fitnotes&hl=en_US&gl=US) app to be made into `.fit` format, applicable in Garmin Connect and Strava for example.

FitNotes has the option to export all workouts into a csv file. So **FitNotes2Fit** parses the CSV file, divides the rows into workouts with respect to the date recorded, and encodes `.fit` files for each workout using the [**FitSDK**](https://developer.garmin.com/fit/overview/) for Java, containing the sets, reps and weights for each exercise recorded.

Additionally, by supplying an average heart-rate and average rest-time between sets, these can also be encoded into the fit file, to be used by the online platform they get uploaded to. For example, Strava uses the recorded set of heart-rate levels over time to record relative effort for each workout.

## Get it from one of the releases

This repository contains [Releases](https://github.com/tihawk/fitnotes2fit/releases/latest), from which you can directly download the latest uber-jar, and run it with [Java JRE](https://www.java.com/en/download/).

## Running

```java -jar fitnotes2fit.jar```

This will show you the usage.

### Converting a CSV into a set of _fit_ files

```java -jar fitnotes2fit.jar convert -i <path/to/file.csv> [-o <optional/output/dir/>] [-hr <average heartrate>] [-rt <average rest time (in minutes)>]```

- This will take the csv provided by `-i <path/to/file.csv>`, and will convert it to a set of fit files, one for each workout (distinguished by the _Date_ column in the csv).

- The optional argument `-o <optional/output/dir/>` specifies the output folder. Otherwise the current directory will be used.

- The other two optional arguments are for "advanced" usage. If provided an average heart-rate, using `-hr <average heartrate>`, the converter will generate a set of "heart-rate-monitor" records around that average, and encode them within the output workouts.

- Average rest time in minutes (e.g. 1.5) can be provided with `-rt <average rest time>`. If provided, a random rest around that time (plus minus 1 minute) will be generated for each set. This helps with encoding a more appropriate time for the workout, which is useful for example in Strava, where the Relative Effort metric uses the heart-rate and time of activity to be calculated.

# For developers
## Building for yourself

> **_Note:_** Since this project relies on FitSDK, and the sdk is not available from any maven repository, you will have to download it yourself from the FitSDK site.

1. Clone the repo.
1. Download [FitSDK](https://developer.garmin.com/fit/download/), unzip and place the file `<fitsdk-dir>/java/fit.jar` into `<fitnotes2fit-repo-dir>/lib/fit.jar`.
1. Go into repo folder `cd fitnotes2fit`.
1. Run `./mvnw process-resources && ./mvnw install` to install dependencies.
1. Run `./mvnw clean compile assembly:single` to create a jar with all the dependencies in it. This will create a uber-jar at `target/fitnotes2fit.jar`.
  - If a non-uber jar is required, you can simply run `./mvnw clean package`.

## Packages

A package for **fitnotes2fit** is also available to get from the GitHub repository [here](https://github.com/tihawk/fitnotes2fit/packages/).

Since the package relies on FitSDK, and the SDK is not available in any repository, you will need to add it as a local dependency in your project. More information should be available in the package documentation.

### Important limitations

* Currently the one big problem, is that not all default exercises provided by FitNotes are being encoded in the fit category system. At least the defaults will be implemented soon enough. In the log of running the converter, these uncategorised exercises are mentioned.

* This leaves the issue with customly-added to FitNotes exercises. For now, this is not the focus of development, but some possible user input option is conceivable.

## Readme TODO

 - [ ] Present the mapping between fitnotes default exercises and fit categories.
 - [X] Document installation
 - [X] Document usage
 - [ ] Document code

 ## Links

 [FitNotes App](https://play.google.com/store/apps/details?id=com.github.jamesgay.fitnotes&hl=en_US&gl=US)

 [FitSDK](https://developer.garmin.com/fit/overview/)

 [OpenCSV docs](http://opencsv.sourceforge.net/#reading_into_beans)

 [Additional Fit docs](https://apizone.suunto.com/fit-description)