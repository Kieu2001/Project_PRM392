package com.groupx.simplenote.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.groupx.simplenote.R;

public class NoteActivity extends AppCompatActivity{
    Button bottomsheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_1_note);
        bottomsheet = findViewById(R.id.editTextNoteContent);
        bottomsheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog();
            }
        });
    }
    private void showDialog() {

        final Dialog dialog = new Dialog( this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_1_bottom_note);

        LinearLayout cameraLayout = dialog.findViewById(R.id.layoutCamera);
        LinearLayout imageLayout = dialog.findViewById(R.id.layoutImagine);
        LinearLayout drawnLayout = dialog.findViewById(R.id.layoutdrawn);
        LinearLayout micLayout = dialog.findViewById(R.id.layoutMic);
        LinearLayout checkLayout = dialog.findViewById(R.id.layouCheck);

        cameraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NoteActivity.this,"Camera is Clicked",Toast.LENGTH_SHORT).show();
            }
        });
        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NoteActivity.this,"Image is Clicked",Toast.LENGTH_SHORT).show();
            }
        });
        drawnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NoteActivity.this,"drawn is Clicked",Toast.LENGTH_SHORT).show();
            }
        });
        micLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NoteActivity.this,"mic is Clicked",Toast.LENGTH_SHORT).show();
            }
        });
        checkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NoteActivity.this,"check box is Clicked",Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

}
