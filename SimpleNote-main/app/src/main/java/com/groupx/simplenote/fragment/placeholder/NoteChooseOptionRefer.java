package com.groupx.simplenote.fragment.placeholder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.groupx.simplenote.R;
import com.groupx.simplenote.activity.CreateNoteActivity;
import com.groupx.simplenote.activity.CreateReminderActivity;

public class NoteChooseOptionRefer extends BottomSheetDialogFragment {
    private CreateNoteActivity activity;
    private View layoutTakePhoto, layoutChoosePhoto, layoutDrawAble;

    public NoteChooseOptionRefer(CreateNoteActivity activity) {
        super();
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.layout_note_reminder_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view != null) {
            layoutTakePhoto = view.findViewById(R.id.layoutTakePhoto);
            layoutTakePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.takePhoto();
                }
            });

            layoutChoosePhoto = view.findViewById(R.id.layoutChoosePhoto);
            layoutChoosePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.choosePhoto();
                }
            });

            layoutDrawAble = view.findViewById(R.id.layoutDrawAble);
            layoutDrawAble.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.drawing();
                }
            });
        }
    }
}
