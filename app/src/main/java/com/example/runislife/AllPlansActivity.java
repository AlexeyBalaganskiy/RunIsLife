package com.example.runislife;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.runislife.data.AppDatabase;
import com.example.runislife.data.TrainingDay;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AllPlansActivity extends AppCompatActivity {

    private TrainingPlanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_plans);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        RecyclerView plans_list =  findViewById(R.id.plans_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        plans_list.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, manager.getOrientation());
        plans_list.addItemDecoration(dividerItemDecoration);
        adapter = new TrainingPlanAdapter(new TrainingPlanAdapter.TrainingPlanCallback() {

            @Override
            public void onPlanDeleted(int id) {
                DeleteTask deleteTask = new DeleteTask(id);
                deleteTask.execute();
            }

            @Override
            public void onPlanClicked(int id) {
                Intent intent = new Intent(AllPlansActivity.this, PlanActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("preview", true);
                startActivity(intent);
            }

            @Override
            public void onButtonClicked(int id) {
                AddTask addTask = new AddTask(id);
                addTask.execute();
            }
        });
        plans_list.setAdapter(adapter);
        Run.instance.getDatabase().trainingPlanDao().getAll().observe(this, items -> {
            if(items != null){
                adapter.setItems(items);
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

    class AddTask extends AsyncTask<Void, Void, Void> {

        private int id;

        public AddTask(int id) {
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... params) {
            AppDatabase database = Run.instance.getDatabase();
            database.trainingDayExerciseDao().resetExercises(id);
            List<TrainingDay> days = database.trainingDayDao().getTrainingDays(id);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            for(TrainingDay day : days){
                calendar.add(Calendar.DATE, 1);
                database.trainingDayDao().setDate(new Date(calendar.getTimeInMillis()), day.getId_day());
            }
            database.trainingPlanDao().updateStatus(id, true);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            AllPlansActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(AllPlansActivity.this, PlanActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("preview", false);
                    startActivity(intent);
                }
            });
        }
    }

    class DeleteTask extends AsyncTask<Void, Void, Void> {

        private int id;

        public DeleteTask(int id) {
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Run.instance.getDatabase().trainingPlanDao().delete(id);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            AllPlansActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }


}
