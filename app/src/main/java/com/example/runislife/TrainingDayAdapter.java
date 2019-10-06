package com.example.runislife;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.runislife.data.Exercise;
import com.example.runislife.data.TrainingDayItem;

import java.util.ArrayList;
import java.util.List;

public class TrainingDayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface TrainingDayCallback {
        void onStart(int id, String title, String description, int value, int type);
    }

    private List<TrainingDayItem> items = new ArrayList<>();
    private TrainingDayCallback callback;
    private boolean preview = true;
    private int currentId = -1;

    public TrainingDayAdapter(boolean preview, TrainingDayCallback callback) {
        this.callback = callback;
        this.preview = preview;
    }

    public void setCurrentId(int currentId) {
        this.currentId = currentId;
        notifyDataSetChanged();
    }

    public void setItems(List<TrainingDayItem> items){
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TrainingDayItem.TYPE_DAY){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item, parent, false);
            return new DayViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item, parent, false);
            return new ExerciseViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TrainingDayItem item = items.get(position);
        if(holder instanceof DayViewHolder){
            ((DayViewHolder) holder).description.setText(item.getDescription());
            ((DayViewHolder) holder).title.setText(item.getTitle());
            if(preview){
                ((DayViewHolder) holder).status.setVisibility(View.GONE);
                ((DayViewHolder) holder).description.setVisibility(View.GONE);
                ((DayViewHolder) holder).progressBar.setVisibility(View.GONE);
            }
            else if(item.getStatus() == TrainingDayItem.TrainingStatus.FINISHED){
                ((DayViewHolder) holder).status.setImageResource(R.drawable.ic_check_green_24dp);
                ((DayViewHolder) holder).progressBar.getProgressDrawable().setColorFilter(0xFF64DD17,android.graphics.PorterDuff.Mode.MULTIPLY);
            }
            else if(item.getStatus() == TrainingDayItem.TrainingStatus.SCHEDULED){
                ((DayViewHolder) holder).status.setImageResource(R.drawable.ic_schedule_yellow_24dp);
                ((DayViewHolder) holder).progressBar.getProgressDrawable().setColorFilter(0xFFFDD835,android.graphics.PorterDuff.Mode.MULTIPLY);
            }
            else if(item.getStatus() == TrainingDayItem.TrainingStatus.ACTIVE){
                ((DayViewHolder) holder).status.setImageResource(R.drawable.ic_play_arrow_blue_24dp);
                ((DayViewHolder) holder).progressBar.getProgressDrawable().setColorFilter(0xFF3F51B5,android.graphics.PorterDuff.Mode.MULTIPLY);
            }
            else if(item.getStatus() == TrainingDayItem.TrainingStatus.FAILED){
                ((DayViewHolder) holder).status.setImageResource(R.drawable.ic_not_interested_red_24dp);
                ((DayViewHolder) holder).progressBar.getProgressDrawable().setColorFilter(0xFFFF0000,android.graphics.PorterDuff.Mode.MULTIPLY);
            }
            ((DayViewHolder) holder).progressBar.setMax(item.getValue());
            ((DayViewHolder) holder).progressBar.setProgress(item.getProgress());
        }else if(holder instanceof ExerciseViewHolder){
            ((ExerciseViewHolder) holder).description.setText(item.getDescription());
            ((ExerciseViewHolder) holder).title.setText(item.getTitle());
            if(preview){
                ((ExerciseViewHolder) holder).value.setText(getValue(item));
            }
            else{
                ((ExerciseViewHolder) holder).value.setText(getValue(item) + getProgress(item));
            }
            if(currentId != item.getId() || preview || item.getStatus() == TrainingDayItem.TrainingStatus.ACTIVE){
                ((ExerciseViewHolder) holder).start.setVisibility(View.GONE);
            }
            else{
                ((ExerciseViewHolder) holder).start.setVisibility(View.VISIBLE);
            }
            if(preview){
                ((ExerciseViewHolder) holder).status.setVisibility(View.GONE);
            }
            else if(item.getStatus() == TrainingDayItem.TrainingStatus.FINISHED){
                ((ExerciseViewHolder) holder).status.setImageResource(R.drawable.ic_check_green_24dp);
            }
            else if(item.getStatus() == TrainingDayItem.TrainingStatus.SCHEDULED){
                ((ExerciseViewHolder) holder).status.setImageResource(R.drawable.ic_schedule_yellow_24dp);
            }
            else if(item.getStatus() == TrainingDayItem.TrainingStatus.ACTIVE){
                ((ExerciseViewHolder) holder).status.setImageResource(R.drawable.ic_play_arrow_blue_24dp);
            }
            else if(item.getStatus() == TrainingDayItem.TrainingStatus.FAILED){
                ((ExerciseViewHolder) holder).status.setImageResource(R.drawable.ic_not_interested_red_24dp);
            }
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

    private String getValue(TrainingDayItem item){
        if(item.getExercise_type() == Exercise.DISTANCE){

            return "Дистанция: " + String.valueOf(item.getValue()) + " м";
        }
        else if(item.getExercise_type() == Exercise.TIME){
            return "Время: " + String.valueOf(item.getValue()) + " c";
        }
        else{
            return "Повторить" + String.valueOf(item.getValue()) + " раз";
        }
    }

    private String getProgress(TrainingDayItem item){
        if(item.getExercise_type() == Exercise.DISTANCE){

            return " / Осталось: " + String.valueOf(item.getValue()-item.getProgress()) + " м";
        }
        else if(item.getExercise_type() == Exercise.TIME){
            return " / Осталось: " + String.valueOf(item.getValue()-item.getProgress()) + " c";
        }
        else{
            return "";
        }
    }

    public class DayViewHolder extends RecyclerView.ViewHolder{

        TextView title, description;
        ImageView status;
        ProgressBar progressBar;

        public DayViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            status = itemView.findViewById(R.id.status);
            progressBar = itemView.findViewById(R.id.progress);
        }

    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder{

        TextView title, description, value;
        ImageView status;
        Button start;

        public ExerciseViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            value = itemView.findViewById(R.id.value);
            status = itemView.findViewById(R.id.status);
            start = itemView.findViewById(R.id.start);
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    start.setVisibility(View.GONE);
                    TrainingDayItem item = items.get(getAdapterPosition());
                    callback.onStart(item.getId(), item.getTitle(), item.getDescription(), item.getValue(), item.getExercise_type());
                }
            });
        }

    }

}
