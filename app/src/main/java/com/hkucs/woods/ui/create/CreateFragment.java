package com.hkucs.woods.ui.create;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hkucs.woods.Post;
import com.hkucs.woods.R;
import com.hkucs.woods.User;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateFragment extends Fragment {

    private CreateViewModel createViewModel;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private RadioGroup emotion;
    private RadioButton negative, positive;
    private EditText event, thought, action, remind_day;
    private Button submit;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        createViewModel =
                ViewModelProviders.of(this).get(CreateViewModel.class);
        View root = inflater.inflate(R.layout.fragment_create, container, false);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        emotion = root.findViewById(R.id.radioGroup_emotion);
        negative = root.findViewById(R.id.radioButton_negative);
        positive = root.findViewById(R.id.radioButton_positive);
        event = root.findViewById(R.id.editText_event);
        thought = root.findViewById(R.id.editText_thought);
        action = root.findViewById(R.id.editText_action);
        remind_day = root.findViewById(R.id.editText_remind_day);
        submit = root.findViewById(R.id.button_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
        return root;
    }

    private void submitPost(){
        final String pEvent = event.getText().toString();
        final String pThought = thought.getText().toString();
        final String pAction = action.getText().toString();
        final int pRemind_day = Integer.parseInt(remind_day.getText().toString());
        final int checkedRadioButtonId = emotion.getCheckedRadioButtonId();
        if(pEvent.matches("") || pAction.matches("") || pThought.matches("") || checkedRadioButtonId == -1){
            Toast.makeText(getActivity(), "You did not complete the post.", Toast.LENGTH_SHORT).show();
            return;
        }
        setEditingEnabled(false);
        Toast.makeText(getActivity(), "Posting...", Toast.LENGTH_SHORT).show();
        final String userId = mAuth.getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user == null) {
                            Log.d("CREATEFRAGMENT", "User " + userId + " is unexpectedly null");
                            Toast.makeText(getActivity(),
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            writeNewPost(userId, user.username, checkedRadioButtonId, pEvent, pThought, pAction, pRemind_day);
                        }
                        setEditingEnabled(true);
                        finishPosting();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("CREATEFRAGMENT", "getUser:onCancelled", databaseError.toException());
                        setEditingEnabled(true);
                    }
                });
    }

    private void setEditingEnabled(boolean enabled) {
        emotion.setEnabled(enabled);
        event.setEnabled(enabled);
        thought.setEnabled(enabled);
        action.setEnabled(enabled);
        submit.setEnabled(enabled);
    }

    private void writeNewPost(String uid, String username, int emotionId, String event, String thought, String action, int remindDay){
        String key = mDatabase.child("posts").push().getKey();
        boolean moods = emotionId == positive.getId()? true : false;
        Date currentDate = Calendar.getInstance().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, remindDay);
        Date remindDate = new Date(c.getTimeInMillis());
        Post post = new Post(key, uid, username, moods, event, thought, action, remindDate);

        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + uid + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

    private void finishPosting(){
        emotion.clearCheck();
        event.setText(null);
        thought.setText(null);
        action.setText(null);
        remind_day.setText(null);
    }
}