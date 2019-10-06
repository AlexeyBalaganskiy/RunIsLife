package com.example.runislife;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.runislife.adapters.TrainingTabsAdapter;
import com.example.runislife.data.AppDatabase;
import com.example.runislife.data.TrainingDay;
import com.example.runislife.data.TrainingDayExercise;
import com.example.runislife.data.TrainingDayItem;
import com.example.runislife.data.TrainingPlan;

import java.util.List;

public class AddTrainingActivity extends AppCompatActivity {

    private TrainingDescriptionFragment tab1;
    private TrainingDaysFragment tab2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_training);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tab1 = new TrainingDescriptionFragment();
        tab1.setSavingCallback(new TrainingDescriptionFragment.SavingCallback() {
            @Override
            public void onSave(String title, String form, String description) {
                SaveTask saveTask = new SaveTask(title, form, description, tab2.getItems());
                saveTask.execute();
            }
        });
        tab2 = new TrainingDaysFragment();
        TabLayout tabLayout = findViewById(R.id.tablayout);
        ViewPager viewPager = findViewById(R.id.viewpager);
        TrainingTabsAdapter adapter = new TrainingTabsAdapter(getSupportFragmentManager(), tab1, tab2);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    class SaveTask extends AsyncTask<Void, Void, Void> {

        private String title;
        private String form;
        private String description;
        private List<TrainingDayItem> items;

        public SaveTask(String title, String form, String description, List<TrainingDayItem> items) {
            this.title = title;
            this.form = form;
            this.description = description;
            this.items = items;
        }

        @Override
        protected Void doInBackground(Void... params) {
            int count = 0;
            for(TrainingDayItem item : items){
                if(item.getType() == TrainingDayItem.TYPE_DAY){
                    count++;
                }
            }
            AppDatabase database = Run.instance.getDatabase();
            int id_plan = (int) database.trainingPlanDao().insert(
                    new TrainingPlan(title, form, count, description, false, true));
            int id_day=0;
            int day_number=1;
            for(TrainingDayItem item : items){
                if(item.getType() == TrainingDayItem.TYPE_DAY){
                    id_day = (int) database.trainingDayDao().insert(new TrainingDay(day_number, id_plan));
                    day_number++;
                }
                else{
                    database.trainingDayExerciseDao().insert(
                            new TrainingDayExercise(id_day, item.getId()));
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            AddTrainingActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AddTrainingActivity.this, "План добавлен!", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }
    }

}
