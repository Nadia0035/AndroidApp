package com.example.dailygoalstracker;

import com.google.firebase.Timestamp;

public class Notess {
    private String textfile;
    private Timestamp timestamp;
    private int is_done;

    public Notess() {

        this.is_done = 0;
    }

    public Notess(String textfile, Timestamp timestamp) {
        this.textfile = textfile;
        this.timestamp = timestamp;
        this.is_done = 0;
    }

    public String getTextfile() {
        return textfile;
    }

    public void setTextfile(String textfile) {
        this.textfile = textfile;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getIsDone() {
        return is_done;
    }

    public void setIsDone(int is_done) {
        this.is_done = is_done;
    }

    public void setText(String textfile) {
        this.textfile = textfile;
    }

    public String getText() {
        return textfile;
    }
}
