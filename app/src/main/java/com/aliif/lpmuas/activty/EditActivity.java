package com.aliif.lpmuas.activty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.aliif.lpmuas.R;
import com.aliif.lpmuas.model.Report;
import com.aliif.lpmuas.util.Auth;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {

    EditText title, content, date, location;
    Button edit;

    String id;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                date.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.US);

        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        date = findViewById(R.id.date);
        location = findViewById(R.id.location);

        edit = findViewById(R.id.button);

        Intent intent  = getIntent();

        id = intent.getStringExtra("id");

        title.setText(intent.getStringExtra("title"));
        content.setText(intent.getStringExtra("content"));
        date.setText(intent.getStringExtra("date"));
        location.setText(intent.getStringExtra("location"));

        edit.setOnClickListener(new View.OnClickListener() {
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
                }else {
                    databaseReference.child("Reports").child(id)
                            .setValue(new Report(Auth.getUserId(getApplicationContext()), str_title, str_content, str_date, str_location))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(EditActivity.this, "Data Pengaduan Berhasil ditubah", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditActivity.this, "Data Pengaduan Gagal diubah", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

    }
}