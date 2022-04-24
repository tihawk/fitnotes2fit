package com.developination.fitnotes2fit.models;

import com.garmin.fit.SetType;

public class ActivitySet {
  
  protected float duration;
  protected int reps;
  protected float weight;
  protected int category;
  protected int subCategory;
  protected int type;
  
  public ActivitySet(int reps, float weight, int category, int subCategory) {
    this.reps = reps;
    this.weight = weight;
    this.category = category;
    this.subCategory = subCategory;
    this.duration = 60f;
    this.type = SetType.ACTIVE;
  }
  
  public ActivitySet() {
    this.duration = 60f;
    this.type = SetType.ACTIVE;
  }

  
  /** 
   * @return float
   */
  public float getDuration() {
    return duration;
  }
  
  /** 
   * @param duration
   */
  public void setDuration(float duration) {
    this.duration = duration;
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
  public int getCategory() {
    return category;
  }
  
  /** 
   * @param category
   */
  public void setCategory(int category) {
    this.category = category;
  }
  
  /** 
   * @return int
   */
  public int getSubCategory() {
    return subCategory;
  }
  
  /** 
   * @param subCategory
   */
  public void setSubCategory(int subCategory) {
    this.subCategory = subCategory;
  }
  
  /** 
   * @return int
   */
  public int getType() {
    return type;
  }
  
  /** 
   * @param type
   */
  public void setType(int type) {
    this.type = type;
  }

}
