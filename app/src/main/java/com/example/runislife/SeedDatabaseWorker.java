package com.example.runislife;

import android.content.Context;
import android.support.annotation.NonNull;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.runislife.data.AppDatabase;
import com.example.runislife.data.Exercise;
import com.example.runislife.data.TrainingDay;
import com.example.runislife.data.TrainingDayExercise;
import com.example.runislife.data.TrainingPlan;

import java.util.ArrayList;
import java.util.List;

public class SeedDatabaseWorker extends Worker {

    public SeedDatabaseWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        AppDatabase database = Run.instance.getDatabase();
        List<TrainingPlan> items = new ArrayList<>();
        items.add(new TrainingPlan(1, "Тренировка #1", "Для новичков", 3, "Описание тренировки 1", false, false));
        items.add(new TrainingPlan(2, "Тренировка #2", "Для спринтёров", 3, "Описание тренировки 2", false, false));
        items.add(new TrainingPlan(3, "Тренировка #3", "Для стаеров", 3, "Описание тренировки 3", false,false));
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(new Exercise(1,"Бег 500", "Пробежать 500 метров", Exercise.DISTANCE, 500));
        exercises.add(new Exercise(7, "Бег 1500", "Пробежать 1500 метров", Exercise.DISTANCE, 1500));
        exercises.add(new Exercise(2, "Беговая дорожка", "Бег на дорожке", Exercise.TIME, 10));
        exercises.add(new Exercise(3, "Отжимания", "Отжаться 10 раз", Exercise.ACTION, 10));
        exercises.add(new Exercise(4,"Бег 3000", "Пробежать 3 км", Exercise.DISTANCE, 3000));
        exercises.add(new Exercise(5, "Ускорения", "Ускорориться на 100м", Exercise.ACTION, 3));
        exercises.add(new Exercise(6, "Обще-развивающие упражнения", "Необходимо сделать специальные упражнениядля рук, ног, туловища, шеи и других частей тела", Exercise.ACTION, 1));
        database.trainingPlanDao().insert(items);
        database.exerciseDao().insert(exercises);
        List<TrainingDay> days = new ArrayList<>();
        days.add(new TrainingDay(1, 1, 1));
        days.add(new TrainingDay(2, 2, 1));
        days.add(new TrainingDay(3, 3, 1));
        database.trainingDayDao().insert(days);
        List<TrainingDayExercise> trainingDayExercises = new ArrayList<>();
        trainingDayExercises.add(new TrainingDayExercise(1, 1, 1));
        trainingDayExercises.add(new TrainingDayExercise(2, 1, 1));
        trainingDayExercises.add(new TrainingDayExercise(3, 2, 2));
        trainingDayExercises.add(new TrainingDayExercise(4, 2, 2));
        trainingDayExercises.add(new TrainingDayExercise(5, 3, 3));
        trainingDayExercises.add(new TrainingDayExercise(6, 3, 3));
        database.trainingDayExerciseDao().insert(trainingDayExercises);
        return Result.success();
    }
}
