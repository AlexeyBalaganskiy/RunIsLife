package com.example.runislife.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface TrainingPlanDao {

    @Insert(onConflict = IGNORE)
    long insert(TrainingPlan trainingPlan);

    @Insert(onConflict = IGNORE)
    void insert(List<TrainingPlan> trainingPlans);

    @Query("SELECT COUNT(*) FROM trainingplan")
    int getCount();

    @Query("SELECT * FROM trainingplan WHERE added = 0 ORDER BY id_training")
    LiveData<List<TrainingPlan>> getAll();

    @Query("SELECT * FROM trainingplan WHERE added = 1 ORDER BY id_training")
    LiveData<List<TrainingPlan>> getAdded();

    @Query("UPDATE trainingplan SET added = :added WHERE id_training = :id")
    void updateStatus(int id, boolean added);

    @Query("SELECT title FROM trainingplan WHERE id_training = :id")
    LiveData<String> getPlanName(int id);

    @Query("DELETE FROM trainingplan WHERE id_training=:id")
    void delete(int id);

}
