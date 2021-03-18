package com.example.medocare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medocare.SetReminder.AddReminder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


public class SetReminders extends AppCompatActivity {
    FloatingActionButton add_me;
    DatabaseReference reff;
    FirebaseDatabase database;
    ListView list;
    ArrayAdapter<String> arrayAdapter;
    MyReminderData reminder;
    ArrayList<String> reminderlist;
    private TextView medi,time_day,quant_type;
    String m,t,q;
    private String userid;


    private static final String Tag = SetReminders.class.getSimpleName();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reminders);

        reminder = new MyReminderData();
        database = FirebaseDatabase.getInstance();
        reff = database.getReference("MyReminder");
        list = (ListView) findViewById(R.id.list_alarms);
        reminderlist = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, reminderlist);
        list.setAdapter(arrayAdapter);

        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    //GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                    Map<String, String> map = (Map<String, String>) d.getValue();
                    if (userid.equals(map.get("userid"))) {
                        m = map.get("medicine");
                        t = map.get("time");
                        q = map.get("quantity");
                        //medi=(TextView)findViewById(R.id.recycle_title);
                        //time_day=(TextView)findViewById(R.id.recycle_date_time);
                        //quant_type=(TextView)findViewById(R.id.recycle_repeat_info);
                        reminderlist.add("Medicine Name:" + m + "\n Time:" + t + "\n Quantity:" + q);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                Log.i(Tag, "Position:" + position + "ID:" + id);
                Intent i = new Intent(view.getContext(), AddReminder.class);
                startActivityForResult(i, 1);
            }
        });


        add_me = (FloatingActionButton) findViewById(R.id.add_me);
        add_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), AddReminder.class);
                startActivity(i);
            }
        });

    }
}
