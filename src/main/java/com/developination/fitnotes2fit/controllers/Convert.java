package com.developination.fitnotes2fit.controllers;

import java.util.List;

import com.developination.fitnotes2fit.ActivityEncoder.ActivityEncoder;
import com.developination.fitnotes2fit.FitNotesParser.FitNotesParser;
import com.developination.fitnotes2fit.models.Activity;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "convert")
public class Convert implements Runnable {

  @Option(names = {"--input", "-i"}, required = true, description = "Path to FitNotes csv file to convert.")
  private String file;
  @Option(names = {"--output", "-o"}, defaultValue = "", description = "Output folder for the generated .fit files.")
  private String outFolder;
  @Option(
    names = {"--heartrate", "-hr"},
    arity = "0..1", 
    interactive = true, 
    description = "Average heart-rate for your workouts. Useful, for example, to calculate Relative Effort metric in Strava.")
  private short avgHeartRate;
  @Option(
    names = {"--resttime", "-rt"},
    arity = "0..1",
    interactive = true,
    description = "Average rest time between sets in minutes (e.g. 1.5). Don't think too hard on it, in the output it's randomised within plus-minus 1 minute. Same as for the heart-rate, this is useful when calculating Relative Effort and other metrics."
  )
  private float avgRestTime;

  @Override
  public void run() {

    if (this.avgHeartRate == 0) {
      String s = System.console().readLine("Would you like to enter average heart-rate for your workouts? Useful, for example, to calculate Relative Effort metric in Strava. (default 0): ");
      try {
        if (!s.isEmpty()) {
          this.avgHeartRate = Short.valueOf(s);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    System.out.printf("Average Heart-rate=%s%n", this.avgHeartRate);

    if (this.avgRestTime == 0) {
      String s = System.console().readLine("Would you like to enter average rest time between sets? Don't think too hard on it, in the output it's randomised within plus-minus 1 minute. Same as for the heart-rate, this is useful when calculating Relative Effort and other metrics. Enter in minutes (e.g. 1.5) (default 0): ");
      try {
        if (!s.isEmpty()) {
          this.avgRestTime = Float.valueOf(s);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    System.out.printf("Average Rest time=%s%n", this.avgRestTime);

    System.out.println( "On it!" );
    try {
        FitNotesParser parser = new FitNotesParser();
        List<Activity> activityList = parser.parseFileNotesIntoActivities(file);
        for (Activity activity : activityList) {
            System.out.println("[main] Starting to encode activity: " + activity.getActivityName());
            ActivityEncoder encoder = new ActivityEncoder(activity, avgHeartRate, avgRestTime);
            encoder.encodeActivity(outFolder);
        }
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
  }
  
}
