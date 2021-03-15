package com.example.medocare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText title;
    Button setRem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.remTitle);
        setRem = findViewById(R.id.setEvent);
        setRem.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("QueryPermissionsNeeded")
            @Override
            public void onClick(View v) {
                if(!title.getText().toString().isEmpty()){

                    Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT).setData(CalendarContract.Reminders.CONTENT_URI);
                    //intent.setData(CalendarContract.Reminders.CONTENT_URI);
                    intent.putExtra(CalendarContract.Reminders.TITLE,title.getText().toString());
                    //startActivity(intent);
                    /*if(intent.resolveActivity(getPackageManager())!=null){
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "No app to handle", Toast.LENGTH_SHORT).show();
                    }*/
                   // String title = getResources().getString(R.string.chooser_title);
// Create intent to show chooser
                    Intent chooser = Intent.createChooser(intent, "choose");

                    startActivity(chooser);
// Try to invoke the intent.


                }

            }
        });


    }
}