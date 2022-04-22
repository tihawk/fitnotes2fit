package com.developination.fitnotes2fit;

import com.developination.fitnotes2fit.ActivityEncoder.ActivityEncoder;
import com.developination.fitnotes2fit.util.NoiseGenerator;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        NoiseGenerator noiseGenerator = new NoiseGenerator();
        ActivityEncoder.CreateExampleStrengthActivity();
        // for (int i = 0; i <= 60; i++) {
        //     System.out.println(noiseGenerator.noise(i));
        //     System.out.println(noiseGenerator.noise(i, 90, 110));
        // }
    }
}
