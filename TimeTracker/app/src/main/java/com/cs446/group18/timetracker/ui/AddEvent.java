package com.cs446.group18.timetracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.cs446.group18.timetracker.R;

public class AddEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_add_event);
    }

    public void addEventBackend(View view) {
        Intent intent = getIntent();

        EditText eventNameEdit = (EditText)findViewById(R.id.eventName);
        EditText eventTextEdit = (EditText)findViewById(R.id.eventText);
        DatePicker dateText = findViewById(R.id.DatePicker);
        String name = eventNameEdit.getText().toString();
        String text = eventTextEdit.getText().toString();
        String month;
        String day;

        if(!name.isEmpty() && !text.isEmpty()) {
            int j = dateText.getMonth() + 1;
            if (j < 10) {
                month = "0" + j;
            } else {
                month = "" + j;
            }

            if (dateText.getDayOfMonth() < 10) {
                day = "0" + dateText.getDayOfMonth();
            } else {
                day = "" + dateText.getDayOfMonth();
            }

            String date = dateText.getYear() + "-" + month + "-" + day;

            // TODO: add event to db

        }

        finish();
    }
}
