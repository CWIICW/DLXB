package com.example.administrator.dilixunbao.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.dilixunbao.R;
import com.example.administrator.dilixunbao.UserPrefs;
import com.example.administrator.dilixunbao.commons.ActivityUtils;
import com.example.administrator.dilixunbao.custom.IconSelectedPopup;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import org.hybridsquad.android.library.CropHandler;
import org.hybridsquad.android.library.CropHelper;
import org.hybridsquad.android.library.CropParams;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AccountActivity extends AppCompatActivity implements AccountView {

    @BindView(R.id.account_toolbar)
    Toolbar mAccountToolbar;
    @BindView(R.id.iv_usericon)
    CircularImageView mIvUsericon;
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.linearLayout)
    RelativeLayout mLinearLayout;
    private Unbinder mUnbinder;
    private ActivityUtils mUtils;
    private ProgressDialog mProgressDialog;
    private IconSelectedPopup mIconSelectedPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mUnbinder = ButterKnife.bind(this);
        mUtils = new ActivityUtils(this);

        setSupportActionBar(mAccountToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.account_msg);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private IconSelectedPopup.OnClickListener mOnClickListener = new IconSelectedPopup.OnClickListener() {
        @Override
        public void toGallery() {
            Intent intent = CropHelper.buildCropFromGalleryIntent(mCropHandler.getCropParams());
            startActivityForResult(intent, CropHelper.REQUEST_CROP);
        }

        @Override
        public void toCamera() {
            Intent intent = CropHelper.buildCaptureIntent(mCropHandler.getCropParams().uri);
            startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        String photo = UserPrefs.getInstance().getPhoto();
        if (photo != null) {
            Picasso.with(this).load(photo).into(mIvUsericon);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.iv_usericon)
    public void onViewClicked() {
        if (mIconSelectedPopup == null) {
            mIconSelectedPopup = new IconSelectedPopup(AccountActivity.this, mOnClickListener);
        }
        if (mIconSelectedPopup.isShowing()) {
            mIconSelectedPopup.dismiss();
            return;
        }
        mIconSelectedPopup.show();
    }

    private CropHandler mCropHandler = new CropHandler() {
        @Override
        public void onPhotoCropped(Uri uri) {
            File file = new File(uri.getPath());
            new AccountPresenter(AccountActivity.this).uploadHeadIcon(file);
        }

        @Override
        public void onCropCancel() {
            mUtils.showToast("剪切取消");
        }

        @Override
        public void onCropFailed(String message) {
            mUtils.showToast("剪切失败");
        }

        @Override
        public CropParams getCropParams() {
            return new CropParams();
        }

        @Override
        public Activity getContext() {
            return AccountActivity.this;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropHelper.handleResult(mCropHandler, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        if (mCropHandler.getCropParams() != null) {
            CropHelper.clearCachedCropFile(mCropHandler.getCropParams().uri);
        }
        super.onDestroy();
        mUnbinder.unbind();
    }

    //-----------------------------------------实现自视图接口的方法----------------------------------
    @Override
    public void showProgress() {
        mProgressDialog = ProgressDialog.show(this, "上传头像", "头像上传中...");
    }

    @Override
    public void showMessage(String message) {
        mUtils.showToast(message);
    }

    @Override
    public void hideProgress() {
        mProgressDialog.dismiss();
    }

    @Override
    public void updatePhoto(String url) {
        if (url != null) {
            Picasso.with(this).load(url)
                    .error(R.mipmap.user_icon)// 加载错误显示的视图
                    .placeholder(R.mipmap.user_icon)// 占位视图
                    .into(mIvUsericon);
        }
    }
}
