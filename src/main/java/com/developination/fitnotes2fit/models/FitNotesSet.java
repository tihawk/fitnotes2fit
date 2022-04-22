package com.developination.fitnotes2fit.models;

import com.opencsv.bean.CsvBindByName;

public class FitNotesSet {
  
  @CsvBindByName(column = "Date", required = true)
  protected String date;
  @CsvBindByName(column = "Exercise", required = true)
  protected String exercise;
  @CsvBindByName(column = "Category")
  protected String category;
  @CsvBindByName(column = "Weight (kgs)"/* , required = true */)
  protected float weight;
  @CsvBindByName(column = "Reps"/* , required = true */)
  protected int reps;
  @CsvBindByName(column = "Distance")
  protected float distance;
  @CsvBindByName(column = "Distance Unit")
  protected String distanceUnit;
  @CsvBindByName(column = "Time")
  protected String time;

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
  public String getTime() {
    return time;
  }
  public void setTime(String time) {
    this.time = time;
  }

}
