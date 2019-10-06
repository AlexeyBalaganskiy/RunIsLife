package com.example.runislife.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {Exercise.class, TrainingDay.class, TrainingPlan.class, TrainingDayExercise.class}, version = 5, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract TrainingPlanDao trainingPlanDao();
    public abstract ExerciseDao exerciseDao();
    public abstract TrainingDayDao trainingDayDao();
    public abstract TrainingDayExerciseDao trainingDayExerciseDao();

}
