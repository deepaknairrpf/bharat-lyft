package com.example.desilyft;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

public class LyfteeActivity extends AppCompatActivity {

    ImageButton btnDatePicker, btnTimePicker;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int currentYear, currentMonth, currentDate, currentHour, currentMin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyftee);

        final TextView txtDate=(TextView) findViewById(R.id.dateText);
        final TextView txtTime=(TextView) findViewById(R.id.timeText);

        Calendar c = Calendar.getInstance();
        currentYear = c.get(Calendar.YEAR);
        currentMonth = c.get(Calendar.MONTH);
        currentDate = c.get(Calendar.DATE);
        currentHour = c.get(Calendar.HOUR);
        currentMin = c.get(Calendar.MINUTE);
        txtDate.setText(currentDate + "-" + (currentMonth + 1) + "-" + currentYear);
        txtTime.setText(currentHour + ":" + currentMin);
        btnDatePicker=(ImageButton)findViewById(R.id.dateImgButton);
        btnTimePicker=(ImageButton)findViewById(R.id.timeImgButton);

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(LyfteeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar calendar = Calendar.getInstance();
                mHour = calendar.get(Calendar.HOUR_OF_DAY);
                mMinute = calendar.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(LyfteeActivity.this ,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                String hour_str = String.format("%02d", hourOfDay);
                                String minute_str = String.format("%02d", minute);
                                txtTime.setText(hour_str + ":" + minute_str);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
    }
}
