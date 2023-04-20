package com.developination.fitnotes2fit.FitNotesParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.developination.fitnotes2fit.models.Activity;
import com.developination.fitnotes2fit.models.ActivitySet;
import com.developination.fitnotes2fit.models.FitNotesSet;
import com.developination.fitnotes2fit.util.DataConverter;
import com.garmin.fit.BenchPressExerciseName;
import com.garmin.fit.CalfRaiseExerciseName;
import com.garmin.fit.CardioExerciseName;
import com.garmin.fit.CarryExerciseName;
import com.garmin.fit.ChopExerciseName;
import com.garmin.fit.CoreExerciseName;
import com.garmin.fit.CrunchExerciseName;
import com.garmin.fit.CurlExerciseName;
import com.garmin.fit.DeadliftExerciseName;
import com.garmin.fit.ExerciseCategory;
import com.garmin.fit.FlyeExerciseName;
import com.garmin.fit.HipRaiseExerciseName;
import com.garmin.fit.HipStabilityExerciseName;
import com.garmin.fit.HipSwingExerciseName;
import com.garmin.fit.HyperextensionExerciseName;
import com.garmin.fit.LateralRaiseExerciseName;
import com.garmin.fit.LegCurlExerciseName;
import com.garmin.fit.LegRaiseExerciseName;
import com.garmin.fit.LungeExerciseName;
import com.garmin.fit.OlympicLiftExerciseName;
import com.garmin.fit.PlankExerciseName;
import com.garmin.fit.PlyoExerciseName;
import com.garmin.fit.PullUpExerciseName;
import com.garmin.fit.PushUpExerciseName;
import com.garmin.fit.RowExerciseName;
import com.garmin.fit.RunExerciseName;
import com.garmin.fit.ShoulderPressExerciseName;
import com.garmin.fit.ShoulderStabilityExerciseName;
import com.garmin.fit.ShrugExerciseName;
import com.garmin.fit.SitUpExerciseName;
import com.garmin.fit.SquatExerciseName;
import com.garmin.fit.TotalBodyExerciseName;
import com.garmin.fit.TricepsExtensionExerciseName;
import com.garmin.fit.WarmUpExerciseName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class FitNotesParser {

  private Map<String, int[]> exercise_to_fit_category_map = new HashMap<>();
  private Map<String, int[]> FITStringToFITIndices;

  /**
   * Class to parse a FitNotes CSV file into activities in FIT format.
   * Supports a hardcoded set of default FitNotes activities. A user-provided map complements this.
   *
   * @param userMap: User-provided mapping from FitNotes activity name to FIT activity name.
   * @return List<Activity>
   * @throws Exception
   */
  public FitNotesParser (Map<String, String> userMap) throws Exception {
    this.exercise_to_fit_category_map = getHardcodedMap();
    this.FITStringToFITIndices = buildFITStringToFITIndices();
    
    // The user-provided mapping maps from FitNotes name to FIT name.
    // We need the tuple of (category_id, exercise_id) instead of the FIT name.
    if (userMap != null){
      for (Map.Entry<String,String> entry : userMap.entrySet()) {
        String FitNotesName = entry.getKey();
        String FITName = entry.getValue();
        int[] idTuple = FITStringToFITIndices.get(FITName);
        this.exercise_to_fit_category_map.put(FitNotesName, idTuple);
      }
    }
  }

  public ArrayList<String> getAllPossibleExercises(){
    ArrayList<String> FITActivityNames = new ArrayList<String>(FITStringToFITIndices.keySet());
    Collections.sort(FITActivityNames);
    return FITActivityNames;
  }


  private Map<String,int[]> getHardcodedMap(){
    Map<String, int[]> tempMap = new HashMap<>();
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
    return tempMap;
  }

  private Map<String, int[]> buildFITStringToFITIndices() throws Exception{
    // Build a map from the string version of the FIT exercise name to a tuple of CategoryID, ExerciseNameID

    Map<Integer, Class<?>> classMap = new HashMap<>();
    classMap.put(0, BenchPressExerciseName.class);
    classMap.put(1, CalfRaiseExerciseName.class);
    classMap.put(2, CardioExerciseName.class);
    classMap.put(3, CarryExerciseName.class);
    classMap.put(4, ChopExerciseName.class);
    classMap.put(5, CoreExerciseName.class);
    classMap.put(6, CrunchExerciseName.class);
    classMap.put(7, CurlExerciseName.class);
    classMap.put(8, DeadliftExerciseName.class);
    classMap.put(9, FlyeExerciseName.class);
    classMap.put(10, HipRaiseExerciseName.class);
    classMap.put(11, HipStabilityExerciseName.class);
    classMap.put(12, HipSwingExerciseName.class);
    classMap.put(13, HyperextensionExerciseName.class);
    classMap.put(14, LateralRaiseExerciseName.class);
    classMap.put(15, LegCurlExerciseName.class);
    classMap.put(16, LegRaiseExerciseName.class);
    classMap.put(17, LungeExerciseName.class);
    classMap.put(18, OlympicLiftExerciseName.class);
    classMap.put(19, PlankExerciseName.class);
    classMap.put(20, PlyoExerciseName.class);
    classMap.put(21, PullUpExerciseName.class);
    classMap.put(22, PushUpExerciseName.class);
    classMap.put(23, RowExerciseName.class);
    classMap.put(24, ShoulderPressExerciseName.class);
    classMap.put(25, ShoulderStabilityExerciseName.class);
    classMap.put(26, ShrugExerciseName.class);
    classMap.put(27, SitUpExerciseName.class);
    classMap.put(28, SquatExerciseName.class);
    classMap.put(29, TotalBodyExerciseName.class);
    classMap.put(30, TricepsExtensionExerciseName.class);
    classMap.put(31, WarmUpExerciseName.class);
    classMap.put(32, RunExerciseName.class);

    Map<String, int[]> map = new HashMap<>();

    String categoryName = "";
    for(int categoryID = 0; true; categoryID++){
      categoryName = ExerciseCategory.getStringFromValue(categoryID);
      if ("".equals(categoryName)){
        break;
      }
      for(int exerciseID = 0; true; exerciseID++){
        Class<?> exerciseNameClass = classMap.get(categoryID);
        Method method = exerciseNameClass.getMethod("getStringFromValue", Integer.class);
        String exerciseName = String.valueOf(method.invoke(null, exerciseID));
        if ("".equals(exerciseName)){
          break;
        }
        map.put(exerciseName, new int[]{categoryID, exerciseID} );
      }
    }
    return map;
  }

  private ImmutablePair<List<Activity>, Set<String>> _parseFileNotesIntoActivities(List<FitNotesSet> setsList) throws Exception {
    List<Activity> result = new ArrayList<>();
    Map<String, List<ActivitySet>> mapDateToSets = new HashMap<>();

    Set<String> unsupportedExercises = new HashSet<>();
    for (FitNotesSet singleSet : setsList) {
      ActivitySet parsedSet = convertFromFitNotesSet(singleSet);
      if (parsedSet == null){
        unsupportedExercises.add(singleSet.getExercise());
      }

      if (mapDateToSets.containsKey(singleSet.getDate())) {
        mapDateToSets.get(singleSet.getDate()).add(parsedSet);
      } else {
        List<ActivitySet> newList = new ArrayList<>(1);
        newList.add(parsedSet);
        mapDateToSets.put(singleSet.getDate(), newList);
      }
    }

    for (Map.Entry<String, List<ActivitySet>> mapEntry : mapDateToSets.entrySet()) {
      Activity activity = new Activity(
              DataConverter.convertDateTime(mapEntry.getKey()), mapEntry.getValue());
      result.add(activity);
    }

    ImmutablePair<List<Activity>, Set<String>> pair = new ImmutablePair<>(result, unsupportedExercises);
    return pair;
  }


  /**
   * Parses a FitNotes CSV file, and creates a list of Activities encodable into the FIT format
   *
   * @param filepath
   * @return List<Activity>
   * @throws Exception
   */
  public ImmutablePair<List<Activity>, Set<String>> parseFileNotesIntoActivities(String filepath) throws Exception {
    List<FitNotesSet> setsList = readFileNotesSets(filepath);
    return _parseFileNotesIntoActivities(setsList);
  }

  /**
   * Parses a FitNotes CSV file, and creates a list of Activities encodable into the FIT format
   *
   * @param fileContent
   * @return List<Activity>
   * @throws Exception
   */

  public ImmutablePair<List<Activity>, Set<String>> parseFileNotesIntoActivities(byte[] fileContent) throws Exception {
    List<FitNotesSet> setsList = readFileNotesSets(fileContent);
    return _parseFileNotesIntoActivities(setsList);
  }


  /**
   * Converts a parsed FitNotes set object into an Activity set, encodable into the FIT format
   *
   * @param fitNotesSet
   * @return ActivitySet
   */
  public ActivitySet convertFromFitNotesSet(FitNotesSet fitNotesSet) {
    if (!this.exercise_to_fit_category_map.containsKey(fitNotesSet.getExercise())) {
      return null;
    }
    ActivitySet result = new ActivitySet();
    int[] exCategorySubcategory = this.exercise_to_fit_category_map.get(fitNotesSet.getExercise());

    result.setCategory(exCategorySubcategory[0]);
    result.setSubCategory(exCategorySubcategory[1]);
    result.setWeight(fitNotesSet.getWeight());
    result.setReps(fitNotesSet.getReps());
    // TODO: check out time -> duration possibility

    return result;
  }

  private static List<FitNotesSet> _readFileNotesSets(Reader reader) throws Exception {
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

  /**
   * Reads a FitNotes csv file into a list of FitNotes sets
   *
   * @param filepath
   * @return List<FitNotesSet>
   * @throws Exception
   */
  public static List<FitNotesSet> readFileNotesSets(String filepath) throws Exception {
    Reader reader = new BufferedReader(new FileReader(filepath));
    return _readFileNotesSets(reader);
  }

  /**
   * Reads a FitNotes csv file into a list of FitNotes sets
   *
   * @param fileContent
   * @return List<FitNotesSet>
   * @throws Exception
   */
  public static List<FitNotesSet> readFileNotesSets(byte[] fileContent) throws Exception {
    Reader reader = new StringReader(new String(fileContent));
    return _readFileNotesSets(reader);
  }

}
