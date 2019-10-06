package com.example.runislife.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.runislife.R;
import com.example.runislife.data.Exercise;
import com.example.runislife.data.TrainingDayItem;

import java.util.List;

import static com.example.runislife.data.TrainingDayItem.TYPE_DAY;
import static com.example.runislife.data.TrainingDayItem.TYPE_EXERCISE;

public class EditDaysAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface EditDaysCallback {
        void addAfter(int position, int type);
        void addBefore(int position, int type);
        void delete(int position, int type);
    }

    private List<TrainingDayItem> items;
    private EditDaysCallback callback;

    public EditDaysAdapter(List<TrainingDayItem> items, EditDaysCallback callback) {
        this.callback = callback;
        this.items = items;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TrainingDayItem.TYPE_DAY){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_edit_item, parent, false);
            return new DayViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_edit_item, parent, false);
            return new ExerciseViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TrainingDayItem item = items.get(position);
        if(holder instanceof DayViewHolder){
            ((DayViewHolder) holder).title.setText(item.getTitle());

        }else if(holder instanceof ExerciseViewHolder){
            ((ExerciseViewHolder) holder).title.setText(item.getTitle());
            ((ExerciseViewHolder) holder).description.setText(item.getDescription());
            ((ExerciseViewHolder) holder).value.setText(getValue(item));
        }
    }

    private String getValue(TrainingDayItem item){
        if(item.getExercise_type() == Exercise.DISTANCE){
            return "Дистанция: " + String.valueOf(item.getValue()) + " м";
        }
        else if(item.getExercise_type() == Exercise.TIME){
            return "Время: " + String.valueOf(item.getValue()) + " минут";
        }
        else{
            return "Повторить" + String.valueOf(item.getValue()) + " раз";
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    public class DayViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        Button addbefore, addafter, delete;

        public DayViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            addafter = itemView.findViewById(R.id.addafter);
            addbefore = itemView.findViewById(R.id.addbefore);
            delete = itemView.findViewById(R.id.delete);
            addafter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.addAfter(getAdapterPosition(), TYPE_DAY);
                }
            });
            addbefore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.addBefore(getAdapterPosition(), TYPE_DAY);
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.delete(getAdapterPosition(), TYPE_DAY);
                }
            });
        }

    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder{

        TextView title, description, value;
        Button addbefore, addafter, delete;

        public ExerciseViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            value = itemView.findViewById(R.id.value);
            addafter = itemView.findViewById(R.id.addafter);
            addbefore = itemView.findViewById(R.id.addbefore);
            delete = itemView.findViewById(R.id.delete);
            addafter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.addAfter(getAdapterPosition(), TYPE_EXERCISE);
                }
            });
            addbefore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.addBefore(getAdapterPosition(), TYPE_EXERCISE);
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.delete(getAdapterPosition(), TYPE_EXERCISE);
                }
            });
        }

    }

}
