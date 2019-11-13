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
    private LinearLayoutManager layoutManager;
    private PostsAdapter postsAdapter;
    private TabLayout exploreTab;
    private int mPosts = 10;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        exploreViewModel =
                ViewModelProviders.of(this).get(ExploreViewModel.class);
        View root = inflater.inflate(R.layout.fragment_explore, container, false);
        exploreTab = root.findViewById(R.id.tabLayout_explore);
        exploreRecyclerView = root.findViewById(R.id.recycleview_explore_posts);
        layoutManager = new LinearLayoutManager(getActivity());
        exploreRecyclerView.setLayoutManager(layoutManager);
        postsAdapter = new PostsAdapter(getActivity());
        exploreRecyclerView.setAdapter(postsAdapter);
        exploreViewModel.getPosts().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(@Nullable List<Post> postList) {
                postsAdapter.setPostList(postList);
                postsAdapter.notifyDataSetChanged();
            }
        });
        exploreViewModel.loadHappyPosts(mPosts);
        exploreTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("EXPLORE", "tab selected"+tab.getPosition());
                if(tab.getPosition() == 0)
                    exploreViewModel.loadHappyPosts(mPosts);
                if(tab.getPosition() == 1)
                    exploreViewModel.loadSadPosts(mPosts);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        exploreRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int id = layoutManager.findLastCompletelyVisibleItemPosition();
                if(id >= postsAdapter.getItemCount()-1){
                    String lastItemDate = postsAdapter.getLastItemDate();
                    exploreViewModel.addNewPost(lastItemDate, exploreTab.getSelectedTabPosition());
                }
            }
        });
        return root;
    }

}