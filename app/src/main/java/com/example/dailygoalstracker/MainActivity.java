package com.example.dailygoalstracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addbtn;
    RecyclerView recyclerView;
    ImageButton menubtn;
    NoteAdapter noteAdapter;
    Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addbtn = findViewById(R.id.add_note);
        recyclerView = findViewById(R.id.recycleview);
        menubtn = findViewById(R.id.menu_btn);
        doneButton = findViewById(R.id.supabutton);

        addbtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, NotesDetailsActivity.class)));
        menubtn.setOnClickListener(v -> showMenu());
        doneButton.setOnClickListener(v -> calculateIsDonePercentage());

        setupRecyclerView();
    }

    void showMenu() {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, menubtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getTitle().equals("Logout")) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, LoginPageActivity.class));
                    finish();
                }
                return false;
            }
        });
    }

    void setupRecyclerView() {
        Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Notess> options = new FirestoreRecyclerOptions.Builder<Notess>().setQuery(query, Notess.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(options, this);
        recyclerView.setAdapter(noteAdapter);
    }

    private void calculateIsDonePercentage() {
        int totalItems = noteAdapter.getItemCount();
        int isDoneCount = 0;
        int notDoneCount = 0;

        for (int i = 0; i < totalItems; i++) {
            DocumentSnapshot snapshot = noteAdapter.getSnapshots().getSnapshot(i);
            Notess note = snapshot.toObject(Notess.class);
            if (note != null) {
                if (note.getIsDone() == 1) {
                    isDoneCount++;
                } else {
                    notDoneCount++;
                }
            }
        }

        double isDonePercentage = (double) isDoneCount / totalItems * 100;
        double notDonePercentage = (double) notDoneCount / totalItems * 100;


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_popup_message, null);
        TextView messageTextView = dialogView.findViewById(R.id.message_text_view);
//        messageTextView.setText(String.format("Percentage of completed tasks: %.2f%%\nPercentage of incomplete tasks: %.2f%%", isDonePercentage, notDonePercentage));
//        messageTextView.setText(String.format("Task Completion Summary:\n\nCompleted Tasks: %.2f%%\nIncomplete Tasks: %.2f%%", isDonePercentage, notDonePercentage));
        if (isDonePercentage == 100.0) {
            messageTextView.setText("Congratulations!\n\nAll tasks have been completed.");
        } else {
            messageTextView.setText(String.format("Task Completion Summary:\n\nCompleted Tasks: %.2f%%\nIncomplete Tasks: %.2f%%", isDonePercentage, notDonePercentage));
        }

        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }
}
