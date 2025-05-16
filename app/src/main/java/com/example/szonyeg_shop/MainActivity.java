package com.example.szonyeg_shop;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.szonyeg_shop.Utils.Alarm;
import com.example.szonyeg_shop.Utils.NotiHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText emailEditText;
    EditText passwordEditText;

    private FirebaseAuth auth;

    private AlarmManager alarmManager;
    private NotiHelper notiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);

        notiHelper = new NotiHelper(this);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        setAlarmManager();

        auth=FirebaseAuth.getInstance();
    }

    public void login(View view){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    go();
                }else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Sikertelen bejelentkezés")
                            .setMessage("Hibás email vagy jelszó.")
                            .setPositiveButton("OK", (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .show();
                }
            }
        });
    }

    public void regist(View view){
        Intent intent= new Intent(this,RegistActivity.class);
        startActivity(intent);
    }

    public void go(){
        Intent intent = new Intent(this,ShopActivity.class);
        startActivity(intent);
    }

    private void setAlarmManager() {
        long repeatInterval = 1000000000;
        long triggerTime = SystemClock.elapsedRealtime() + repeatInterval;

        Intent intent = new Intent(this, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime,
                repeatInterval,
                pendingIntent);


        alarmManager.cancel(pendingIntent);
    }
}