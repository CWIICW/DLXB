package com.example.administrator.dilixunbao.treasure.hide;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.baidu.mapapi.model.LatLng;
import com.example.administrator.dilixunbao.R;
import com.example.administrator.dilixunbao.UserPrefs;
import com.example.administrator.dilixunbao.commons.ActivityUtils;
import com.example.administrator.dilixunbao.treasure.TreasureRepo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HideTreasureActivity extends AppCompatActivity implements HideTreasureView {

    private static final String TREASURE_TITLE = "treasure_title";
    private static final String TREASURE_LOCATION = "treasure_location";
    private static final String TREASURE_ADRESS = "treasure_adress";
    @BindView(R.id.hide_send)
    ImageView mHideSend;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_description)
    EditText mEtDescription;
    private Unbinder mUnbinder;
    private ActivityUtils mUtils;
    private String mTitle;
    private LatLng mLocation;
    private String mAsress;
    private ProgressDialog mProgressDialog;

    public static void open(Context context, String treasureTitle, LatLng currentStatus, String mCurrentAdress) {
        Intent intent = new Intent(context, HideTreasureActivity.class);
        intent.putExtra(TREASURE_TITLE, treasureTitle);
        intent.putExtra(TREASURE_LOCATION, currentStatus);
        intent.putExtra(TREASURE_ADRESS, mCurrentAdress);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hide_treasure);
        mUnbinder = ButterKnife.bind(this);
        mUtils = new ActivityUtils(this);
        Intent intent = getIntent();
        mTitle = intent.getStringExtra(TREASURE_TITLE);
        mLocation = intent.getParcelableExtra(TREASURE_LOCATION);
        mAsress = intent.getStringExtra(TREASURE_ADRESS);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() == null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mTitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //点击发送，发送请求
    @OnClick(R.id.hide_send)
    public void onViewClicked() {
        String description = mEtDescription.getText().toString();
        int tokenid = UserPrefs.getInstance().getTokenid();
        HideTreasure hideTreasure = new HideTreasure();
        hideTreasure.setAltitude(0);
        hideTreasure.setDescription(description);
        hideTreasure.setLatitude(mLocation.latitude);
        hideTreasure.setLongitude(mLocation.longitude);
        hideTreasure.setLocation(mAsress);
        hideTreasure.setTitle(mTitle);
        hideTreasure.setTokenId(tokenid);
        new HideTreasurePresenter(this).hideTreasure(hideTreasure);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    //---------------------------实现自视图接口的方法---------------------------------
    @Override
    public void showProgress() {
        mProgressDialog = ProgressDialog.show(this, "上传宝藏", "宝藏正在上传中......");
    }

    @Override
    public void hideProgress() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showMessage(String message) {
        mUtils.showToast(message);
    }

    @Override
    public void backHome() {

        finish();
        // 清除缓存 : 为了返回到之前的页面重新去请求数据
        TreasureRepo.getInstance().clear();

    }
}
