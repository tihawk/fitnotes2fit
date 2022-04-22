package com.developination.fitnotes2fit.ActivityEncoder;

import com.developination.fitnotes2fit.util.FitMessage;
import com.developination.fitnotes2fit.util.NoiseGenerator;
import com.garmin.fit.ActivityMesg;
import com.garmin.fit.DateTime;
import com.garmin.fit.DeadliftExerciseName;
import com.garmin.fit.DeviceIndex;
import com.garmin.fit.DeviceInfoMesg;
import com.garmin.fit.Event;
import com.garmin.fit.EventMesg;
import com.garmin.fit.EventType;
import com.garmin.fit.ExerciseCategory;
import com.garmin.fit.File;
import com.garmin.fit.FileEncoder;
import com.garmin.fit.FileIdMesg;
import com.garmin.fit.Fit;
import com.garmin.fit.FitRuntimeException;
import com.garmin.fit.LapMesg;
import com.garmin.fit.Manufacturer;
import com.garmin.fit.Mesg;
import com.garmin.fit.RecordMesg;
import com.garmin.fit.SessionMesg;
import com.garmin.fit.SetMesg;
import com.garmin.fit.SetType;
import com.garmin.fit.Sport;
import com.garmin.fit.SubSport;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

public class ActivityEncoder {

    public static void CreateExampleStrengthActivity() {
        final String filename = "ExampleStrengthActivity.fit";
        final String date = "2022-04-21" + "T14:00:00.000Z";
        Instant dateInstant = Instant.parse(date);

        List<Mesg> messages = new ArrayList<Mesg>();


        DateTime startTime = new DateTime(Date.from(dateInstant));
        EventMesg eventMesgStart = new EventMesg();
            eventMesgStart.setTimestamp(startTime);
            eventMesgStart.setEvent(Event.TIMER);
            eventMesgStart.setEventType(EventType.START);
            messages.add(eventMesgStart);

            // Session Accumulators
            int sessionTotalElapsedTime = 0;

            // Set accumulators
            DateTime setStartTime = new DateTime(startTime);

            DateTime timestamp = new DateTime(startTime);

            // RecordMesg recordMesg = new RecordMesg();
            // recordMesg.setTimestamp(timestamp);
            // recordMesg.setDistance(0f);
            // recordMesg.setHeartRate((short) 90);
            // messages.add(recordMesg);

            List<Map<String, Object>> setData = getSetData();
            int setIndex = 0;
            Random random = new Random();
            NoiseGenerator noiseGenerator = new NoiseGenerator();

            for (Map<String, Object> set : setData) {

                float duration = (float) set.get("duration");

                // working set message
                SetMesg setMsg = new SetMesg();
                setMsg.setTimestamp(timestamp);
                setMsg.setDuration(duration);
                setMsg.setStartTime(setStartTime);
                setMsg.setRepetitions((int) set.get("reps"));
                setMsg.setWeight((float) set.get("weight"));
                setMsg.setCategory(0, (int) set.get("category"));
                setMsg.setCategorySubtype(0, (int) set.get("subCategory"));
                setMsg.setWeightDisplayUnit(1);
                setMsg.setMessageIndex(setIndex);
                setMsg.setSetType((short) set.get("type"));
                messages.add(setMsg);

                setIndex++;

                // record messages help with generating relative effort in strava
                List<RecordMesg> recordMesges = FitMessage.generateRecordMessages(setStartTime, noiseGenerator, Math.round(duration), sessionTotalElapsedTime);
                for (RecordMesg recordMesg : recordMesges) {
                    messages.add(recordMesg);
                }

                // rest set message
                setStartTime.add(duration);
                float restTime = random.nextFloat()*300 % (300 - 180 + 1) + 180;
                SetMesg restSetMsg = new SetMesg();
                restSetMsg.setTimestamp(timestamp);
                restSetMsg.setDuration(restTime);
                restSetMsg.setStartTime(setStartTime);
                restSetMsg.setMessageIndex(setIndex);
                restSetMsg.setSetType(SetType.REST);
                messages.add(restSetMsg);

                recordMesges = FitMessage.generateRecordMessages(setStartTime, noiseGenerator, Math.round(restTime), sessionTotalElapsedTime);
                for (RecordMesg recordMesg : recordMesges) {
                    messages.add(recordMesg);
                }
        
                setIndex++;
                setStartTime.add(restTime);
                sessionTotalElapsedTime += duration + restTime;

            }


            // Timer Events are a BEST PRACTICE for FIT ACTIVITY files
            EventMesg eventMesgStop = new EventMesg();
            eventMesgStop.setTimestamp(timestamp);
            eventMesgStop.setEvent(Event.TIMER);
            eventMesgStop.setEventType(EventType.STOP_ALL);
            messages.add(eventMesgStop);

            timestamp.add(sessionTotalElapsedTime);

            // Every FIT ACTIVITY file MUST contain at least one Lap message
            LapMesg lapMesg = new LapMesg();
            lapMesg.setMessageIndex(0);
            lapMesg.setTimestamp(timestamp);
            lapMesg.setStartTime(startTime);
            lapMesg.setTotalElapsedTime((float) (timestamp.getTimestamp() - startTime.getTimestamp()));
            lapMesg.setTotalTimerTime((float) (timestamp.getTimestamp() - startTime.getTimestamp()));
            messages.add(lapMesg);

            // Every FIT ACTIVITY file MUST contain at least one Session message
            SessionMesg sessionMesg = new SessionMesg();
            sessionMesg.setMessageIndex(0);
            sessionMesg.setTimestamp(timestamp);
            sessionMesg.setStartTime(startTime);
            sessionMesg.setTotalElapsedTime((float) sessionTotalElapsedTime);
            sessionMesg.setTotalTimerTime((float) sessionTotalElapsedTime);
            sessionMesg.setTotalDistance(0f);
            sessionMesg.setSport(Sport.TRAINING);
            sessionMesg.setSubSport(SubSport.STRENGTH_TRAINING);
            sessionMesg.setFirstLapIndex(0);
            sessionMesg.setNumLaps(1);
            sessionMesg.setAvgHeartRate((short) 90);
            sessionMesg.setTotalCalories(380);
            messages.add(sessionMesg);

            // Every FIT ACTIVITY file MUST contain EXACTLY one Activity message
            ActivityMesg activityMesg = new ActivityMesg();
            activityMesg.setTimestamp(timestamp);
            activityMesg.setNumSessions(1);
            TimeZone timeZone = TimeZone.getTimeZone("Europe/Athens");
            long timezoneOffset = (timeZone.getRawOffset() + timeZone.getDSTSavings()) / 1000;
            activityMesg.setLocalTimestamp(timestamp.getTimestamp() + timezoneOffset);
            activityMesg.setTotalTimerTime((float) sessionTotalElapsedTime);

            messages.add(activityMesg);

            CreateActivityFile(messages, filename, startTime);
    }
   
