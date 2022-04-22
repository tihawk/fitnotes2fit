package com.developination.fitnotes2fit.util;

import java.util.ArrayList;
import java.util.List;

import com.garmin.fit.DateTime;
import com.garmin.fit.RecordMesg;

public class FitMessage {
  
  public static List<RecordMesg> generateRecordMessages(DateTime setStartTime, NoiseGenerator noiseGenerator, int range, int totalElapsedTime) {

    RecordMesg recordMesg = new RecordMesg();
    List<RecordMesg> result = new ArrayList<RecordMesg>();

    DateTime recordTime = new DateTime(setStartTime);
    for (int i = 0; i <= range; i++) {
        recordTime.add(1);
        short heartRate = noiseGenerator.noise(i+totalElapsedTime, 90, 120);
        recordMesg = new RecordMesg();
        recordMesg.setTimestamp(recordTime);
        recordMesg.setDistance(0f);
        recordMesg.setHeartRate(heartRate);
        result.add(recordMesg);
    }

    return result;
  }

}
