package com.example.runislife.data;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DayWithExercises {

    @Embedded
    public TrainingDay day;

    @Relation(parentColumn = "id_day", entityColumn = "id_day", entity = TrainingDayExercise.class)
    public List<ExerciseWithDescription> exercises;

    public List<ExerciseWithDescription> getSortedExercises(){
        List<ExerciseWithDescription> result = new ArrayList<>(exercises);
        Collections.sort(result, new Comparator<ExerciseWithDescription>() {
            @Override
            public int compare(final ExerciseWithDescription object1, final ExerciseWithDescription object2) {
                return object1.trainingDayExercise.getId() - object2.trainingDayExercise.getId();
            }
        });
        return result;
    }

}
