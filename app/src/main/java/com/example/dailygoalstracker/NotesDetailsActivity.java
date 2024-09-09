package com.example.dailygoalstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NotesDetailsActivity extends AppCompatActivity {
    EditText edittext;
    ImageButton save_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_details);

        edittext = findViewById(R.id.edittask);
        save_btn = findViewById(R.id.save_btn);

        save_btn.setOnClickListener(v -> saveNote());
    }

    void saveNote() {
        String writetext = edittext.getText().toString();
        if (writetext == null || writetext.isEmpty()) {
            edittext.setError("Text is required");
            return;
        }

        Notess note = new Notess();
        note.setText(writetext);
        note.setTimestamp(Timestamp.now());
        // is_done is already set to 0 by default in the Notess constructor

        saveNoteToFirebase(note);
    }

    void saveNoteToFirebase(Notess note) {
        DocumentReference documentReference = Utility.getCollectionReferenceForNotes().document();

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Utility.showToast(NotesDetailsActivity.this, "Note added successfully");
                    startActivity(new Intent(NotesDetailsActivity.this, MainActivity.class));
                    finish();
                } else {
                    Utility.showToast(NotesDetailsActivity.this, "Failed while adding note");
                }
            }
        });
    }
}
