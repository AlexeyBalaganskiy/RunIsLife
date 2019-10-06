package com.example.runislife.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface TrainingDayDao {

    @Insert(onConflict = IGNORE)
    long insert(TrainingDay trainingDay);

    @Insert(onConflict = IGNORE)
    void insert(List<TrainingDay> trainingDays);

    @Query("SELECT * FROM trainingday WHERE id_training = :id")
    LiveData<List<DayWithExercises>> getDays(int id);

    @Query("SELECT * FROM trainingday WHERE id_training = :id ORDER BY day_number")
    List<TrainingDay> getTrainingDays(int id);

    @Query("UPDATE trainingday SET date = :date WHERE id_day = :id")
    void setDate(Date date, int id);

}
