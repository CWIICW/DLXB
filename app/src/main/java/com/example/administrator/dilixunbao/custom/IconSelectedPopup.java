package com.example.administrator.dilixunbao.custom;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.example.administrator.dilixunbao.R;
import com.example.administrator.dilixunbao.commons.ActivityUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/14.
 */

public class IconSelectedPopup extends PopupWindow {
    private Activity mActivity;
    private OnClickListener mOnClickListener;

    public IconSelectedPopup(Activity mActivity, OnClickListener onClickListener) {
        super(mActivity.getLayoutInflater().inflate(R.layout.window_select_icon, null), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        this.mActivity = mActivity;
        mOnClickListener = onClickListener;
        ButterKnife.bind(this, getContentView());
        setBackgroundDrawable(new BitmapDrawable());
    }

    public void show() {
        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @OnClick({R.id.btn_gallery, R.id.btn_camera})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_gallery:
                mOnClickListener.toGallery();
                break;
            case R.id.btn_camera:
                mOnClickListener.toCamera();
                break;
        }
    }

    public interface OnClickListener {
        void toGallery();

        void toCamera();
    }
}
