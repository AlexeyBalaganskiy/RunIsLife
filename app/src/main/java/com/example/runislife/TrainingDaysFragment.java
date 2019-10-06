package com.example.runislife;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.runislife.adapters.EditDaysAdapter;
import com.example.runislife.data.TrainingDayItem;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class TrainingDaysFragment extends Fragment {

    public final int ADD_BEFORE = 101;
    public final int ADD_AFTER = 102;
    public final int ADD_AT_END = 103;

    private List<TrainingDayItem> items = new ArrayList<>();
    private EditDaysAdapter adapter;


    public TrainingDaysFragment() {
    }

    public List<TrainingDayItem> getItems() {
        return items;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training_days, container, false);
        FloatingActionButton fab = view.findViewById(R.id.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSelecting(0, 0, ADD_AT_END);
            }
        });
        RecyclerView plans_list = (RecyclerView) view.findViewById(R.id.days_list);
        LinearLayoutManager manager = new LinearLayoutManager(requireContext());
        plans_list.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), manager.getOrientation());
        plans_list.addItemDecoration(dividerItemDecoration);
        adapter = new EditDaysAdapter(items, new EditDaysAdapter.EditDaysCallback() {
            @Override
            public void addAfter(int position, int type) {
                startSelecting(position, type, ADD_AFTER);
            }

            @Override
            public void addBefore(int position, int type) {
                startSelecting(position, type, ADD_BEFORE);
            }

            @Override
            public void delete(int position, int type) {
                TrainingDaysFragment.this.delete(position, type);
            }
        });
        plans_list.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==ADD_BEFORE && resultCode==RESULT_OK){
            addBefore(data);
        }
        else if(requestCode==ADD_AFTER && resultCode==RESULT_OK){
            addAfter(data);
        }
        else if(requestCode==ADD_AT_END && resultCode==RESULT_OK){
            addDayAtEnd(data);
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void startSelecting(int position, int type, int code){
        Intent intent = new Intent(requireActivity(), SelectExerciseActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("type", type);
        startActivityForResult(intent, code);
    }

    private TrainingDayItem getExercise(Intent data){
        int id = data.getIntExtra("id", 0);
        String title = data.getStringExtra("title");
        String description = data.getStringExtra("description");
        int value = data.getIntExtra("value",0);
        int exercise_type = data.getIntExtra("exercise_type", 0);
        return new TrainingDayItem(id, title, description, value, TrainingDayItem.TYPE_EXERCISE, exercise_type);
    }

    private void addAfter(Intent data){
        int position = data.getIntExtra("position", 0);
        int type = data.getIntExtra("type", TrainingDayItem.TYPE_DAY);
        if(type == TrainingDayItem.TYPE_DAY){
            for(int i = position+1; i<items.size(); i++){
                if(items.get(i).getType() == TrainingDayItem.TYPE_DAY){
                    TrainingDayItem day = new TrainingDayItem(TrainingDayItem.TYPE_DAY);
                    items.add(i+1,day);
                    TrainingDayItem exercise = getExercise(data);
                    items.add(i+1,exercise);
                    calculateDays();
                    break;
                }
            }
        }
        else{
            TrainingDayItem exercise = getExercise(data);
            items.add(position+1,exercise);
        }
        adapter.notifyDataSetChanged();
    }

    private void addBefore(Intent data){
        int position = data.getIntExtra("position", 0);
        int type = data.getIntExtra("type", TrainingDayItem.TYPE_DAY);
        if(type == TrainingDayItem.TYPE_DAY){
            TrainingDayItem day = new TrainingDayItem(TrainingDayItem.TYPE_DAY);
            items.add(position,day);
            TrainingDayItem exercise = getExercise(data);
            items.add(position+1,exercise);
            calculateDays();
        }
        else{
            TrainingDayItem exercise = getExercise(data);
            items.add(position,exercise);
        }
        adapter.notifyDataSetChanged();
    }

    private void delete(int position, int type) {
        if(type == TrainingDayItem.TYPE_DAY){
            items.remove(position);
            while(items.size()>position+1 && items.get(position).getType() != TrainingDayItem.TYPE_DAY){
                items.remove(position);
            }
        }
        else{
            items.remove(position);
        }
        removeEmptyDays();
        calculateDays();
        adapter.notifyDataSetChanged();
    }

    private void addDayAtEnd(Intent data){
        TrainingDayItem day = new TrainingDayItem(TrainingDayItem.TYPE_DAY);
        TrainingDayItem exercise = getExercise(data);
        items.add(day);
        items.add(exercise);
        calculateDays();
        adapter.notifyDataSetChanged();
    }

    private void calculateDays(){
        int count = 1;
        for(TrainingDayItem item : items){
            if(item.getType() == TrainingDayItem.TYPE_DAY){
                item.setTitle("День " + String.valueOf(count));
                count ++;
            }
        }
    }

    private void removeEmptyDays(){
        int i = 0;
        if(items.size() == 1){
            items.clear();
        }
        else{
            while(i < items.size()-1){
                if(items.get(i).getType() == TrainingDayItem.TYPE_DAY && items.get(i+1).getType() == TrainingDayItem.TYPE_DAY){
                    items.remove(i);
                }
                else{
                    i++;
                }
            }
        }
    }

}
