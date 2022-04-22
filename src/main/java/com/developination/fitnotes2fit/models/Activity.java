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

  public DateTime getActivityStartTime() {
    return activityStartTime;
  }
  public void setActivityStartTime(DateTime activityStartTime) {
    this.activityStartTime = activityStartTime;
  }
  public String getActivityName() {
    return activityName;
  }
  public void setActivityName(String activityName) {
    this.activityName = activityName;
  }
  public List<ActivitySet> getSetList() {
    return setList;
  }
  public void setSetList(List<ActivitySet> setList) {
    this.setList = setList;
  }

}
