package com.hkucs.woods.ui.explore;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hkucs.woods.Post;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExploreViewModel extends ViewModel {

    private MutableLiveData<List<Post>> posts;
    private List<Post> happyPostList;
    private List<Post> sadPostList;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String uid;

    public ExploreViewModel() {
        posts = new MutableLiveData<>();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        happyPostList = new ArrayList<>();
        sadPostList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public LiveData<List<Post>> getPosts() {
        return posts;
    }

    public void loadHappyPosts(int numberPost) {
        Log.d("EXPLORE", "load happy");
        final Boolean[] childExist = {false};
        Query ref = mDatabase.child("posts").child("positive").orderByChild("remindDate").limitToFirst(numberPost);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Post post = dataSnapshot.getValue(Post.class);
                if(!happyPostList.contains(post) || happyPostList.isEmpty()){
                    happyPostList.add(post);
                }
                posts.setValue(happyPostList);
                childExist[0] = true;
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(!childExist[0]){
            posts.setValue(happyPostList);
        }
    }

    public void loadSadPosts(int numberPost) {
        final Boolean[] childExist = {false};
        Log.d("EXPLORE", "load sad");
        Query ref = mDatabase.child("posts").child("negative").orderByChild("remindDate").limitToFirst(numberPost);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Post post = dataSnapshot.getValue(Post.class);
                if(!sadPostList.contains(post) || sadPostList.isEmpty()){
                    sadPostList.add(post);
                }
                posts.setValue(sadPostList);
                childExist[0] = true;
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(!childExist[0]){
            posts.setValue(sadPostList);
        }
    }

    public void addNewPost(String lastRemindDate, int selectedTabPosition){
        final String mood;
        if(selectedTabPosition == 0)
            mood = "positive";
        else
            mood = "negative";
        Query ref = mDatabase.child("posts").child(mood).orderByChild("remindDate").startAt(lastRemindDate).limitToFirst(10);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    if(mood == "positive"){
                        if (!happyPostList.contains(post)) {
                            happyPostList.add(post);
                            posts.setValue(happyPostList);
                        }
                    }else {
                        if (!sadPostList.contains(post)) {
                            sadPostList.add(post);
                            posts.setValue(sadPostList);
                        }
                    }
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}