package com.developination.fitnotes2fit.util;

import java.time.Instant;
import java.util.Date;

import com.garmin.fit.DateTime;

public class DataConverter {

  
  /** 
   * Converts an ISO date string to a FIT DateTime object
   * 
   * @param _dateString in ISO format, e.g. 2022-04-01
   * @return DateTime
   */
  public static DateTime convertDateTime(String _dateString) {

    final String date = _dateString + "T14:00:00.000Z";
    Instant dateInstant = Instant.parse(date);

    return new DateTime(Date.from(dateInstant));
  }
  
}
