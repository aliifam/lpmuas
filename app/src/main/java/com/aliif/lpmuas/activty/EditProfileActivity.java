package com.aliif.lpmuas.activty;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
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

    String channelnotif = "mychannel";
    String channelid = "default";

    EditText name, email, password, confirmpassword;
    Button button;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Edit Profile");

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
                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(EditProfileActivity.this, "Data Profile Berhasil Diuabh", Toast.LENGTH_SHORT).show();
                                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelid)
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setContentTitle("Profile Updated")
                                            .setContentText("Berhasil mengubah data profile user!");
                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        int importance =  NotificationManager.IMPORTANCE_HIGH;
                                        NotificationChannel notificationChannel = new NotificationChannel(channelnotif, "example channel", importance);
                                        notificationChannel.enableLights(true);
                                        notificationChannel.setLightColor(Color.RED);
                                        mBuilder.setChannelId(channelnotif);
                                        notificationManager.createNotificationChannel(notificationChannel);
                                    }
                                    notificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());
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