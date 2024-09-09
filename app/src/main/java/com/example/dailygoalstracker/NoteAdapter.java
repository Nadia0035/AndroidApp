package com.example.dailygoalstracker;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import android.app.AlertDialog;

public class NoteAdapter extends FirestoreRecyclerAdapter<Notess, NoteAdapter.NoteViewHolder> {
    Context context;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Notess> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Notess note) {
        holder.titleTextView.setText(note.getTextfile());
        holder.timestampTextView.setText(Utility.timestampToString(note.getTimestamp()));
        holder.checkBox.setChecked(note.getIsDone() == 1);

        holder.checkBox.setOnCheckedChangeListener(null);

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
            snapshot.getReference().update("isDone", isChecked ? 1 : 0);
        });

        holder.imageView.setOnClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Task")
                    .setMessage("Are you sure you want to delete this task?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        getSnapshots().getSnapshot(holder.getAdapterPosition()).getReference().delete();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_task, parent, false);
        return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, timestampTextView;
        CheckBox checkBox;
        ImageView imageView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            timestampTextView = itemView.findViewById(R.id.timestamp_text_view);
            checkBox = itemView.findViewById(R.id.simpleCheckBox);
            imageView = itemView.findViewById(R.id.delete_id);
        }
    }
}
