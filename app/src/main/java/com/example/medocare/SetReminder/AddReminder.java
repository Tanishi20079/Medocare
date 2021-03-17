package com.example.medocare.SetReminder;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.medocare.MyReminderData;
import com.example.medocare.R;
import com.example.medocare.SetReminders;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddReminder extends AppCompatActivity {
    private EditText medicine;
    private CheckBox everyday, sun, mon, tues, wed, thurs, fri, sat;
    private TextView clock;
    private EditText quantity;
    private Spinner type;
    private DatabaseReference Database;
    private Button submit;
    private MyReminderData reminder;
    private int hour, minute;
    private List<String> doseUnitList;
    private String doseUnit;
    private String userid;
    int minutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addreminder);

        createNotificationChannel();

        medicine = findViewById(R.id.edit_med_name);
        everyday = findViewById(R.id.every_day);
        sun = findViewById(R.id.dv_sunday);
        mon = findViewById(R.id.dv_monday);
        tues = findViewById(R.id.dv_tuesday);
        wed = findViewById(R.id.dv_wednesday);
        thurs = findViewById(R.id.dv_thursday);
        fri = findViewById(R.id.dv_friday);
        sat = findViewById(R.id.dv_saturday);
        clock = findViewById(R.id.tv_medicine_time);
        quantity = findViewById(R.id.tv_dose_quantity);
        type =  findViewById(R.id.spinner_dose_units);
        Database = FirebaseDatabase.getInstance().getReference().child("MyReminder");
        submit = findViewById(R.id.reminder_submit);




        doseUnitList = Arrays.asList(getResources().getStringArray(R.array.type_name));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddReminder.this, android.R.layout.simple_spinner_item, doseUnitList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);


        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doseUnit = doseUnitList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AddReminder.this, "You haven't selected anything", Toast.LENGTH_SHORT).show();
            }
        });


        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCurrentTime = Calendar.getInstance();
                hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                minute = mCurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddReminder.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hour = selectedHour;
                        minute = selectedMinute;
                        clock.setText(String.format(Locale.getDefault(), "%d:%d", selectedHour, selectedMinute));
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String medi = medicine.getText().toString();
                String every = "0", m = "0", t = "0", w = "0", th = "0", su = "0", f = "0", sa = "0";
                String time = clock.getText().toString();
                String quant = quantity.getText().toString();


                if (!(medi.isEmpty() && time.isEmpty() && quant.isEmpty() && doseUnit.isEmpty())) {
                    if (everyday.isChecked() || mon.isChecked() || tues.isChecked() || thurs.isChecked() || wed.isChecked() || sat.isChecked() || fri.isChecked() || sun.isChecked()) {
                        if (everyday.isChecked()) {
                            m = "1";
                            t = "1";
                            w = "1";
                            th = "1";
                            f = "1";
                            sa = "1";
                            su = "1";
                        }
                        if (mon.isClickable())
                            m = "1";
                        if (sun.isChecked())
                            su = "1";
                        if (tues.isChecked())
                            t = "1";
                        if (wed.isChecked())
                            w = "1";
                        if (thurs.isChecked())
                            th = "1";
                        if (fri.isChecked())
                            f = "1";
                        if (sat.isChecked())
                            sa = "1";

                        reminder = new MyReminderData(); //medi,time, days,quant, doseUnit
                        reminder.setMedicine(medi);
                        reminder.setMon(m);
                        reminder.setFri(f);
                        reminder.setSat(sa);
                        reminder.setSun(su);
                        reminder.setTues(t);
                        reminder.setThurs(th);
                        reminder.setWed(w);
                        reminder.setTime(time);
                        reminder.setQuantity(quant);
                        reminder.setType(doseUnit);
                        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        reminder.setUserid(userid);
                        Database.push().setValue(reminder);
                        Toast.makeText(AddReminder.this, "Data:" + userid, Toast.LENGTH_LONG).show();


                        //set alarm
                        String[] arr = time.split(":");
                        Intent intent = new Intent(AddReminder.this, AlarmReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddReminder.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        if (m.equals("1")) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(arr[0]));
                            calendar.set(Calendar.MINUTE, Integer.parseInt(arr[1])-1);  //pass min which you have select
                            calendar.set(Calendar.SECOND, 59);
                            calendar.set(Calendar.MILLISECOND, 0);
                            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                                calendar.add(Calendar.DAY_OF_YEAR, 7);
                            }

                            long startTime = calendar.getTimeInMillis();
                            long timeAtClick = System.currentTimeMillis();
                            long tenSec = 1000 * 10;
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                    startTime, AlarmManager.INTERVAL_DAY * 7,
                                    pendingIntent);
                        }
                        if (t.equals("1")) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(arr[0]));
                            calendar.set(Calendar.MINUTE, Integer.parseInt(arr[1])-1);   //pass min which you have select
                            calendar.set(Calendar.SECOND,59);
                            calendar.set(Calendar.MILLISECOND, 0);
                            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                                calendar.add(Calendar.DAY_OF_YEAR, 7);
                            }

                            long startTime = calendar.getTimeInMillis();
                            long timeAtClick = System.currentTimeMillis();
                            long tenSec = 1000 * 10;
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                    startTime, AlarmManager.INTERVAL_DAY * 7,
                                    pendingIntent);
                        }
                        if (w.equals("1")) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(arr[0]));
                            calendar.set(Calendar.MINUTE, Integer.parseInt(arr[1])-1);   //pass min which you have select
                            calendar.set(Calendar.SECOND, 59);
                            calendar.set(Calendar.MILLISECOND, 0);
                            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                                calendar.add(Calendar.DAY_OF_YEAR, 7);
                            }

                            long startTime = calendar.getTimeInMillis();
                            long timeAtClick = System.currentTimeMillis();
                            long tenSec = 1000 * 10;
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                    startTime, AlarmManager.INTERVAL_DAY * 7,
                                    pendingIntent);

                        }
                        if (th.equals("1")) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(arr[0]));
                            calendar.set(Calendar.MINUTE, Integer.parseInt(arr[1])-1);   //pass min which you have select
                            calendar.set(Calendar.SECOND, 59);
                            calendar.set(Calendar.MILLISECOND, 0);
                            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                                calendar.add(Calendar.DAY_OF_YEAR, 7);
                            }

                            long startTime = calendar.getTimeInMillis();
                            long timeAtClick = System.currentTimeMillis();
                            long tenSec = 1000 * 10;
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                    startTime, AlarmManager.INTERVAL_DAY * 7,
                                    pendingIntent);

                        }
                        if (f.equals("1")) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(arr[0]));
                            calendar.set(Calendar.MINUTE, Integer.parseInt(arr[1])-1);   //pass min which you have select
                            calendar.set(Calendar.SECOND, 59);
                            calendar.set(Calendar.MILLISECOND, 0);
                            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                                calendar.add(Calendar.DAY_OF_YEAR, 7);
                            }

                            long startTime = calendar.getTimeInMillis();
                            long timeAtClick = System.currentTimeMillis();
                            long tenSec = 1000 * 10;
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                    startTime, AlarmManager.INTERVAL_DAY * 7,
                                    pendingIntent);

                        }
                        if (sa.equals("1")) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(arr[0]));
                            calendar.set(Calendar.MINUTE, Integer.parseInt(arr[1])-1);  //pass min which you have select
                            calendar.set(Calendar.SECOND, 59);
                            calendar.set(Calendar.MILLISECOND, 0);
                            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                                calendar.add(Calendar.DAY_OF_YEAR, 7);
                            }

                            long startTime = calendar.getTimeInMillis();
                            long timeAtClick = System.currentTimeMillis();
                            long tenSec = 1000 * 10;
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                    startTime, AlarmManager.INTERVAL_DAY * 7,
                                    pendingIntent);

                        }
                        if (su.equals("1")) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(arr[0]));
                            calendar.set(Calendar.MINUTE, Integer.parseInt(arr[1])-1);  //pass min which you have select
                            calendar.set(Calendar.SECOND, 59);
                            calendar.set(Calendar.MILLISECOND, 0);
                            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                                calendar.add(Calendar.DAY_OF_YEAR, 7);
                            }

                            long startTime = calendar.getTimeInMillis();
                            long timeAtClick = System.currentTimeMillis();
                            long tenSec = 1000 * 10;
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                    startTime, AlarmManager.INTERVAL_DAY * 7,
                                    pendingIntent);

                        }
                    }
                }
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "reminder";
            String description = "channel for reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            Toast.makeText(AddReminder.this, "notification", Toast.LENGTH_SHORT).show();
            NotificationChannel channel = new NotificationChannel("rem", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}