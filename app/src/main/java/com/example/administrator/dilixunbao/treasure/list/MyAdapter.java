package com.example.administrator.dilixunbao.treasure.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.dilixunbao.custom.TreasureView;
import com.example.administrator.dilixunbao.treasure.Treasure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/11.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Treasure> mTreasureList = new ArrayList<>();

    public MyAdapter(List<Treasure> treasureList) {
        mTreasureList = treasureList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(new TreasureView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Treasure treasure = mTreasureList.get(position);
        TreasureView treasureView = holder.mTreasureView;
        treasureView.bindView(treasure);
        treasureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //回掉接口中的方法
                mOnItemClickListener.onItemClick(treasure);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTreasureList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TreasureView mTreasureView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTreasureView = (TreasureView) itemView;
        }
    }

    //接口回调第一步：申明一个接口
    interface OnItemClickListener {
        void onItemClick(Treasure treasure);
    }

    //接口回调第二步：申明一个借口类型的成员变量
    private OnItemClickListener mOnItemClickListener;

    //对外提供一个公共的访问方式
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
