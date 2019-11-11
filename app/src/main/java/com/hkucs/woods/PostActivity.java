package com.hkucs.woods;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

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
import android.widget.RadioButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

public class PostActivity extends AppCompatActivity{

   public static final String POSTS = "posts";

   private String mUsername;
   private DatabaseReference mFirebaseDatabaseReference;
   private FirebaseAuth mFirebaseAuth;
   private FirebaseUser mFirebaseUser;
   private Button mPostButton;

   private Boolean moods;
   private RadioButton happy;
   private RadioButton unhappy;
   private EditText eventText;
   private EditText thoughtText;
   private EditText actionText;
   private String event;
   private String thought;
   private String action;
   private int days;
   private EditText daysText;



   protected void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);

       mUsername = mFirebaseUser.getDisplayName();
       mFirebaseAuth = FirebaseAuth.getInstance();
       mFirebaseUser = mFirebaseAuth.getCurrentUser();
       mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

       happy = (RadioButton) findViewById(R.id.radioButton);
       unhappy = (RadioButton) findViewById((R.id.radioButton2));

       if(happy.isChecked()){
           moods = true;
       }else if(unhappy.isChecked()){
           moods = false;
       }

       eventText = (EditText) findViewById(R.id.editText_happened);
       event = eventText.getText().toString();
       thoughtText = (EditText) findViewById(R.id.editText_thought);
       thought = thoughtText.getText().toString();
       actionText = (EditText) findViewById(R.id.editText_action);
       action = actionText.getText().toString();
       daysText = (EditText) findViewById((R.id.editText_day));
       days =  Integer.parseInt(daysText.getText().toString());



       mPostButton = (Button) findViewById(R.id.button_submit);
       mPostButton.setOnClickListener(new View.OnClickListener() {

           public void onClick(View view){

               String key = mFirebaseDatabaseReference.child(POSTS).push().getKey();
               Post post = new Post(key,mFirebaseUser.getUid(),mUsername,moods,event,thought,action,getRemindDate(days));

               Map<String, Object> values = post.toMap();
               Map<String, Object> childUpdates = new HashMap<>();
               childUpdates.put("/posts/"+ key,values);

               mFirebaseDatabaseReference.updateChildren(childUpdates);
               eventText.setText("");
               thoughtText.setText("");
               actionText.setText("");
               happy.setChecked(false);
               unhappy.setChecked(false);
           }
       });





   }

    public Date getRemindDate(int days){
        Date current = Calendar.getInstance().getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(current);
        cal.add(Calendar.DATE,days);
        Date futureDate = cal.getTime();
        return futureDate;
    }


}