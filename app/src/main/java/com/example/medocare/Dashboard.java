package com.example.medocare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.util.Calendar;

public class Dashboard extends AppCompatActivity {

    //EditText hr,min;
    TextView timerView;
    Button reminder;
    Button logout;
    TextView userMail;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    int timerhr,timermin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        createNotificationChannel();

        //hr = findViewById(R.id.editTextNumberHr);
        //min = findViewById(R.id.editTextNumberMin);
        userMail = findViewById(R.id.dashboardTextView);
        logout = findViewById(R.id.logoutButton);
        timerView = findViewById(R.id.timer);

        timerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        reminder = findViewById(R.id.alarmbtn);
        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Reminder set", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Dashboard.this, ReminderBroadcast.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(Dashboard.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                calendar.set(Calendar.HOUR_OF_DAY, 20);  //pass hour which you have select
                calendar.set(Calendar.MINUTE, 5);  //pass min which you have select
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                // Check we aren't setting it in the past which would trigger it to fire instantly
                if(calendar.getTimeInMillis() < System.currentTimeMillis()) {
                    calendar.add(Calendar.DAY_OF_YEAR, 7);
                }

                long startTime = calendar.getTimeInMillis();

                long timeAtClick = System.currentTimeMillis();
                long tenSec = 1000*10;
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                        startTime, AlarmManager.INTERVAL_DAY * 7,
                        pendingIntent);

            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userMail.setText(firebaseUser.getEmail());
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Dashboard.this,SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
    }
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "reminder";
            String description = "channel for reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("rem",name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}