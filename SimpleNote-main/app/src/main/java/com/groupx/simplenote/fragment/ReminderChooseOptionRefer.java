package com.groupx.simplenote.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.groupx.simplenote.R;
import com.groupx.simplenote.activity.CreateReminderActivity;

public class ReminderChooseOptionRefer extends BottomSheetDialogFragment {
    private CreateReminderActivity activity;

    public ReminderChooseOptionRefer(CreateReminderActivity activity) {
        super();
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.layout_note_reminder_detail, container, false);
    }
}
