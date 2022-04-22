package com.developination.fitnotes2fit;

import java.util.List;

import com.developination.fitnotes2fit.ActivityEncoder.ActivityEncoder;
import com.developination.fitnotes2fit.FitNotesParser.FitNotesParser;
import com.developination.fitnotes2fit.models.Activity;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        try {
            List<Activity> activityList = FitNotesParser.parseFileNotesIntoActivities("target/FitNotes_Export_2022_04_21_22_28_06.csv");
            for (Activity activity : activityList) {
                System.out.println("[main] Starting to encode activity: " + activity.getActivityName());
                ActivityEncoder encoder = new ActivityEncoder(activity);
                encoder.encodeActivity();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Map<String, String> values = new CSVReaderHeaderAware(new FileReader("yourfile.csv")).readMap();
    }
}
