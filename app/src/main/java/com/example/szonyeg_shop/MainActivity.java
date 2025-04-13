package com.example.szonyeg_shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);

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
}