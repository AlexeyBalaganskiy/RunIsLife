package com.example.runislife;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TrainingDescriptionFragment extends Fragment {

    public interface SavingCallback{
        void onSave(String title, String form, String description);
    }

    private TextInputEditText title, form, description;
    private SavingCallback savingCallback;

    public TrainingDescriptionFragment() {
    }

    public void setSavingCallback(SavingCallback savingCallback) {
        this.savingCallback = savingCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training_description, container, false);
        title = view.findViewById(R.id.title);
        form = view.findViewById(R.id.form);
        description = view.findViewById(R.id.description);
        FloatingActionButton save = view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savingCallback.onSave(title.getText().toString(), form.getText().toString(), description.getText().toString());
            }
        });
        return view;
    }


}
