package com.example.runislife;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.runislife.adapters.ExercisesAdapter;

public class SelectExerciseActivity extends AppCompatActivity {

    private int position = 0;
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_exercise);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            position = extras.getInt("position");
            type = extras.getInt("type");
        }
        else{
            finish();
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        RecyclerView exercise_list =  findViewById(R.id.exercise_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        exercise_list.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, manager.getOrientation());
        exercise_list.addItemDecoration(dividerItemDecoration);
        ExercisesAdapter adapter = new ExercisesAdapter(new ExercisesAdapter.ExercisesCallback() {
            @Override
            public void onClicked(int id, String title, String description, int value, int exercise_type) {
                Intent intent = new Intent();
                intent.putExtra("position", position);
                intent.putExtra("type", type);
                intent.putExtra("id", id);
                intent.putExtra("title", title);
                intent.putExtra("description", description);
                intent.putExtra("value", value);
                intent.putExtra("exercise_type", exercise_type);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        exercise_list.setAdapter(adapter);
        Run.instance.getDatabase().exerciseDao().getAll().observe(this, items -> {
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

}
