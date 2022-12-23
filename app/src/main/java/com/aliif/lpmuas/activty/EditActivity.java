package com.aliif.lpmuas.activty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.aliif.lpmuas.R;

public class EditActivity extends AppCompatActivity {

    EditText title, content, date, location;
    Button edit;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        date = findViewById(R.id.date);
        location = findViewById(R.id.location);

        Intent intent  = getIntent();

        id = intent.getStringExtra("id");

        title.setText(intent.getStringExtra("title"));
        content.setText(intent.getStringExtra("content"));
        date.setText(intent.getStringExtra("date"));
        location.setText(intent.getStringExtra("location"));

    }
}