package com.example.runislife;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.runislife.data.TrainingPlan;

import java.util.ArrayList;
import java.util.List;

public class TrainingPlanAdapter extends RecyclerView.Adapter<TrainingPlanAdapter.ViewHolder> {

    public interface TrainingPlanCallback{
        void onPlanDeleted(int id);
        void onPlanClicked(int id);
        void onButtonClicked(int id);
    }

    private List<TrainingPlan> items = new ArrayList<>();
    private TrainingPlanCallback callback;

    public TrainingPlanAdapter(TrainingPlanCallback callback) {
        this.callback = callback;
    }

    public void setItems(List<TrainingPlan> items){
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    @NonNull
    public TrainingPlanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.training_plan_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingPlanAdapter.ViewHolder holder, int position) {
        TrainingPlan plan = items.get(position);
        holder.title.setText(plan.getTitle());
        holder.form_training.setText(plan.getForm_training());
        holder.description.setText(plan.getDescription());
        holder.count_training.setText(plan.getCount_training() + " дней");
        if(plan.isAdded()){
            holder.add.setText("Удалить из моего плана");
        }
        if(!plan.isCustom() || plan.isAdded()){
            holder.delete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, form_training, description, count_training;
        Button add, delete;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onPlanClicked(items.get(getAdapterPosition()).getId_training());
                }
            });
            title = itemView.findViewById(R.id.title);
            form_training = itemView.findViewById(R.id.form_training);
            description = itemView.findViewById(R.id.description);
            count_training = itemView.findViewById(R.id.count_training);
            add = itemView.findViewById(R.id.add);
            delete = itemView.findViewById(R.id.delete);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onButtonClicked(items.get(getAdapterPosition()).getId_training());
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onPlanDeleted(items.get(getAdapterPosition()).getId_training());
                }
            });
        }

    }

}
