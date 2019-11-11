package com.hkucs.woods.ui.explore;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.hkucs.woods.Post;
import com.hkucs.woods.R;
import com.hkucs.woods.ui.PostsAdapter;

import java.util.List;

public class ExploreFragment extends Fragment {

    private ExploreViewModel exploreViewModel;
    private RecyclerView exploreRecyclerView;
    private TabLayout exploreTab;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        exploreViewModel =
                ViewModelProviders.of(this).get(ExploreViewModel.class);
                View root = inflater.inflate(R.layout.fragment_explore, container, false);
                exploreTab = root.findViewById(R.id.tabLayout_explore);
                exploreViewModel.loadHappyPosts();
                exploreTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        Log.d("EXPLORE", "tab selected"+tab.getPosition());
                        if(tab.getPosition() == 0)
                            exploreViewModel.loadHappyPosts();
                        if(tab.getPosition() == 1)
                            exploreViewModel.loadSadPosts();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        exploreRecyclerView = root.findViewById(R.id.recycleview_explore_posts);
        exploreRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        exploreViewModel.getPosts().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(@Nullable List<Post> postList) {
                exploreRecyclerView.setAdapter(new PostsAdapter(postList));
            }
        });

        return root;
    }

}