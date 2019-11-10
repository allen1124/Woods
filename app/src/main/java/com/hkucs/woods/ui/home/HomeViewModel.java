package com.hkucs.woods.ui.home;

import com.hkucs.woods.Post;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Post>> posts;

    public HomeViewModel() {
        posts = new MutableLiveData<>();
        loadPosts();
    }


    public LiveData<List<Post>> getPosts() {
        return posts;
    }

    private void loadPosts() {
        // Do an asynchronous operation to fetch users.
        List<Post> postList = new ArrayList<Post>();
        for(int i = 0; i < 100; i++){
            postList.add(new Post("1231231", "1231231", "test", true, "asdasidaosidjaosdjia","hhjhj","hjl", Calendar.getInstance().getTime()));
        }
        posts.postValue(postList);
    }
}