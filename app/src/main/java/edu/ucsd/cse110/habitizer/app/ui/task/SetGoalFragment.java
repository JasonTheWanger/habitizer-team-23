package edu.ucsd.cse110.habitizer.app.ui.task;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentSetGoalTimeBinding;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentTaskListBinding;

public class SetGoalFragment extends DialogFragment{
    private FragmentSetGoalTimeBinding view;
    private FragmentTaskListBinding list_view;
    private MainViewModel activityModel;

    SetGoalFragment() {

    }

    public static SetGoalFragment newInstance() {
        var fragment = new SetGoalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentSetGoalTimeBinding.inflate(getLayoutInflater());
        this.list_view = FragmentTaskListBinding.inflate(getLayoutInflater());

        return new AlertDialog.Builder(getActivity())
                .setTitle("Set Goal Time")
                .setMessage("Please enter the goal time.")
                .setView(view.getRoot())
                .setPositiveButton("Create", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        var goal_time_string = view.newGoalTime.getText().toString();
        int goalTime = Integer.parseInt(goal_time_string);
        activityModel.setRoutineGoalTime(goalTime);

        requireActivity().runOnUiThread(() -> {
            list_view.goalTimeDisplay.setText(formatTime(goalTime));
        });
        dialog.dismiss();
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d", minutes, sec);
    }

}
