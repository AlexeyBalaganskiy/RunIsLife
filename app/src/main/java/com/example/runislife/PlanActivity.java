package com.example.runislife;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.runislife.data.DayWithExercises;
import com.example.runislife.data.Exercise;
import com.example.runislife.data.ExerciseWithDescription;
import com.example.runislife.data.TrainingDayItem;
import com.example.runislife.services.DistanceService;
import com.example.runislife.services.TimerService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.runislife.data.Exercise.DISTANCE;
import static com.example.runislife.data.Exercise.TIME;

public class PlanActivity extends AppCompatActivity {

    private TrainingDayAdapter adapter;
    private int id_plan = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        Bundle extras = getIntent().getExtras();
        int id = extras.getInt("id", 1);
        id_plan = id;
        boolean preview = extras.getBoolean("preview", true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        RecyclerView days_list = findViewById(R.id.days_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        days_list.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, manager.getOrientation());
        days_list.addItemDecoration(dividerItemDecoration);
        adapter = new TrainingDayAdapter(preview, new TrainingDayAdapter.TrainingDayCallback() {

            @Override
            public void onStart(int id, String title, String description, int value, int type) {
                if(type == TIME){
                    startTimer(id, title, description, value);
                }
                else if(type == DISTANCE){
                    startRun(id, title, description, value);
                }
                else{
                    PassTask passTask = new PassTask(id);
                    passTask.execute();
                }
            }

        });
        days_list.setAdapter(adapter);
        Run.instance.getDatabase().trainingPlanDao().getPlanName(id).observe(this, name -> {
            if(name != null){
                getSupportActionBar().setTitle(name);
            }
        });
        Run.instance.getDatabase().trainingDayDao().getDays(id).observe(this, items -> {
            if(items != null){
                processDays(items);
            }
        });
        Run.instance.getDatabase().trainingDayExerciseDao().getCurrentId(id).observe(this, result -> {
            if(result != null){
                adapter.setCurrentId(result);
            }
            else{
                adapter.setCurrentId(-1);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void startTimer(int id, String title, String description, int value){
        Intent intent = new Intent(this, TimerService.class);
        intent.putExtra("id_plan", id_plan);
        intent.putExtra("id", id);
        intent.putExtra("value", value);
        intent.putExtra("title", title);
        intent.putExtra("description", description);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    private void startRun(int id, String title, String description, int value){
        Intent intent = new Intent(this, DistanceService.class);
        intent.putExtra("id_plan", id_plan);
        intent.putExtra("id", id);
        intent.putExtra("value", value);
        intent.putExtra("title", title);
        intent.putExtra("description", description);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    private void processDays(List<DayWithExercises> days){
        String pattern = "dd-MM-yyyy";
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        List<TrainingDayItem> items = new ArrayList<>();
        for(DayWithExercises day : days){
            String date = "";
            if(day.day.getDate() != null){
                date = dateFormat.format(day.day.getDate());
            }
            TrainingDayItem.TrainingStatus status = TrainingDayItem.TrainingStatus.SCHEDULED;
            int progress = 0;
            for(ExerciseWithDescription exercise : day.exercises){
                if(exercise.trainingDayExercise.getStatus() == 1){
                    progress++;
                }
            }
            if(progress > 0 && progress < day.exercises.size()){
                status = TrainingDayItem.TrainingStatus.ACTIVE;
            }
            else if(progress == day.exercises.size()){
                status = TrainingDayItem.TrainingStatus.FINISHED;
            }
            items.add(new TrainingDayItem(day.day.getId_day(),
                    "День " + String.valueOf(day.day.getDay_number()), date,
                    status, TrainingDayItem.TYPE_DAY,
                    day.exercises.size(), progress));
            for(ExerciseWithDescription exercise : day.getSortedExercises()){
                Exercise ex = exercise.exercises.get(0);
                TrainingDayItem.TrainingStatus exStatus = TrainingDayItem.TrainingStatus.SCHEDULED;
                if(exercise.trainingDayExercise.getStatus() == 1){
                    exStatus = TrainingDayItem.TrainingStatus.FINISHED;
                }
                else if(exercise.trainingDayExercise.getStatus() == 2){
                    exStatus = TrainingDayItem.TrainingStatus.ACTIVE;
                }
                items.add(new TrainingDayItem(exercise.trainingDayExercise.getId(), ex.getId_exercise(),
                        ex.getTitle(), ex.getDescription(), ex.getValue(), exStatus,
                        TrainingDayItem.TYPE_EXERCISE, ex.getType(),exercise.trainingDayExercise.getProgress()));
            }
        }
        adapter.setItems(items);
    }

    class PassTask extends AsyncTask<Void, Void, Void> {

        private int id;

        public PassTask(int id) {
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Run.instance.getDatabase().trainingDayExerciseDao().passExercise(id);
            return null;
        }
    }

}
