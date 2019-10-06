package com.example.runislife.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface ExerciseDao {

    @Insert(onConflict = IGNORE)
    void insert(List<Exercise> exercises);

    @Query("SELECT COUNT(*) FROM exercise")
    int getCount();

    @Query("SELECT * FROM exercise ORDER BY id_exercise")
    LiveData<List<Exercise>> getAll();
}
