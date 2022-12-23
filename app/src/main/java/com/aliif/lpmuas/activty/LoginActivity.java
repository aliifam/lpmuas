package com.aliif.lpmuas.activty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aliif.lpmuas.R;
import com.aliif.lpmuas.model.User;
import com.aliif.lpmuas.util.Auth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button button, gotoact;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        button = findViewById(R.id.button);

        gotoact = findViewById(R.id.gotoact);

        gotoact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email = email.getText().toString();
                String str_password = password.getText().toString();

                databaseReference.child("Users").orderByChild("email").equalTo(str_email)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        User user = dataSnapshot.getValue(User.class);
                                        assert user != null;
                                        user.setId(dataSnapshot.getKey());

                                        if (str_password.equals(user.getPassword())){
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.putExtra("user_id", user.getId());
                                            intent.putExtra("user_name", user.getName());
                                            intent.putExtra("user_email", user.getEmail());
                                            intent.putExtra("user_password", user.getPassword());
                                            Auth.setUserLoggedOut(getApplicationContext(), false);
                                            Auth.setUserId(getApplicationContext(), user.getId());
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            Toast.makeText(LoginActivity.this, "Password tidak sesuai gagal login", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }else {
                                    Toast.makeText(LoginActivity.this, "Account doesn't exist", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });
    }
}