package com.developination.fitnotes2fit.util;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.developination.fitnotes2fit.models.ActivitySet;
import com.garmin.fit.DateTime;
import com.garmin.fit.RecordMesg;
import com.garmin.fit.SetMesg;
import com.garmin.fit.SetType;

public class FitMessage {

  public static SetMesg createSetMessage(ActivitySet set, DateTime timestamp, DateTime setStartTime, int setIndex) {
    SetMesg setMsg = new SetMesg();
    setMsg.setTimestamp(timestamp);
    setMsg.setDuration(set.getDuration());
    setMsg.setStartTime(setStartTime);
    setMsg.setRepetitions((int) set.getReps());
    setMsg.setWeight((float) set.getWeight());
    setMsg.setCategory(0, (int) set.getCategory());
    setMsg.setCategorySubtype(0, (int) set.getSubCategory());
    setMsg.setWeightDisplayUnit(1);
    setMsg.setMessageIndex(setIndex);
    setMsg.setSetType((short) set.getType());
    
    return setMsg;
  }
  
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

  public static SetMesg generateRestSetMessages(DateTime timestamp, DateTime setStartTime, int setIndex) {
    Random random = new Random();
    
    float restTime = random.nextFloat()*300 % (300 - 180 + 1) + 180;
    SetMesg restSetMsg = new SetMesg();
    restSetMsg.setTimestamp(timestamp);
    restSetMsg.setDuration(restTime);
    restSetMsg.setStartTime(setStartTime);
    restSetMsg.setMessageIndex(setIndex);
    restSetMsg.setSetType(SetType.REST);

    return restSetMsg;
  }

}
