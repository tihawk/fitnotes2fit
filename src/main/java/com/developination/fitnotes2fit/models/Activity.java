package com.developination.fitnotes2fit.models;

import java.util.List;

import com.garmin.fit.DateTime;

public class Activity {
  
  protected DateTime activityStartTime;
  protected String activityName;
  protected List<ActivitySet> setList;
  
  public Activity(DateTime activityStartTime, List<ActivitySet> setList) {
    this.activityStartTime = activityStartTime;
    this.setList = setList;
    this.activityName = "Workout_" + activityStartTime.getDate().toInstant().toString();
  }

  
  /** 
   * @return DateTime
   */
  public DateTime getActivityStartTime() {
    return activityStartTime;
  }
  
  /** 
   * @param activityStartTime
   */
  public void setActivityStartTime(DateTime activityStartTime) {
    this.activityStartTime = activityStartTime;
  }
  
  /** 
   * @return String
   */
  public String getActivityName() {
    return activityName;
  }
  
  /** 
   * @param activityName
   */
  public void setActivityName(String activityName) {
    this.activityName = activityName;
  }
  
  /** 
   * @return List<ActivitySet>
   */
  public List<ActivitySet> getSetList() {
    return setList;
  }
  
  /** 
   * @param setList
   */
  public void setSetList(List<ActivitySet> setList) {
    this.setList = setList;
  }

}
