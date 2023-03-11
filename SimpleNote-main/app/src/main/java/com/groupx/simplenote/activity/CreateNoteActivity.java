package com.groupx.simplenote.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.groupx.simplenote.R;
import com.groupx.simplenote.common.Component;
import com.groupx.simplenote.common.Const;
import com.groupx.simplenote.common.Utils;
import com.groupx.simplenote.database.NoteDatabase;
import com.groupx.simplenote.entity.Account;
import com.groupx.simplenote.entity.Folder;
import com.groupx.simplenote.entity.Note;
import com.groupx.simplenote.entity.NoteAccount;
import com.groupx.simplenote.entity.NoteTag;
import com.groupx.simplenote.fragment.ChoosingNoteColorFragment;
import com.groupx.simplenote.fragment.NoteDetailOptionFragment;
import com.groupx.simplenote.fragment.ReminderChooseOptionRefer;
import com.groupx.simplenote.fragment.placeholder.NoteChooseOptionRefer;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreateNoteActivity extends AppCompatActivity {

    private ImageView imageNoteDetailBack, imageNoteDetailSave, imageNoteDetailColorOptionLens,
            imageNoteDetailOption, imageViewAddOption;
    private EditText editTextNoteSubtitle, editTextNoteTitle, editTextNoteContent;
    private TextView textViewNoteDetailDatetime;
    private ConstraintLayout layoutNoteDetail;
    private ImageView imgTakePhoto;


    private String selectedNoteColor;
    private Note alreadyNote = new Note();
    private Set<Integer> tagIdList = new HashSet<>();
    private List<NoteTag> oldNoteTagForUpdate = new ArrayList<>();
    private Set<Integer> accountId = new HashSet<>();
    public Account currentUser = new Account();
    private Folder folder;


    private int notePosition;

    private short mode;

    public Note getAlreadyNote() {
        return this.alreadyNote;
    }

    public Set<Integer> getTagIdSet() {
        return this.tagIdList;
    }

    private void findView() {
        imageNoteDetailBack = findViewById(R.id.imageNoteDetailBack);
        imageNoteDetailSave = findViewById(R.id.imageNoteDetailSave);
        imageNoteDetailColorOptionLens = findViewById(R.id.imageViewColorOptionLens);
        imageNoteDetailOption = findViewById(R.id.imageNoteDetailOption);
        imgTakePhoto = findViewById(R.id.imgTakePhotoInNote);
        imageViewAddOption = findViewById(R.id.imageViewAddOption);

        editTextNoteTitle = findViewById(R.id.editTextNoteTitle);
        editTextNoteSubtitle = findViewById(R.id.editTextNoteSubtitle);
        editTextNoteContent = findViewById(R.id.editTextNoteContent);

        textViewNoteDetailDatetime = findViewById(R.id.textViewNoteDetailDatetime);
        layoutNoteDetail = findViewById(R.id.layoutNoteDetail);
        selectedNoteColor = Utils.ColorIntToString(getColor(R.color.noteColorDefault));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        findView();
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.ACCOUNT_ID, 0);
        currentUser.setId(sharedPreferences.getInt("accountId", 0));

        mode = getIntent().getShortExtra("mode", Const.NoteDetailActivityMode.CREATE);
        folder = (Folder) getIntent().getSerializableExtra("folder");
        notePosition = getIntent().getIntExtra("position", 0);

        Date currentTimer = new Date();
        StringBuilder dateBuilder = new StringBuilder("Edited ");
        dateBuilder.append(Utils.DateTimeToString(currentTimer));
        textViewNoteDetailDatetime.setText(dateBuilder);
        imageNoteDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imageNoteDetailSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveOrUpdate();
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();

                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            bundle = null;
//            byte[] img = getIntent().getByteArrayExtra("signNature");
//            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
//            imgTakePhoto.setImageBitmap(bitmap);
//        }


        if (mode == Const.NoteDetailActivityMode.VIEW) {
            setOnlyView();
        }
        if (mode == Const.NoteDetailActivityMode.VIEW || mode == Const.NoteDetailActivityMode.EDIT) {
            alreadyNote = (Note) getIntent().getSerializableExtra("note");
            setViewAndEditNote();
        }
        initChooseOptionRefer();
        initChooseColorOption();
        initOption();
    }

    private void saveOrUpdate() {
        if (mode == Const.NoteDetailActivityMode.EDIT) {
            updateNote();
        } else if (mode == Const.NoteDetailActivityMode.CREATE) {
            saveNote();
        }
    }

    private void setOnlyView() {
        imageNoteDetailSave.setVisibility(View.GONE);
        imageNoteDetailColorOptionLens.setVisibility(View.GONE);
        imageNoteDetailOption.setVisibility(View.GONE);
        imageViewAddOption.setVisibility(View.GONE);

        Utils.disableEditText(editTextNoteTitle);
        Utils.disableEditText(editTextNoteSubtitle);
        Utils.disableEditText(editTextNoteContent);
    }

    private void initChooseOptionRefer() {
        NoteChooseOptionRefer option = new NoteChooseOptionRefer(this);
        imageViewAddOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option.show(getSupportFragmentManager(), "option");
            }
        });
    }

    private void initChooseColorOption() {
        ChoosingNoteColorFragment colorFragment = new ChoosingNoteColorFragment();
        Bundle args = new Bundle();
        colorFragment.setArguments(args);

        imageNoteDetailColorOptionLens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                args.putString("selectedColor", selectedNoteColor);
                colorFragment.show(getSupportFragmentManager(), "colorFragment");
            }
        });
    }

    private void initOption() {
        NoteDetailOptionFragment optionFragment = new NoteDetailOptionFragment(this);

        imageNoteDetailOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionFragment.show(getSupportFragmentManager(), "optionFragment");
            }
        });
    }

    private Note saveNote() {
        String title = editTextNoteTitle.getText().toString().trim();
        String subtitle = editTextNoteSubtitle.getText().toString().trim();
        String content = editTextNoteContent.getText().toString();
        byte[] image = null;
        if (imgTakePhoto.getDrawable() != null) {
            image = ImageView_To_Byte(imgTakePhoto);
        }

        final Note note = new Note();
        note.setTitle(title);
        note.setNote(content);
        note.setSubTitle(subtitle);
        note.setImage(image);
        note.setColor(selectedNoteColor);
        note.setSince(new Date());
        note.setLastUpdate(new Date());
        note.setStatusKey(Const.NoteStatus.NORMAL);
        if(folder != null){
            note.setFolderId(folder.getId());
        }

        NoteDatabase.getSNoteDatabase(getApplicationContext())
                .noteDao().insert(note);
        Note currentNote = NoteDatabase.getSNoteDatabase(getApplicationContext())
                .noteDao().getNewestNote();
        alreadyNote = currentNote;
        NoteAccount noteAccount = new NoteAccount();
        noteAccount.setNoteId(currentNote.getId());
        noteAccount.setAccountId(currentUser.getId());
        noteAccount.setPermission(Const.StatusPermission.CREATED.toString());
        NoteDatabase.getSNoteDatabase(getApplicationContext())
                .noteDao().insertWithNoteAccount(noteAccount);

        insertUpdateNoteTagId(currentNote);
        mode = Const.NoteDetailActivityMode.EDIT;
        return currentNote;
    }

    private void insertUpdateNoteTagId(Note note) {
        if (!oldNoteTagForUpdate.isEmpty()) {
            NoteDatabase.getSNoteDatabase(getApplicationContext())
                    .noteDao().deleteAllTag(oldNoteTagForUpdate);
        }
        List<NoteTag> noteTagList = new ArrayList<>();
        tagIdList.forEach(e -> {
            NoteTag notetag = new NoteTag();
            notetag.setTagId(e);
            notetag.setNoteId(note.getId());
            noteTagList.add(notetag);
        });
        NoteDatabase.getSNoteDatabase(getApplicationContext())
                .noteDao().insertNoteTag(noteTagList);
    }

    private void updateNote() {
        String title = editTextNoteTitle.getText().toString().trim();
        String subtitle = editTextNoteSubtitle.getText().toString().trim();
        String content = editTextNoteContent.getText().toString();
        byte[] image = null;
        if (imgTakePhoto.getDrawable() != null) {
            image = ImageView_To_Byte(imgTakePhoto);
        }


        if (alreadyNote == null) {
            alreadyNote = new Note();
            alreadyNote.setSince(new Date());
        }
        alreadyNote.setTitle(title);
        alreadyNote.setNote(content);
        alreadyNote.setSubTitle(subtitle);
        alreadyNote.setColor(selectedNoteColor);
        alreadyNote.setLastUpdate(new Date());
        alreadyNote.setImage(image);

        NoteDatabase.getSNoteDatabase(getApplicationContext())
                .noteDao().update(alreadyNote);

        insertUpdateNoteTagId(alreadyNote);
    }

    private void setBackGroundNoteColor(int color) {
        layoutNoteDetail.setBackgroundColor(color);
        // Convert color from int to string hex
        selectedNoteColor = Utils.ColorIntToString(color);
    }

    public void onClickColor(View view) {
        int color = new Component().getColorFromColorChooser(view, getApplicationContext());
        setBackGroundNoteColor(color);
    }

    private void setViewAndEditNote() {
        editTextNoteTitle.setText(alreadyNote.getTitle());
        editTextNoteSubtitle.setText(alreadyNote.getSubTitle());
        editTextNoteContent.setText(alreadyNote.getNote());
        if (alreadyNote.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(alreadyNote.getImage(), 0, alreadyNote.getImage().length);
            imgTakePhoto.setImageBitmap(bitmap);
        }
        selectedNoteColor = alreadyNote.getColor();
        if (selectedNoteColor == null) {
            selectedNoteColor = "#FFFFFF";
        }
        setBackGroundNoteColor(Color.parseColor(selectedNoteColor));

        oldNoteTagForUpdate = NoteDatabase.getSNoteDatabase(getApplicationContext())
                .noteDao().findNoteTagOf(alreadyNote.getId());
        oldNoteTagForUpdate.forEach(e -> {
            getTagIdSet().add(e.getTagId());
        });

        StringBuilder dateBuilder = new StringBuilder("Edited ");
        dateBuilder.append(Utils.DateTimeToString(alreadyNote.getLastUpdate()));
        textViewNoteDetailDatetime.setText(dateBuilder);
    }

    public void deleteNote() {
        if (alreadyNote != null && mode == Const.NoteDetailActivityMode.EDIT) {
            alreadyNote.setStatusKey(Const.NoteStatus.BIN);
            NoteDatabase.getSNoteDatabase(getApplicationContext())
                    .noteDao().update(alreadyNote);
        }
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public void shareNote(int accountId, String permisson) {
        if(accountId == currentUser.getId()){
            Toast.makeText(this, "Cannot share for yourself", Toast.LENGTH_SHORT).show();
            return;
        }
        saveOrUpdate();

        NoteAccount noteAccount = new NoteAccount();
        noteAccount.setAccountId(accountId);
        noteAccount.setPermission(permisson);
        if (alreadyNote != null) {
            noteAccount.setNoteId(alreadyNote.getId());
        }

        NoteDatabase.getSNoteDatabase(getApplicationContext())
                .noteDao().insertWithNoteAccount(noteAccount);
    }

    public void favouriteNote() {
        if (alreadyNote != null) {
            alreadyNote.setStatusKey(Const.NoteStatus.FAVORITE);
            NoteDatabase.getSNoteDatabase(getApplicationContext())
                    .noteDao().update(alreadyNote);
        }
        Toast.makeText(this, "Moved to favourite", Toast.LENGTH_LONG).show();
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public void archiveNote() {
        if (alreadyNote != null) {
            alreadyNote.setStatusKey(Const.NoteStatus.ARCHIVE);
            NoteDatabase.getSNoteDatabase(getApplicationContext())
                    .noteDao().update(alreadyNote);
        }
        Toast.makeText(this, "Archived", Toast.LENGTH_LONG).show();
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public void moveToBin() {
        if (alreadyNote != null) {
            alreadyNote.setStatusKey(Const.NoteStatus.BIN);
            NoteDatabase.getSNoteDatabase(getApplicationContext())
                    .noteDao().update(alreadyNote);
        }
        Toast.makeText(this, "Moved to bin", Toast.LENGTH_LONG).show();
        Intent intent = getIntent();
        getIntent().putExtra("myRequestCode", Const.NoteRequestCode.REQUEST_CODE_DELETE);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void requestCamPermision() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, 8888);
            }
        }
    }

    public void takePhoto() {
        requestCamPermision();
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 8888);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 8888 && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imgTakePhoto.setImageBitmap(photo);
        }

        if (requestCode == 2001 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgTakePhoto.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private byte[] ImageView_To_Byte(ImageView img) {
        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 2001);
    }

    public void drawing() {
        Intent intent = new Intent(CreateNoteActivity.this, Drawing.class);
        //startActivityForResult(intent, 0406);
        startActivity(intent);
    }
}