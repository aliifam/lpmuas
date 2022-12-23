package com.aliif.lpmuas.activty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aliif.lpmuas.R;
import com.aliif.lpmuas.util.Auth;

public class DetailActivity extends AppCompatActivity {

    TextView title, content, date, location;
    Button edit, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        date = findViewById(R.id.date);
        location = findViewById(R.id.location);
        edit = findViewById(R.id.edit);
        delete = findViewById(R.id.delete);

        title.setText(intent.getStringExtra("title"));
        content.setText(intent.getStringExtra("content"));
        date.setText(intent.getStringExtra("date"));
        location.setText(intent.getStringExtra("location"));

        if(!Auth.getUserId(getApplicationContext()).equals(intent.getStringExtra("user_id"))){
            edit.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        }
    }
}