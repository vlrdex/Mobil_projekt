package com.example.szonyeg_shop;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private TextView textViewNameValue;
    private TextView textViewEmailValue;
    private Button buttonModifyProfile;
    private Button buttonDeleteProfile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);

        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        textViewEmailValue=findViewById(R.id.textViewProfileEmailValue);
        textViewNameValue=findViewById(R.id.textViewProfileNameValue);
        buttonModifyProfile=findViewById(R.id.buttonModifyProfile);
        buttonDeleteProfile=findViewById(R.id.buttonDeleteProfile);

        loadUserProfileData();

        buttonModifyProfile.setOnClickListener(e ->{
            Intent intent=new Intent(this,EditProfileActivity.class);
            startActivity(intent);
        });

        buttonDeleteProfile.setOnClickListener(e->{
            showDeleteConfirmationDialog();
        });
    }


    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Profil törlése")
                .setMessage("Biztosan törölni szeretnéd a profilodat? Ez a művelet nem visszavonható!")
                .setPositiveButton("Törlés", (dialog, which) -> {
                    FirebaseUser user =auth.getCurrentUser();
                    if(user !=null){
                        String uId= user.getUid();
                        user.delete();

                        db.collection("Users").document(uId).delete().addOnSuccessListener(e ->{
                            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        });
                    }

                })
                .setNegativeButton("Mégse", (dialog, which) -> {
                    dialog.dismiss(); // Bezárja a párbeszédablakot
                })
                .show();
    }

    private void loadUserProfileData() {
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            db.collection("Users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            String email = documentSnapshot.getString("email");


                            textViewNameValue.setText(name != null ? name : "Nincs megadva");
                            textViewEmailValue.setText(email != null ? email : "Nincs megadva");


                        } else {
                            textViewNameValue.setText("Ismeretlen felhasználó");
                            textViewEmailValue.setText("Ismeretlen email");
                        }
                    })
                    .addOnFailureListener(e -> {
                        textViewNameValue.setText("Hiba a betöltéskor");
                        textViewEmailValue.setText("Hiba a betöltéskor");
                    });
        } else {
            textViewNameValue.setText("Nincs bejelentkezve");
            textViewEmailValue.setText("Nincs bejelentkezve");
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        if(auth==null){
            auth=FirebaseAuth.getInstance();
        }
        if(db==null){
            db=FirebaseFirestore.getInstance();
        }
        if(textViewNameValue==null){
            textViewNameValue=findViewById(R.id.textViewProfileNameValue);
        }
        if(textViewEmailValue==null){
            textViewEmailValue=findViewById(R.id.textViewProfileEmailValue);
        }
        loadUserProfileData();
    }

}
