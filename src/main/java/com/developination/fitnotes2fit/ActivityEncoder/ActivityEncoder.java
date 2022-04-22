package com.developination.fitnotes2fit.ActivityEncoder;

import com.developination.fitnotes2fit.models.Activity;
import com.developination.fitnotes2fit.models.ActivitySet;
import com.developination.fitnotes2fit.util.DataConverter;
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
import com.garmin.fit.Sport;
import com.garmin.fit.SubSport;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

public class ActivityEncoder {

    protected List<Mesg> messages;
    protected Activity activity;
    protected String filename;

    public ActivityEncoder(Activity activity) {
        this.activity = activity;
        this.messages = new ArrayList<Mesg>();
        this.filename = activity.getActivityName() + ".fit";
    }

    public void encodeActivity(String outFolder) {
        if (activity.getSetList().isEmpty()) {
            return;
        }

        // Start event
        DateTime startTime = activity.getActivityStartTime();
        EventMesg eventMesgStart = new EventMesg();
        eventMesgStart.setTimestamp(startTime);
        eventMesgStart.setEvent(Event.TIMER);
        eventMesgStart.setEventType(EventType.START);
        messages.add(eventMesgStart);

        // Accumulators
        int sessionTotalElapsedTime = 0;
        DateTime setStartTime = new DateTime(startTime);
        DateTime timestamp = new DateTime(startTime);
        int setIndex = 0;
        NoiseGenerator noiseGenerator = new NoiseGenerator();

        for (ActivitySet set : activity.getSetList()) {
            if (set == null) {
                continue;
            }
            float duration = set.getDuration();

            // working set message
            SetMesg setMsg = FitMessage.createSetMessage(set, timestamp, setStartTime, setIndex);
            messages.add(setMsg);

            // record messages help with generating relative effort in strava
            List<RecordMesg> recordMesges = FitMessage.generateRecordMessages(
                setStartTime, noiseGenerator, Math.round(duration), sessionTotalElapsedTime);
            for (RecordMesg recordMesg : recordMesges) {
                messages.add(recordMesg);
            }

            // increment time and set index for rest set
            setIndex++;
            setStartTime.add(duration);
            // rest set message
            SetMesg restSetMsg = FitMessage.generateRestSetMessages(timestamp, setStartTime, setIndex);
            messages.add(restSetMsg);
            
            // more record messages for during rest
            float restTime = restSetMsg.getDuration();
            recordMesges = FitMessage.generateRecordMessages(
                setStartTime, noiseGenerator, Math.round(restTime), sessionTotalElapsedTime + Math.round(duration));
            for (RecordMesg recordMesg : recordMesges) {
                messages.add(recordMesg);
            }

            // increment for next iteration
            setIndex++;
            setStartTime.add(restTime);
            sessionTotalElapsedTime += duration + restTime;

        }

        // End event, add lap, session and activity messages as required
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

        CreateActivityFile(messages, filename, startTime, outFolder);

    }
   
    public static void CreateActivityFile(List<Mesg> messages, String filename, DateTime startTime, String outFolder) {
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
            java.io.File theFolder = new java.io.File(outFolder);
            if (!theFolder.exists()) {
                theFolder.mkdirs();
            }
            
            encode = new FileEncoder(new java.io.File(Paths.get(outFolder, filename).toString()), Fit.ProtocolVersion.V2_0);
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

    public static Activity getExampleActivity() {
        Activity activity = new Activity(DataConverter.convertDateTime("2022-04-21"), getExampleSetData());
        return activity;
    }
    /**
     * Creates an example strength training data set
     * Each set contains duration, reps, weight, category and
     * subcategory, type.
     *
     * @return a list of maps where each map is a pool length.
     */
    public static List<ActivitySet> getExampleSetData() {
        ActivitySet set0 = new ActivitySet(
            6, 105f, ExerciseCategory.DEADLIFT, DeadliftExerciseName.BARBELL_DEADLIFT);

        ActivitySet set1 = new ActivitySet(
            6, 105f, ExerciseCategory.DEADLIFT, DeadliftExerciseName.BARBELL_DEADLIFT);

        ActivitySet set2 = new ActivitySet(
            6, 105f, ExerciseCategory.DEADLIFT, DeadliftExerciseName.BARBELL_DEADLIFT);

        ActivitySet set3 = new ActivitySet(
            6, 105f, ExerciseCategory.DEADLIFT, DeadliftExerciseName.BARBELL_DEADLIFT);

        List<ActivitySet> setData = new ArrayList<ActivitySet>(
            Arrays.asList(set0, set1, set2, set3));
        return setData;
    }
}

