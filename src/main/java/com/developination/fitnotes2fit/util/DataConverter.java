package com.developination.fitnotes2fit.util;

import java.time.Instant;
import java.util.Date;

import com.garmin.fit.DateTime;

public class DataConverter {

  public static DateTime convertDateTime(String _dateString) {

    final String date = _dateString + "T14:00:00.000Z";
    Instant dateInstant = Instant.parse(date);

    return new DateTime(Date.from(dateInstant));
  }
  
}
