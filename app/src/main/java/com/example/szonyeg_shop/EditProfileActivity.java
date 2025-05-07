package com.example.szonyeg_shop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private EditText editTextEditName;
    private EditText editTextEditEmail;
    private EditText editTextEditOldPassword;
    private EditText editTextEditNewPassword;
    private EditText getEditTextEditNewPasswordVer;

    private Button cancel;
    private Button edit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        editTextEditName=findViewById(R.id.editTextEditName);
        editTextEditEmail=findViewById(R.id.editTextEditEmail);
        editTextEditOldPassword=findViewById(R.id.editTextEditOldPassword);
        editTextEditNewPassword=findViewById(R.id.editTextEditNewPassword);
        getEditTextEditNewPasswordVer=findViewById(R.id.editTextEditNewPasswordVer);

        cancel=findViewById(R.id.buttonCancelEdit);
        edit=findViewById(R.id.buttonSaveProfile);

        cancel.setOnClickListener(e->{
            finish();
        });

        edit.setOnClickListener(e->{
            edit();
        });


    }


    private void edit(){
        FirebaseUser user = auth.getCurrentUser();
        if(user!= null) {
            String oldemail=user.getEmail();
            String name = editTextEditName.getText().toString().trim();
            String newemail=editTextEditEmail.getText().toString().trim();
            String oldPass=editTextEditOldPassword.getText().toString().trim();
            String newPass=editTextEditNewPassword.getText().toString().trim();
            String newPassVer=getEditTextEditNewPasswordVer.getText().toString().trim();
            int hiba=0;
            if(newemail.isBlank()){
                hiba++;
                notifyAlert("Add meg az email cimet!");
            }
            if (name.isBlank()){
                hiba++;
                notifyAlert("Add meg a neved!");
            }
            if(oldPass.isBlank()){
                hiba++;
                notifyAlert("Add meg a régi jelszavad");
            }
            if (newPass.isBlank()){
                newPass=oldPass;
                newPassVer=oldPass;
            }
            if(!newPass.equals(newPassVer) && newPass.length()<6){
                hiba++;
                notifyAlert("A jelszó és ellenörző jelszó nem egyezik vagy nem elég erős");
            }


            if(hiba==0){
                String ne=newPass;
                AuthCredential credential= EmailAuthProvider.getCredential(oldemail,oldPass);

                user.reauthenticate(credential).addOnSuccessListener( e ->{
                    user.updateEmail(newemail).addOnSuccessListener(c->{
                        user.updatePassword(ne).addOnSuccessListener(g->{
                            updateUser(newemail,name,user.getUid());
                        }).addOnFailureListener(g->{
                            errorAlert(g.getMessage());
                        });
                    }).addOnFailureListener(c->{
                        errorAlert(c.getMessage());
                    });

                }).addOnFailureListener( e->{
                    errorAlert(e.getMessage());
                });

            }

        }
    }


    private void errorAlert(String massage){
        new AlertDialog.Builder(EditProfileActivity.this)
                .setTitle("Sikertelen modosítás.")
                .setMessage(massage)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void notifyAlert(String massage){
        new AlertDialog.Builder(EditProfileActivity.this)
                .setTitle("Hibás bemenet")
                .setMessage(massage)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void updateUser(String email,String name,String uId){
        HashMap<String, Object> user=new HashMap<>();
        user.put("email",email);
        user.put("name",name);

        db.collection("Users").document(uId).update(user).addOnSuccessListener(e->{
            finish();
        });
    }






}





