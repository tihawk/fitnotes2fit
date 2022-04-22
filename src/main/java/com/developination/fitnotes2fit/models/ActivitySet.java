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

  public float getDuration() {
    return duration;
  }
  public void setDuration(float duration) {
    this.duration = duration;
  }
  public int getReps() {
    return reps;
  }
  public void setReps(int reps) {
    this.reps = reps;
  }
  public float getWeight() {
    return weight;
  }
  public void setWeight(float weight) {
    this.weight = weight;
  }
  public int getCategory() {
    return category;
  }
  public void setCategory(int category) {
    this.category = category;
  }
  public int getSubCategory() {
    return subCategory;
  }
  public void setSubCategory(int subCategory) {
    this.subCategory = subCategory;
  }
  public int getType() {
    return type;
  }
  public void setType(int type) {
    this.type = type;
  }

}
