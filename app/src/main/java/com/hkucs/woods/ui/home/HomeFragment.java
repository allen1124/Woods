package com.hkucs.woods.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.hkucs.woods.Post;
import com.hkucs.woods.ui.PostsAdapter;
import com.hkucs.woods.R;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private PostsAdapter postsAdapter;
    private int mPosts = 10;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recycleview_posts);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        postsAdapter = new PostsAdapter(getActivity());
        homeViewModel.loadPosts(mPosts);
        homeViewModel.getPosts().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(@Nullable List<Post> postList) {
                postsAdapter.setPostList(postList);
                recyclerView.setAdapter(postsAdapter);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int id = layoutManager.findLastCompletelyVisibleItemPosition();
                if(id >= postsAdapter.getItemCount()-1){
                    homeViewModel.addNewPost(postsAdapter.getLastItemDate());
                }
            }
        });
        return root;
    }
}