package com.aliif.lpmuas.activty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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

    private void viewReports() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.logout){
            confirmLogout();
        }
        return super.onOptionsItemSelected(item);
    }

    public void confirmLogout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout From Account?");
        builder.setMessage("Are you sure want to Logout from your account?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Auth.setUserId(getApplicationContext(), "");
                Auth.setUserLoggedOut(getApplicationContext(), true);
                Toast.makeText(MainActivity.this, "Logout Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}