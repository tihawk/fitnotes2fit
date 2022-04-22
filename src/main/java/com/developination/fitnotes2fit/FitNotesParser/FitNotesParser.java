package com.developination.fitnotes2fit.FitNotesParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.developination.fitnotes2fit.models.Activity;
import com.developination.fitnotes2fit.models.ActivitySet;
import com.developination.fitnotes2fit.models.FitNotesSet;
import com.developination.fitnotes2fit.util.DataConverter;
import com.garmin.fit.BenchPressExerciseName;
import com.garmin.fit.CalfRaiseExerciseName;
import com.garmin.fit.CoreExerciseName;
import com.garmin.fit.CrunchExerciseName;
import com.garmin.fit.CurlExerciseName;
import com.garmin.fit.DeadliftExerciseName;
import com.garmin.fit.ExerciseCategory;
import com.garmin.fit.FlyeExerciseName;
import com.garmin.fit.HipRaiseExerciseName;
import com.garmin.fit.LateralRaiseExerciseName;
import com.garmin.fit.LungeExerciseName;
import com.garmin.fit.PullUpExerciseName;
import com.garmin.fit.RowExerciseName;
import com.garmin.fit.ShoulderPressExerciseName;
import com.garmin.fit.ShoulderStabilityExerciseName;
import com.garmin.fit.SquatExerciseName;
import com.garmin.fit.TricepsExtensionExerciseName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

public class FitNotesParser {

  public static Map<String, int[]> EXERCISE_TO_FIT_CATEGORY_MAP = new HashMap<String, int[]>();
    static {
      Map<String, int[]> tempMap = new HashMap<String, int[]>();
        tempMap.put( "Flat Barbell Bench Press",      new int[]{ ExerciseCategory.BENCH_PRESS, BenchPressExerciseName.BARBELL_BENCH_PRESS } );
        tempMap.put( "EZ-Bar Curl",                   new int[]{ ExerciseCategory.CURL, CurlExerciseName.STANDING_EZ_BAR_BICEPS_CURL } );
        tempMap.put( "Overhead Press",                new int[]{ ExerciseCategory.SHOULDER_PRESS, ShoulderPressExerciseName.OVERHEAD_BARBELL_PRESS } );
        tempMap.put( "Cable Crunch",                  new int[]{ ExerciseCategory.CRUNCH, CrunchExerciseName.CABLE_CRUNCH } );
        tempMap.put( "EZ-Bar Skullcrusher",           new int[]{ ExerciseCategory.TRICEPS_EXTENSION, TricepsExtensionExerciseName.LYING_EZ_BAR_TRICEPS_EXTENSION } );
        tempMap.put( "Lat Pulldown",                  new int[]{ ExerciseCategory.PULL_UP, PullUpExerciseName.LAT_PULLDOWN } );
        tempMap.put( "Lateral Dumbbell Raise",        new int[]{ ExerciseCategory.LATERAL_RAISE, LateralRaiseExerciseName.LEANING_DUMBBELL_LATERAL_RAISE } );
        tempMap.put( "Dumbbell Row",                  new int[]{ ExerciseCategory.ROW, RowExerciseName.DUMBBELL_ROW } );
        tempMap.put( "Dumbbell Curl",                 new int[]{ ExerciseCategory.CURL, CurlExerciseName.DUMBBELL_BICEPS_CURL_WITH_STATIC_HOLD } );
        tempMap.put( "Dumbbell Skullcrusher",         new int[]{ ExerciseCategory.TRICEPS_EXTENSION, TricepsExtensionExerciseName.DUMBBELL_LYING_TRICEPS_EXTENSION } );
        tempMap.put( "Flat Dumbbell Fly",             new int[]{ ExerciseCategory.FLYE, FlyeExerciseName.DUMBBELL_FLYE } );
        tempMap.put( "Incline Dumbbell Bench Press",  new int[]{ ExerciseCategory.BENCH_PRESS, BenchPressExerciseName.INCLINE_DUMBBELL_BENCH_PRESS } );
        tempMap.put( "Barbell Glute Bridge",          new int[]{ ExerciseCategory.HIP_RAISE, HipRaiseExerciseName.BARBELL_HIP_THRUST_ON_FLOOR } );
        tempMap.put( "Romanian Deadlift",             new int[]{ ExerciseCategory.DEADLIFT, DeadliftExerciseName.BARBELL_STRAIGHT_LEG_DEADLIFT } );
        tempMap.put( "Barbell Row",                   new int[]{ ExerciseCategory.ROW, RowExerciseName.REVERSE_GRIP_BARBELL_ROW } );
        tempMap.put( "Bulgarian Split Squat",         new int[]{ ExerciseCategory.LUNGE, LungeExerciseName.DUMBBELL_BULGARIAN_SPLIT_SQUAT } );
        tempMap.put( "Deadlift",                      new int[]{ ExerciseCategory.DEADLIFT, DeadliftExerciseName.BARBELL_DEADLIFT } );
        tempMap.put( "Seated Dumbbell Press",         new int[]{ ExerciseCategory.SHOULDER_PRESS, ShoulderPressExerciseName.OVERHEAD_DUMBBELL_PRESS } );
        tempMap.put( "Barbell Curl",                  new int[]{ ExerciseCategory.CURL, CurlExerciseName.BARBELL_BICEPS_CURL } );
        tempMap.put( "Barbell Squat",                 new int[]{ ExerciseCategory.SQUAT, SquatExerciseName.BARBELL_BACK_SQUAT } );
        tempMap.put( "One-legged Hip Thrust",         new int[]{ ExerciseCategory.HIP_RAISE, HipRaiseExerciseName.WEIGHTED_BRIDGE_WITH_LEG_EXTENSION } );
        tempMap.put( "Front Dumbbell Raise",          new int[]{ ExerciseCategory.LATERAL_RAISE, LateralRaiseExerciseName.FRONT_RAISE } );
        tempMap.put( "Dumbbell Calf Raise",           new int[]{ ExerciseCategory.CALF_RAISE, CalfRaiseExerciseName.SINGLE_LEG_STANDING_DUMBBELL_CALF_RAISE } );
        tempMap.put( "Dumbbell Lunges",               new int[]{ ExerciseCategory.LUNGE, LungeExerciseName.DUMBBELL_LUNGE } );
        tempMap.put( "Side Lying External Rotation",  new int[]{ ExerciseCategory.SHOULDER_STABILITY, ShoulderStabilityExerciseName.LYING_EXTERNAL_ROTATION } );
        tempMap.put( "Full Can Exercise",             new int[]{ ExerciseCategory.SHOULDER_STABILITY, ShoulderStabilityExerciseName.STANDING_L_RAISE } );
        tempMap.put( "Flat Dumbbell Bench Press",     new int[]{ ExerciseCategory.BENCH_PRESS, BenchPressExerciseName.DUMBBELL_BENCH_PRESS } );
        tempMap.put( "Russian Twist",                 new int[]{ ExerciseCategory.CORE, CoreExerciseName.RUSSIAN_TWIST } );
        tempMap.put( "Concentration Curl",            new int[]{ ExerciseCategory.CURL, CurlExerciseName.SEATED_DUMBBELL_BICEPS_CURL } );

        EXERCISE_TO_FIT_CATEGORY_MAP = Collections.unmodifiableMap( tempMap );
    }

