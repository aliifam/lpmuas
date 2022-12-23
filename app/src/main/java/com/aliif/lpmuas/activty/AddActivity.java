package com.aliif.lpmuas.activty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aliif.lpmuas.R;
import com.aliif.lpmuas.model.Report;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddActivity extends AppCompatActivity {

    EditText title, content, date, location;
    Button button;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        date = findViewById(R.id.date);
        location = findViewById(R.id.location);

        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_title = title.getText().toString();
                String str_content = content.getText().toString();
                String str_date = date.getText().toString();
                String str_location = location.getText().toString();

                if(str_title.trim().isEmpty()){
                    title.setError("Judul tidak boleh kosong");
                }else if(str_content.trim().isEmpty()){
                    content.setError("Isi tidak boleh kosong");
                }else if(str_date.trim().isEmpty()){
                    date.setError("Tanggal tidak boleh kosong");
                }else if(str_location.trim().isEmpty()){
                    location.setError("Lokasi tidak boleh kosong");
                }else{
                    databaseReference.child("Reports").push().setValue(new Report("user_id", str_title, str_content, str_date, str_location)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AddActivity.this, "Pengaduan Berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddActivity.this, "Pengaduan Gagal ditambahkan", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}