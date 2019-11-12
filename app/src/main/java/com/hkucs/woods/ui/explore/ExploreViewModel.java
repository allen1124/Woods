package com.hkucs.woods.ui.explore;

import com.hkucs.woods.Post;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExploreViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List<Post>> posts;

    public ExploreViewModel() {
        posts = new MutableLiveData<>();;
    }

    public LiveData<List<Post>> getPosts() {
        return posts;
    }

    public void loadHappyPosts() {
        List<Post> postList = new ArrayList<Post>();
        for(int i = 0; i < 100; i++){
            postList.add(new Post("1231231", "1231231", "HAPPY", true, "asdasidaosidjaosdjia","hhjhj","hjl", Calendar.getInstance().getTime()));
        }
        posts.postValue(postList);
    }

    public void loadSadPosts() {
        List<Post> postList = new ArrayList<Post>();
        for(int i = 0; i < 100; i++){
            postList.add(new Post("1231231", "1231231", "SAD", true, "asdasidaosidjaosdjia","hhjhj","hjl", Calendar.getInstance().getTime()));
        }
        posts.postValue(postList);
    }
}