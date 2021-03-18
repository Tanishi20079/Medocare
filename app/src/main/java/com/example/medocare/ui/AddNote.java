package com.example.medocare.ui;

import android.os.Bundle;
import com.example.medocare.R;
import com.example.medocare.SetReminders;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class AddNote extends AppCompatActivity {

    private EditText write;
    private DatabaseReference Database;
    private Button save;
    private MyNote note;
    private String userid;
    private static final String Tag = AddNote.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        write=findViewById(R.id.type_note);
        save=findViewById(R.id.submitnote);
        Database = FirebaseDatabase.getInstance().getReference().child("MyNotes");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mywrites=write.getText().toString();
                Log.i(Tag,"Writes"+mywrites);
                userid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                note.setNotes(mywrites);
                note.setUserid(userid);
                Database.push().setValue(note);
                Toast.makeText(AddNote.this,"Note Added",Toast.LENGTH_SHORT).show();


            }
        });




    }
}