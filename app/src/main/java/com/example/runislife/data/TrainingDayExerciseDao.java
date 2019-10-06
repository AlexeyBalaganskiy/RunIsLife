package com.example.runislife.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface TrainingDayExerciseDao {

    @Insert(onConflict = IGNORE)
    long insert(TrainingDayExercise trainingDayExercise);

    @Insert(onConflict = IGNORE)
    void insert(List<TrainingDayExercise> trainingDayExercises);

    @Query("SELECT id FROM trainingdayexercise INNER JOIN trainingday ON trainingday.id_day = trainingdayexercise.id_day " +
            "WHERE trainingday.id_training = :id_training AND (trainingdayexercise.status = 0 OR trainingdayexercise.status = 2)" +
            "ORDER BY trainingday.day_number,trainingdayexercise.id " +
            "LIMIT 1")
    LiveData<Integer> getCurrentId(int id_training);

    @Query("UPDATE trainingdayexercise SET status = 1 WHERE id = :id")
    void passExercise(int id);

    @Query("UPDATE trainingdayexercise SET status = 2 WHERE id = :id")
    void startExercise(int id);

    @Query("UPDATE trainingdayexercise SET status = 0 WHERE id = :id")
    void resetExercise(int id);

    @Query("SELECT progress FROM trainingdayexercise WHERE id = :id")
    int getProgress(int id);

    @Query("UPDATE trainingdayexercise SET progress = :progress WHERE id = :id")
    void setProgress(int progress, int id);

    @Query("UPDATE trainingdayexercise SET status=0, progress = 0 WHERE id IN (" +
            "SELECT id FROM trainingdayexercise INNER JOIN trainingday ON trainingday.id_day = trainingdayexercise.id_day " +
            "WHERE trainingday.id_training = :id_training)")
    void resetExercises(int id_training);

}
