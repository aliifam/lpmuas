package com.aliif.lpmuas.activty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aliif.lpmuas.R;
import com.aliif.lpmuas.adapter.ReportAdapter;
import com.aliif.lpmuas.model.Report;
import com.aliif.lpmuas.util.Auth;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    ReportAdapter reportAdapter;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    ArrayList<Report> reports;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Auth.isUserLoggedOut(getApplicationContext()))
        {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }else {
            floatingActionButton = findViewById(R.id.add_button);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), AddActivity.class));
                }
            });

            recyclerView = findViewById(R.id.list_report);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            viewReports();
        }
    }

        private void viewReports()
        {
            databaseReference.child("Reports").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    reports = new ArrayList<>();
                    for (DataSnapshot item: snapshot.getChildren())
                    {
                        Report report = item.getValue(Report.class);
                        assert report != null;
                        report.setId(item.getKey());
                        reports.add(report);
                    }

                    reportAdapter = new ReportAdapter(reports, MainActivity.this);
                    recyclerView.setAdapter(reportAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
}