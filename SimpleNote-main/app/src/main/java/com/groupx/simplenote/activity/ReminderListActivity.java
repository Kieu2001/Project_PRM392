package com.groupx.simplenote.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.groupx.simplenote.R;
import com.groupx.simplenote.adapter.NoteBinAdapter;
import com.groupx.simplenote.adapter.NoteFullAdapter;
import com.groupx.simplenote.common.Const;
import com.groupx.simplenote.database.NoteDatabase;
import com.groupx.simplenote.entity.Account;
import com.groupx.simplenote.entity.Note;
import com.groupx.simplenote.listener.NoteListener;

import java.util.ArrayList;
import java.util.List;

public class ReminderListActivity extends AppCompatActivity implements NoteListener {
    private final int REQUEST_CODE_UPDATE_NOTE = 2;

    private RecyclerView notesRecyclerView;
    private NoteFullAdapter noteAdapter;
    ImageView imageBack, imgAdd, imgSearch;

    private List<Note> noteList;

    private Account currentUser = new Account();

    private int noteClickedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_list);

        notesRecyclerView = findViewById(R.id.recyclerviewNote);
        //imageBack = findViewById(R.id.imageReminderBack);
        imgAdd = findViewById(R.id.imageReminderAdd);
        imgSearch = findViewById(R.id.imageSearch);

        notesRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        );

        noteList = new ArrayList<>();
        noteAdapter = new NoteFullAdapter(noteList, this);
        notesRecyclerView.setAdapter(noteAdapter);

        getNotes();

//        imageBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), CreateReminderActivity.class);
                startActivity(intent);
            }
        });

        //InitDrawerNavigationMenu();
    }

    @Override
    public void onNoteClicked(Note note, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), CreateReminderActivity.class);
        intent.putExtra("note", note);
        intent.putExtra("mode", Const.NoteDetailActivityMode.EDIT);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);
    }

    private void getNotes() {
        List<Note> notes = new ArrayList<>();
        notes = NoteDatabase.getSNoteDatabase(getApplicationContext())
                .noteDao().getAllReminders();
        if (noteList.size() == 0) {
            noteList.addAll(notes);
            noteAdapter.notifyDataSetChanged();
        } else {
            noteList.add(0, notes.get(0));
            noteAdapter.notifyItemInserted(0);
        }
        notesRecyclerView.smoothScrollToPosition(0);
    }

    private void InitDrawerNavigationMenu() {
        DrawerLayout drawerMainMenu = findViewById(R.id.drawerMainMenu);
        findViewById(R.id.imageReminderBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerMainMenu.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationViewMenu = findViewById(R.id.navViewMenu);
        TextView textAccountUserName = navigationViewMenu.getHeaderView(0).findViewById(R.id.textAccountUserName);
        textAccountUserName.setText(currentUser.getFullName());

        navigationViewMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent;
                switch (id) {
//                    case R.id.itemTestNoteDetail:
//                        intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
//                        startActivity(intent);
//                        return true;
                    case R.id.itemTestNoteListView:
                        //intent = new Intent(getApplicationContext(), NoteListActivity.class);
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.itemTestManageFolder:
                        intent = new Intent(getApplicationContext(), FolderActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.itemTestFeedback:
                        intent = new Intent(getApplicationContext(), FeedbackActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.itemTestSwm:
                        intent = new Intent(getApplicationContext(), ShareWithMeActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.itemSetting:
                        intent = new Intent(getApplicationContext(), SettingActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.itemRegister:
                        intent = new Intent(getApplicationContext(), RegisterActivity.class);
                        startActivity(intent);
                        return true;
//                    case R.id.itemSearchNote:
//                        intent = new Intent(getApplicationContext(), SearchActivity.class);
//                        startActivity(intent);
//                        return true;
                    case R.id.itemTag:
                        intent = new Intent(getApplicationContext(), TagActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.itemTestUpgradeScreen:
                        intent = new Intent(getApplicationContext(), PremiumUpgradeActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.itemTestArchive:
                        intent = new Intent(getApplicationContext(), ArchiveActivity.class);
                        startActivity(intent);
                        return true;
//                    case R.id.itemTestCalendar:
//                        intent = new Intent(getApplicationContext(), CalendarActivity.class);
//                        startActivity(intent);
//                        return true;
//                    case R.id.itemTestFavourite:
//                        intent = new Intent(getApplicationContext(), FavouriteActivity.class);
//                        startActivity(intent);
//                        return true;
                    case R.id.itemTestBin:
                        intent = new Intent(getApplicationContext(), BinActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.itemTestRemindersList:
                        intent = new Intent(getApplicationContext(), ReminderListActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.itemLogout:
                        intent = new Intent(getApplicationContext(), LogoutActivity.class);
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
}