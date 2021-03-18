package com.example.medocare.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.medocare.MyReminderData;
import com.example.medocare.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


public class NotesFragment extends Fragment {

    FloatingActionButton add_me;
    DatabaseReference reff;
    FirebaseDatabase database;
    ListView list;
    ArrayAdapter<String> arrayAdapter;
    MyReminderData reminder;
    ArrayList<String> reminderlist;
    private String userid;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notes, container, false);

        database = FirebaseDatabase.getInstance();
        reff = database.getReference("MyNotes");
        list = (ListView)root.findViewById(R.id.list_notes);
        reminderlist = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, reminderlist);
        list.setAdapter(arrayAdapter);

        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    //GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                    Map<String, String> map = (Map<String, String>) d.getValue();
                    if (userid.equals(map.get("userid"))) {
                        String note=map.get("notes");
                        reminderlist.add("Your note is:-"+note);
                        arrayAdapter.notifyDataSetChanged();

                    }
                }
            }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        add_me=(FloatingActionButton)root.findViewById(R.id.note_add);
        add_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),AddNote.class);
                startActivity(i);
            }
        });



    return root;
    }
}