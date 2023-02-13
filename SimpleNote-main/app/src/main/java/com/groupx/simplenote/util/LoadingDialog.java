package com.groupx.simplenote.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import com.airbnb.lottie.animation.content.Content;
import com.groupx.simplenote.R;

public class LoadingDialog {
    Context context;
    Dialog dialog;

    public LoadingDialog(Context context) {
        this.context = context;
    }

    public void showDialog(String title) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvTitleDialog = dialog.findViewById(R.id.tv_title_dialog);
        if (title != null) {
            tvTitleDialog.setText(title);
        }
        dialog.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }

    public void hideDialog() {
        dialog.dismiss();
    }
}
