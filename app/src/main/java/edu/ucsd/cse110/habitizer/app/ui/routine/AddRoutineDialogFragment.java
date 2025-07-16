package edu.ucsd.cse110.habitizer.app.ui.routine;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentAddRoutineBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class AddRoutineDialogFragment extends DialogFragment {
    private FragmentAddRoutineBinding view;
    private MainViewModel activityModel;

    public static AddRoutineDialogFragment newInstance() {
        return new AddRoutineDialogFragment();
    }

    @Override
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
        this.view = FragmentAddRoutineBinding.inflate(getLayoutInflater());

        return new AlertDialog.Builder(requireContext())
                .setTitle("Add New Routine")
                .setView(view.getRoot())
                .setPositiveButton("Add", (dialog, which) -> {
                    var routineName = view.newRoutineName.getText().toString();
                    if (!routineName.isEmpty()) {
                        List<Task> emptyTasks = new ArrayList<>();
                        var newRoutine = new Routine(null, routineName, emptyTasks);
                        activityModel.addRoutine(newRoutine);
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                })
                .create();
    }
}