  public static List<Activity> parseFileNotesIntoActivities(String filepath) throws Exception {
    List<Activity> result = new ArrayList<Activity>();
    List<FitNotesSet> setsList = readFileNotesSets(filepath);
    Map<String, List<ActivitySet>> mapDateToSets = new HashMap<String, List<ActivitySet>>();

    for (FitNotesSet singleSet : setsList) {
      ActivitySet parsedSet = convertFromFitNotesSet(singleSet);

      if (mapDateToSets.containsKey(singleSet.getDate())) {
        mapDateToSets.get(singleSet.getDate()).add(parsedSet);
      } else {
        List<ActivitySet> newList = new ArrayList<ActivitySet>(1);
        newList.add(parsedSet);
        mapDateToSets.put(singleSet.getDate(), newList);
      }
    }

    for (Map.Entry<String, List<ActivitySet>> mapEntry : mapDateToSets.entrySet()) {
      Activity activity = new Activity(
        DataConverter.convertDateTime(mapEntry.getKey()), mapEntry.getValue());
      result.add(activity);
    }
    
    return result;
  }

  public static ActivitySet convertFromFitNotesSet(FitNotesSet fitNotesSet) {
    if (!EXERCISE_TO_FIT_CATEGORY_MAP.containsKey(fitNotesSet.getExercise())) {
      System.out.println("[FitNotesParser][convertFromFitNotesSet] Didn't find mapping for exercise " + fitNotesSet.getExercise());
      return null;
    }
    ActivitySet result = new ActivitySet();
    int[] exCategorySubcategory = EXERCISE_TO_FIT_CATEGORY_MAP.get(fitNotesSet.getExercise());

    result.setCategory(exCategorySubcategory[0]);
    result.setSubCategory(exCategorySubcategory[1]);
    result.setWeight(fitNotesSet.getWeight());
    result.setReps(fitNotesSet.getReps());
    // TODO: check out time -> duration possibility

    return result;
  }

  public static List<FitNotesSet> readFileNotesSets(String filepath) throws Exception {
    Reader reader = new BufferedReader(new FileReader(filepath));
    CsvToBean<FitNotesSet> csvReader = new CsvToBeanBuilder<FitNotesSet>(reader)
                .withType(FitNotesSet.class)
                .withIgnoreLeadingWhiteSpace(true)
                .withSeparator(',')
                .withIgnoreLeadingWhiteSpace(true)
                .build();
    List<FitNotesSet> list = csvReader.parse();
    reader.close();
    return list;
  }
  
}
