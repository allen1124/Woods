package com.hkucs.woods;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class RegistrationActivity extends AppCompatActivity {

    private Button avatar;
    private Button register;
    private EditText username;
    private Uri avatarUri;
    private String avatarImageUrl = "";
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        register = (Button) findViewById(R.id.button_register);
        avatar = (Button) findViewById(R.id.button_avator);
        username = (EditText) findViewById(R.id.editText_username);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        register.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                if(username.getText().toString().matches("")){
                    Toast.makeText(RegistrationActivity.this, "Please input your username",
                            Toast.LENGTH_SHORT).show();
                }else if(avatarUri == null) {
                    Toast.makeText(RegistrationActivity.this, "Please upload your avatar",
                            Toast.LENGTH_SHORT).show();
                }else{
                    performRegister();
                }
            }
        });
        avatar.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                Log.d("REGISTRATION", "select avatar clicked");
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            Log.d("REGISTRATION", "Photo selected");
            avatarUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), avatarUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            avatar.setBackgroundDrawable(new BitmapDrawable(bitmap));
        }
    }

    private void performRegister() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("REGISTRATION", "signInAnonymously:success");
                            progressBar.setVisibility(View.VISIBLE);
                            FirebaseUser user = mAuth.getCurrentUser();
                            uploadImageToFirebase();
                            Toast.makeText(RegistrationActivity.this, "Account Created",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("REGISTRATION", "signInAnonymously:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void uploadImageToFirebase(){
        if(avatarUri == null)
            return;
        String filename = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/"+filename);
        ref.putFile(avatarUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("REGISTRATION", "avatar photo uploaded");
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        avatarImageUrl = uri.toString();
                        saveUser();
                        Log.d("REGISTRATION", "avatar url: "+avatarImageUrl);
                    }
                });
            }
        });
    }

    private void saveUser(){
        String uid = mAuth.getCurrentUser().getUid();
        Log.d("REGISTRATION", "save user uid: "+uid);
        Log.d("REGISTRATION", "save user username: "+username.getText().toString());
        Log.d("REGISTRATION", "save user avatarImageUrl: "+avatarImageUrl);
        User user = new User(uid, username.getText().toString(), avatarImageUrl);
        mDatabase.child("users").child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Log.d("REGISTRATION", "user saved");
                    Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Log.d("REGISTRATION", task.getException().getMessage());
                }
            }
        });
    }
}
