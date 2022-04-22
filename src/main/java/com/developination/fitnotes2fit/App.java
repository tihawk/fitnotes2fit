package com.developination.fitnotes2fit;

import com.developination.fitnotes2fit.ActivityEncoder.ActivityEncoder;
import com.developination.fitnotes2fit.models.Activity;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Activity activity = ActivityEncoder.getExampleActivity();
        ActivityEncoder encoder = new ActivityEncoder(activity);
        encoder.encodeActivity();
    }
}
