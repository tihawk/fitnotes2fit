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

  
  /** 
   * @return String
   */
  public String getDate() {
    return date;
  }
  
  /** 
   * @param date
   */
  public void setDate(String date) {
    this.date = date;
  }
  
  /** 
   * @return String
   */
  public String getExercise() {
    return exercise;
  }
  
  /** 
   * @param exercise
   */
  public void setExercise(String exercise) {
    this.exercise = exercise;
  }
  
  /** 
   * @return String
   */
  public String getCategory() {
    return category;
  }
  
  /** 
   * @param category
   */
  public void setCategory(String category) {
    this.category = category;
  }
  
  /** 
   * @return float
   */
  public float getWeight() {
    return weight;
  }
  
  /** 
   * @param weight
   */
  public void setWeight(float weight) {
    this.weight = weight;
  }
  
  /** 
   * @return int
   */
  public int getReps() {
    return reps;
  }
  
  /** 
   * @param reps
   */
  public void setReps(int reps) {
    this.reps = reps;
  }
  
  /** 
   * @return float
   */
  public float getDistance() {
    return distance;
  }
  
  /** 
   * @param distance
   */
  public void setDistance(float distance) {
    this.distance = distance;
  }
  
  /** 
   * @return String
   */
  public String getDistanceUnit() {
    return distanceUnit;
  }
  
  /** 
   * @param distanceUnit
   */
  public void setDistanceUnit(String distanceUnit) {
    this.distanceUnit = distanceUnit;
  }
  
  /** 
   * @return String
   */
  public String getTime() {
    return time;
  }
  
  /** 
   * @param time
   */
  public void setTime(String time) {
    this.time = time;
  }

}
