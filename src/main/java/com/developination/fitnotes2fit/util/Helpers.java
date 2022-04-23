package com.developination.fitnotes2fit.util;

public class Helpers {
  
  /**
   * Generates a number of calories burned, based on 180 calories burned every 30 minutes.
   * source: https://www.medicalnewstoday.com/articles/323922#calculating-weightlifting-calories
   * @param sessionDurationSeconds
   * @return Calories burned
  */
  public static int generateCaloriesBurned(int sessionDurationSeconds) {
    float duration = sessionDurationSeconds / 3600;
    int caloriesPerHour = 360;
    return Math.round(duration*caloriesPerHour);
  }

}
