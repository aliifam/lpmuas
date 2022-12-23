package com.aliif.lpmuas.activty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aliif.lpmuas.R;
import com.aliif.lpmuas.model.User;
import com.aliif.lpmuas.util.Auth;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailActivity extends AppCompatActivity {

    TextView title, content, date, location, name, email;
    Button edit, delete;

    String id;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Detail Laporan");

        Intent intent = getIntent();
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        date = findViewById(R.id.date);
        location = findViewById(R.id.location);
        edit = findViewById(R.id.edit);
        delete = findViewById(R.id.delete);

        delete.setBackgroundColor(Color.RED);
        delete.setTextColor(Color.WHITE);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);

        title.setText(intent.getStringExtra("title"));
        content.setText(intent.getStringExtra("content"));
        date.setText("Tanggal : " + intent.getStringExtra("date"));
        location.setText("Lokasi : " + intent.getStringExtra("location"));

        id = intent.getStringExtra("id");

        if(!Auth.getUserId(getApplicationContext()).equals(intent.getStringExtra("user_id"))){
            edit.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        }

        databaseReference.child("Users").orderByKey().equalTo(intent.getStringExtra("user_id"))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        User user = dataSnapshot.getValue(User.class);
                                        assert user != null;
                                        name.setText("Nama Pengirim : " + user.getName());
                                        email.setText("Email Pengirim : " + user.getEmail());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDelete();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_edit = new Intent(getApplicationContext(), EditActivity.class);
                intent_edit.putExtra("id", id);
                intent_edit.putExtra("title", intent.getStringExtra("title"));
                intent_edit.putExtra("content", intent.getStringExtra("content"));
                intent_edit.putExtra("date", intent.getStringExtra("date"));
                intent_edit.putExtra("location", intent.getStringExtra("location"));
                intent_edit.putExtra("user_id", intent.getStringExtra("user_id"));
                startActivity(intent_edit);
            }
        });
    }

    public void confirmDelete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Report?");
        builder.setMessage("Are you sure want to Delete this report?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseReference.child("Reports").child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DetailActivity.this, "Report Successfully Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailActivity.this, "Delete Report Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}