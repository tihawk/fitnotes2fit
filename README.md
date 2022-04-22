# FitNotes2Fit Converter

Status: working proof of concept
---

This project allows for converting workouts recorded on FitNotes to be made into `fit` format, applicable in Garmin Connect and Strava for example.

FitNotes has the option to export all workouts into a csv file. So `FitNotes2Fit` parses the `csv` file, and encodes a `fit` file using the **FitSDK** for `java`, containing the sets, reps and weights for each exercise recorded.

> **_NOTE:_** Currently this also generates a list of heart-rate records with average heart-rate of ~100 (hardcoded, with a relatively nice smooth noise function), to make use of Strava's relative effort metric.

More documentation to come.

## TODO

 - [ ] The mapping of exercises to fit categories is incomplete.
 - [ ] Make selection of csv file possible.
 - [ ] Upload directly to garmin connect.
 - [ ] Document process
 - [ ] Document code
 - [ ] Host online