    public static void CreateActivityFile(List<Mesg> messages, String filename, DateTime startTime) {
        // The combination of file type, manufacturer id, product id, and serial number should be unique.
        // When available, a non-random serial number should be used.
        File fileType = File.ACTIVITY;
        short manufacturerId = Manufacturer.DEVELOPMENT;
        short productId = 0;
        float softwareVersion = 1.0f;

        Random random = new Random();
        int serialNumber = random.nextInt();

        // Every FIT file MUST contain a File ID message
        FileIdMesg fileIdMesg = new FileIdMesg();
        fileIdMesg.setType(fileType);
        fileIdMesg.setManufacturer((int) manufacturerId);
        fileIdMesg.setProduct((int) productId);
        fileIdMesg.setTimeCreated(startTime);
        fileIdMesg.setSerialNumber((long) serialNumber);

        // A Device Info message is a BEST PRACTICE for FIT ACTIVITY files
        DeviceInfoMesg deviceInfoMesg = new DeviceInfoMesg();
        deviceInfoMesg.setDeviceIndex(DeviceIndex.CREATOR);
        deviceInfoMesg.setManufacturer(Manufacturer.DEVELOPMENT);
        deviceInfoMesg.setProduct((int) productId);
        deviceInfoMesg.setProductName("FIT Cookbook"); // Max 20 Chars
        deviceInfoMesg.setSerialNumber((long) serialNumber);
        deviceInfoMesg.setSoftwareVersion(softwareVersion);
        deviceInfoMesg.setTimestamp(startTime);

        // Create the output stream
        FileEncoder encode;

        try {
            encode = new FileEncoder(new java.io.File("target/" + filename), Fit.ProtocolVersion.V2_0);
        } catch (FitRuntimeException e) {
            System.err.println("Error opening file " + filename);
            e.printStackTrace();
            return;
        }

        encode.write(fileIdMesg);
        encode.write(deviceInfoMesg);

        for (Mesg message : messages) {
            encode.write(message);
        }

        // Close the output stream
        try {
            encode.close();
        } catch (FitRuntimeException e) {
            System.err.println("Error closing encode.");
            e.printStackTrace();
            return;
        }
        System.out.println("Encoded FIT Activity file " + filename);
    }

    /**
     * Creates an example strength training data set
     * Each set contains duration, reps, weight, category and
     * subcategory, type.
     *
     * @return a list of maps where each map is a pool length.
     */
    @SuppressWarnings("serial")
    public static List<Map<String, Object>> getSetData() {
        // Example Swim length representing a 500 yard pool swim using different strokes and drills.
        LinkedHashMap<String, Object> set0 = new LinkedHashMap<String, Object>() {
            {
                put("duration", 60f);
                put("reps", 6);
                put("weight", 105f);
                put("category", ExerciseCategory.DEADLIFT);
                put("subCategory", DeadliftExerciseName.BARBELL_DEADLIFT);
                put("type", SetType.ACTIVE);
            }
        };

        LinkedHashMap<String, Object> set1 = new LinkedHashMap<String, Object>() {
            {
                put("duration", 60f);
                put("reps", 5);
                put("weight", 105f);
                put("category", ExerciseCategory.DEADLIFT);
                put("subCategory", DeadliftExerciseName.BARBELL_DEADLIFT);
                put("type", SetType.ACTIVE);
            }
        };

        LinkedHashMap<String, Object> set2 = new LinkedHashMap<String, Object>() {
            {
                put("duration", 60f);
                put("reps", 6);
                put("weight", 100f);
                put("category", ExerciseCategory.DEADLIFT);
                put("subCategory", DeadliftExerciseName.BARBELL_DEADLIFT);
                put("type", SetType.ACTIVE);
            }
        };

        LinkedHashMap<String, Object> set3 = new LinkedHashMap<String, Object>() {
            {
                put("duration", 60f);
                put("reps",  5);
                put("weight", 100f);
                put("category", ExerciseCategory.DEADLIFT);
                put("subCategory", DeadliftExerciseName.BARBELL_DEADLIFT);
                put("type", SetType.ACTIVE);
            }
        };

        

        List<Map<String, Object>> setData = new ArrayList<Map<String, Object>>(
                Arrays.asList(
                        set0, set1, set2, set3)
        );
        return setData;
    }
}

