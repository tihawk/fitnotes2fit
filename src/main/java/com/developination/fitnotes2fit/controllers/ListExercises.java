package com.developination.fitnotes2fit.controllers;

import java.util.ArrayList;

import com.developination.fitnotes2fit.FitNotesParser.FitNotesParser;

import picocli.CommandLine.Command;

@Command(name = "list_exercises")
public class ListExercises implements Runnable {
  @Override
  public void run() {
    try {
        FitNotesParser parser = new FitNotesParser(null);
        ArrayList<String> exercises = parser.getAllPossibleExercises();

        for (String FITExerciseName : exercises) {
            System.out.println(FITExerciseName);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
  }
  
}
