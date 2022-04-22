package com.developination.fitnotes2fit.models;

public class FitNotesWorkout {
  
  protected String date;
  protected String exercise;
  protected String category;
  protected float weight;
  protected int reps;
  protected float distance;
  protected String distanceUnit;
  protected float time;

  public FitNotesWorkout(String date, String exercise, String category, float weight, int reps) {
    this.date = date;
    this.exercise = exercise;
    this.category = category;
    this.weight = weight;
    this.reps = reps;
  }
  
  public String getDate() {
    return date;
  }
  public void setDate(String date) {
    this.date = date;
  }
  public String getExercise() {
    return exercise;
  }
  public void setExercise(String exercise) {
    this.exercise = exercise;
  }
  public String getCategory() {
    return category;
  }
  public void setCategory(String category) {
    this.category = category;
  }
  public float getWeight() {
    return weight;
  }
  public void setWeight(float weight) {
    this.weight = weight;
  }
  public int getReps() {
    return reps;
  }
  public void setReps(int reps) {
    this.reps = reps;
  }
  public float getDistance() {
    return distance;
  }
  public void setDistance(float distance) {
    this.distance = distance;
  }
  public String getDistanceUnit() {
    return distanceUnit;
  }
  public void setDistanceUnit(String distanceUnit) {
    this.distanceUnit = distanceUnit;
  }
  public float getTime() {
    return time;
  }
  public void setTime(float time) {
    this.time = time;
  }

}
