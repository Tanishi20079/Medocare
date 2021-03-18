package com.example.medocare.ui;

public class MyNote {
    public String notes;
    public String userid;

    public MyNote(String notes, String userid) {
        this.notes = notes;
        this.userid = userid;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
