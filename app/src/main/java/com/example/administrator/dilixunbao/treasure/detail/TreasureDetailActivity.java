package com.example.administrator.dilixunbao.treasure.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.administrator.dilixunbao.R;
import com.example.administrator.dilixunbao.commons.ActivityUtils;
import com.example.administrator.dilixunbao.custom.TreasureView;
import com.example.administrator.dilixunbao.treasure.Treasure;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TreasureDetailActivity extends AppCompatActivity implements TreasureDetailActivityView {

    private static final String TREASURE_KEY = "treasure_key";
    @BindView(R.id.iv_navigation)
    ImageView mIvNavigation;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.frameLayout)
    FrameLayout mFrameLayout;
    @BindView(R.id.detail_treasure)
    TreasureView mDetailTreasure;
    @BindView(R.id.tv_detail_description)
    TextView mTvDetailDescription;
    private ActivityUtils mActivityUtils;
    private Treasure mTreasure;
    private BaiduMap mBaiduMap;

    public static void open(Context context, Treasure treasure) {
        Intent intent = new Intent(context, TreasureDetailActivity.class);
        intent.putExtra(TREASURE_KEY, treasure);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure_detail);
        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);
        //获取宝物信息
        Intent intent = getIntent();
        mTreasure = (Treasure) intent.getSerializableExtra(TREASURE_KEY);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mTreasure.getTitle());
        }
        //初始化地图
        initMapView();
        mDetailTreasure.bindView(mTreasure);
        TreasureDetail treasureDetail = new TreasureDetail(mTreasure.getId());
        new TreasureDetailActivityPresenter(this).getTreasureDecribe(treasureDetail);

    }

    private void initMapView() {
        LatLng latLng = new LatLng(mTreasure.getLatitude(), mTreasure.getLongitude());
        MapStatus mapStatus = new MapStatus.Builder()
                .target(latLng)
                .zoom(19)
                .rotate(0f)
                .overlook(-20f)
                .build();
        BaiduMapOptions options = new BaiduMapOptions();
        options.mapStatus(mapStatus);
        options.scaleControlEnabled(false); //隐藏比例尺
        options.zoomControlsEnabled(false); //隐藏缩放控件
        options.compassEnabled(false);
        options.overlookingGesturesEnabled(false);
        options.rotateGesturesEnabled(false);
        options.scrollGesturesEnabled(false);
        options.zoomGesturesEnabled(false);
        MapView mapView = new MapView(this, options);

        mBaiduMap = mapView.getMap();

        mFrameLayout.addView(mapView);

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.treasure_expanded);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.rotate(0);
        markerOptions.icon(bitmapDescriptor);
        markerOptions.position(latLng);
        mBaiduMap.addOverlay(markerOptions);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.iv_navigation})
    public void showPupMenu() {
        PopupMenu popupMenu = new PopupMenu(this, mIvNavigation);
        popupMenu.inflate(R.menu.menu_navigation);
        popupMenu.show();
    }


    //------------------------------------实现自视图接口上的方法-------------------------------------
    @Override
    public void showMessage(String message) {
        mActivityUtils.showToast(message);
    }

    @Override
    public void setTreasureDetailResultData(List<TreasureDetailResult> list) {
        if (list.size() >= 1) {
            mTvDetailDescription.setText(list.get(0).description);
            return;
        }
        mTvDetailDescription.setText("没有描述");
    }
}
