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
import com.aliif.lpmuas.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText name, email, password, confirmpassword;
    Button button, gotoact;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpassword);

        button = findViewById(R.id.button);

        gotoact = findViewById(R.id.gotoact);
        gotoact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str_name = name.getText().toString();
                String str_email = email.getText().toString();
                String str_pass = password.getText().toString();
                String str_conf = confirmpassword.getText().toString();

                if (str_name.trim().isEmpty()){
                    name.setError("Nama Tidak boleh kosong");
                }else if (str_email.trim().isEmpty()){
                    email.setError("Email Tidak boleh kosong");
                }else if (str_pass.isEmpty()){
                    password.setError("Password Tidak boleh kosong");
                }else if (!str_pass.equals(str_conf)){
                    password.setError("Password harus sesuai konfirmasi password");
                    confirmpassword.setError("Konfirmasi Password harus sesuai password");
                }else {
                    databaseReference.child("Users").push().
                            setValue(new User(str_email, str_name, str_pass)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(RegisterActivity.this, "Registrasi Berhasil silahkan login", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "Gagal melakukan registrasi", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}