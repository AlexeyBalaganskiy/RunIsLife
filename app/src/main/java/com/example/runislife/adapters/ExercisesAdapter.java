package com.example.runislife.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.runislife.R;
import com.example.runislife.data.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ViewHolder> {

    public interface ExercisesCallback {
        void onClicked(int id, String title, String description, int value, int exercise_type);
    }

    private List<Exercise> items = new ArrayList<>();
    private ExercisesCallback callback;

    public ExercisesAdapter(ExercisesCallback callback) {
        this.callback = callback;
    }

    public void setItems(List<Exercise> items){
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    @NonNull
    public ExercisesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_select_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ExercisesAdapter.ViewHolder holder, int position) {
        Exercise exercise = items.get(position);
        holder.title.setText(exercise.getTitle());
        holder.description.setText(exercise.getDescription());
        holder.value.setText(getValue(exercise));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private String getValue(Exercise exercise){
        if(exercise.getType() == Exercise.DISTANCE){
            return "Дистанция: " + String.valueOf(exercise.getValue()) + " м";
        }
        else if(exercise.getType() == Exercise.TIME){
            return "Время: " + String.valueOf(exercise.getValue()) + " сек";
        }
        else{
            return "Повторить " + String.valueOf(exercise.getValue()) + " раз";
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, description, value;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Exercise exercise = items.get(getAdapterPosition());
                    callback.onClicked(exercise.getId_exercise(), exercise.getTitle(), exercise.getDescription(), exercise.getValue(), exercise.getType());
                }
            });
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            value = itemView.findViewById(R.id.value);
        }

    }

}

