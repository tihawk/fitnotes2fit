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

  @Override
  public void run() {
    System.out.println( "On it!" );
    try {
        List<Activity> activityList = FitNotesParser.parseFileNotesIntoActivities(file);
        for (Activity activity : activityList) {
            System.out.println("[main] Starting to encode activity: " + activity.getActivityName());
            ActivityEncoder encoder = new ActivityEncoder(activity);
            encoder.encodeActivity(outFolder);
        }
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
  }
  
}
