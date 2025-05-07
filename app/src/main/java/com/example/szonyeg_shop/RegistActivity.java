package com.example.szonyeg_shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistActivity extends AppCompatActivity {
    EditText nameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText verPasswordEditText;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        verPasswordEditText = findViewById(R.id.password_again);
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
    }

    public void regist(View view){
        String name=nameEditText.getText().toString();
        String email=emailEditText.getText().toString();
        String password=passwordEditText.getText().toString();
        String verPassword=verPasswordEditText.getText().toString();

        if(isValidEmail(email)){
            if(password.equals(verPassword)){
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Map<String,Object> userdata= new HashMap<>();
                            userdata.put("email",email);
                            userdata.put("name",name);

                            db.collection("Users").document(Objects.requireNonNull(task.getResult().getUser()).getUid()).set(userdata)
                                    .addOnSuccessListener(aVoid -> {
                                        back_to_main();
                                    })
                                    .addOnFailureListener(e -> {
                                        new AlertDialog.Builder(RegistActivity.this)
                                                .setTitle("Sikertelen regisztráció.")
                                                .setMessage("Váratlan hiba történt probáld meg ujra később")
                                                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                                .show();
                                    }
                                    );

                        }else {
                            Exception exception = task.getException();
                            String errorMessage;

                            if (exception instanceof FirebaseAuthWeakPasswordException) {
                                errorMessage = "Gyenge jelszó.";
                            } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                                errorMessage = "Az email formátuma nem megfelelő";
                            } else if (exception instanceof FirebaseAuthUserCollisionException) {
                                errorMessage = "Az email már foglalt";
                            } else {
                                errorMessage = "Hiba történt a regsztráció során. Probálja meg később";
                            }

                            new AlertDialog.Builder(RegistActivity.this)
                                    .setTitle("Sikertelen regisztráció.")
                                    .setMessage(errorMessage)
                                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                    .show();
                        }
                    }
                });
            }else {
                new AlertDialog.Builder(RegistActivity.this)
                        .setTitle("Sikertelen regisztráció.")
                        .setMessage("A jelszó és ellenőrző jelszó nem egyezik")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        }else {
            new AlertDialog.Builder(RegistActivity.this)
                    .setTitle("Sikertelen regisztráció.")
                    .setMessage("Az email formátuma nem megfelelő")
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
        }

    }

    public void login(View view){
       back_to_main();
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void back_to_main(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
