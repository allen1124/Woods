package com.hkucs.woods;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

public class PostActivity extends AppCompatActivity{

    private DatabaseReference mDatabase;
    private RadioGroup emotion;
    private RadioButton negative, positive;
    private EditText event, thought, action, remind_day;
    private Button submit;
    private Button delete;
    private Intent intent;
    private String pid;
    private String username;
    private String avatarUrl;
    private String uid;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Edit Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_post);
        intent = getIntent();
        HashMap<String, Object> post = (HashMap) intent.getSerializableExtra("post");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        emotion = (RadioGroup) findViewById(R.id.radioGroup_emotion);
        positive = (RadioButton) findViewById(R.id.radioButton_positive_edit);
        negative = (RadioButton) findViewById((R.id.radioButton_negative));
        positive.setChecked((boolean)post.get("moods") == true);
        negative.setChecked((boolean)post.get("moods") == false);
        event = (EditText) findViewById(R.id.editText_event);
        event.setText((String)post.get("event"));
        thought = (EditText) findViewById(R.id.editText_thought);
        thought.setText((String)post.get("thought"));
        action = (EditText) findViewById(R.id.editText_action);
        action.setText((String)post.get("action"));
        remind_day = (EditText) findViewById((R.id.editText_remind_day));
        submit = (Button) findViewById(R.id.button_submit);
        delete = (Button) findViewById(R.id.button_delete);
        pid = (String) post.get("pid");
        uid = (String) post.get("uid");
        username = (String) post.get("username");
        avatarUrl = (String) post.get("avatar_url");
        submit.setOnClickListener(new View.OnClickListener() {
           public void onClick(View view){
               updatePost();
           }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePost();
            }
        });
    }

    private void deletePost(){
        boolean moods = emotion.getCheckedRadioButtonId() == positive.getId()? true : false;
        String mood = moods? "positive" : "negative";
        mDatabase.child("posts").child(mood).child(pid).setValue(null);
        mDatabase.child("user-posts").child(uid).child(pid).setValue(null);
        this.startActivity(new Intent(this, HomeActivity.class));
    }

    private void updatePost(){
        final String pEvent = event.getText().toString();
        final String pThought = thought.getText().toString();
        final String pAction = action.getText().toString();
        int pRemind_day = 0;
        if(!remind_day.getText().toString().matches("")){
            pRemind_day = Integer.parseInt(remind_day.getText().toString());
        }
        final int checkedRadioButtonId = emotion.getCheckedRadioButtonId();
        if(pEvent.matches("") || pAction.matches("") || pThought.matches("") || checkedRadioButtonId == -1){
            Toast.makeText(this, "You did not complete the post.", Toast.LENGTH_SHORT).show();
            return;
        }
        setEditingEnabled(false);
        Toast.makeText(this, "Updating...", Toast.LENGTH_SHORT).show();
        String key = pid;
        boolean moods = emotion.getCheckedRadioButtonId() == positive.getId()? true : false;
        String mood = moods? "positive" : "negative";
        Date currentDate = Calendar.getInstance().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, pRemind_day);
        String remindDate = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date(c.getTimeInMillis()));
        Post post = new Post(key, uid, username, avatarUrl, moods, pEvent, pThought, pAction, remindDate);
        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + mood + "/" + key, postValues);
        childUpdates.put("/user-posts/" + uid + "/" + key, postValues);
        mDatabase.updateChildren(childUpdates);
        this.startActivity(new Intent(this, HomeActivity.class));
    }

    private void setEditingEnabled(boolean enabled) {
        emotion.setEnabled(enabled);
        event.setEnabled(enabled);
        thought.setEnabled(enabled);
        action.setEnabled(enabled);
        submit.setEnabled(enabled);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
