package com.example.administrator.dilixunbao.treasure.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.dilixunbao.R;
import com.example.administrator.dilixunbao.treasure.Treasure;
import com.example.administrator.dilixunbao.treasure.TreasureRepo;
import com.example.administrator.dilixunbao.treasure.detail.TreasureDetailActivity;

/**
 * Created by Administrator on 2017/8/11.
 */

public class TreasureListFragment extends Fragment {

    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecyclerView = new RecyclerView(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setBackgroundResource(R.mipmap.screen_bg);
        MyAdapter myAdapter = new MyAdapter(TreasureRepo.getInstance().getTreasure());
        myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Treasure treasure) {
                //跳转到宝藏详情页面
                TreasureDetailActivity.open(getContext(), treasure);
            }
        });
        mRecyclerView.setAdapter(myAdapter);

        return mRecyclerView;
    }
}
