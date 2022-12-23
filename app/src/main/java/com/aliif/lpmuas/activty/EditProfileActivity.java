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
import com.aliif.lpmuas.util.Auth;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    EditText name, email, password, confirmpassword;
    Button button;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpassword);

        button = findViewById(R.id.button);

        databaseReference.child("Users").orderByKey().equalTo(Auth.getUserId(getApplicationContext()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                User user = dataSnapshot.getValue(User.class);
                                assert user != null;
                                name.setText(user.getName());
                                email.setText(user.getEmail());
                                password.setText(user.getPassword());
                                confirmpassword.setText(user.getPassword());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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
                    databaseReference.child("Users").child(Auth.getUserId(getApplicationContext()))
                            .setValue(new User(str_email, str_name, str_pass)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(EditProfileActivity.this, "Data Profile Berhasil Diuabh", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditProfileActivity.this, "Data Profile gagal diubah", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}