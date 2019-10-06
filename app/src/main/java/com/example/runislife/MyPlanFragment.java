package com.example.runislife;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyPlanFragment extends Fragment {

    private TrainingPlanAdapter adapter;

    public MyPlanFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_plan, container, false);
        RecyclerView plans_list = view.findViewById(R.id.plans_list);
        LinearLayoutManager manager = new LinearLayoutManager(requireContext());
        plans_list.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), manager.getOrientation());
        plans_list.addItemDecoration(dividerItemDecoration);
        adapter = new TrainingPlanAdapter(new TrainingPlanAdapter.TrainingPlanCallback() {
            @Override
            public void onPlanDeleted(int id) {

            }

            @Override
            public void onPlanClicked(int id) {
                Intent intent = new Intent(requireActivity(), PlanActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("preview", false);
                startActivity(intent);
            }

            @Override
            public void onButtonClicked(int id) {
                RemoveTask removeTask = new RemoveTask(id);
                removeTask.execute();
            }
        });
        plans_list.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Run.instance.getDatabase().trainingPlanDao().getAdded().observe(this, items -> {
            if(items != null){
                adapter.setItems(items);
            }
        });
    }

    class RemoveTask extends AsyncTask<Void, Void, Void> {

        private int id;

        public RemoveTask(int id) {
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Run.instance.getDatabase().trainingPlanDao().updateStatus(id, false);
            return null;
        }
    }
}
