package edu.ucsd.cse110.habitizer.app.ui.task;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentDialogRenameTaskBinding;

public class RenameTaskDialogFragment extends DialogFragment {
    private MainViewModel viewModel;
    private int taskId;
    private FragmentDialogRenameTaskBinding binding;

    public static RenameTaskDialogFragment newInstance(int taskId) {
        RenameTaskDialogFragment fragment = new RenameTaskDialogFragment();
        Bundle args = new Bundle();
        args.putInt("task_id", taskId);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        taskId = requireArguments().getInt("task_id");

        binding = FragmentDialogRenameTaskBinding.inflate(LayoutInflater.from(getContext()));

        return new AlertDialog.Builder(requireContext())
                .setTitle("Rename Task")
                .setView(binding.getRoot())
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = binding.taskNameEditText.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        viewModel.renameTask(taskId, newName);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
    }

}

