package com.groupx.simplenote.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.groupx.simplenote.R;
import com.groupx.simplenote.dao.AccountDao;
import com.groupx.simplenote.database.NoteDatabase;
import com.groupx.simplenote.entity.Account;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText capcha, re_capcha, newPass, re_newPass;
    Button btnRest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        capcha = findViewById(R.id.etUserName);
        re_capcha = findViewById(R.id.etCapCha);
        newPass = findViewById(R.id.etRePassword);
        re_newPass = findViewById(R.id.edtReConfirmPassword);
        btnRest = findViewById(R.id.btnReSetPass);

        capcha.setText(getIntent().getExtras().getString("capcha"));

        btnRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!re_capcha.getText().toString().equals(capcha.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Wrong Capcha!!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newPass.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Password is require!!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPass.getText().toString().equals(re_newPass.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Wrong ReEnter Password!!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                NoteDatabase noteDatabase = NoteDatabase.getSNoteDatabase(getApplicationContext());
                final AccountDao accountDao = noteDatabase.accountDao();
                Account account = accountDao.getAccountByEmail(getIntent().getExtras().getString("email"));
                account.setPassword(newPass.getText().toString());
                accountDao.update(account);
                Toast.makeText(getApplicationContext(), "Reset Password Successfull", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}