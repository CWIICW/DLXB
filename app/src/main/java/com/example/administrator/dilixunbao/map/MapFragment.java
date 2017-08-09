package com.example.administrator.dilixunbao.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.administrator.dilixunbao.R;
import com.example.administrator.dilixunbao.commons.ActivityUtils;
import com.example.administrator.dilixunbao.custom.TreasureView;
import com.example.administrator.dilixunbao.treasure.Area;
import com.example.administrator.dilixunbao.treasure.Treasure;
import com.example.administrator.dilixunbao.treasure.TreasureRepo;
import com.example.administrator.dilixunbao.treasure.detail.TreasureDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/8/3.
 */

public class MapFragment extends Fragment implements MapFragmentView {
    private static final int REQUEST_CODE = 100;
    @BindView(R.id.center)
    Space mCenter;
    @BindView(R.id.iv_located)
    ImageView mIvLocated;
    @BindView(R.id.btn_HideHere)
    Button mBtnHideHere;
    @BindView(R.id.centerLayout)
    RelativeLayout mCenterLayout;
    @BindView(R.id.iv_scaleUp)
    ImageView mIvScaleUp;
    @BindView(R.id.iv_scaleDown)
    ImageView mIvScaleDown;
    @BindView(R.id.tv_located)
    TextView mTvLocated;
    @BindView(R.id.tv_satellite)
    TextView mTvSatellite;
    @BindView(R.id.tv_compass)
    TextView mTvCompass;
    @BindView(R.id.ll_locationBar)
    LinearLayout mLlLocationBar;
    @BindView(R.id.tv_currentLocation)
    TextView mTvCurrentLocation;
    @BindView(R.id.iv_toTreasureInfo)
    ImageView mIvToTreasureInfo;
    @BindView(R.id.et_treasureTitle)
    EditText mEtTreasureTitle;
    @BindView(R.id.cardView)
    CardView mCardView;
    @BindView(R.id.layout_bottom)
    FrameLayout mLayoutBottom;
    @BindView(R.id.map_frame)
    FrameLayout mMapFrame;
    Unbinder unbinder;
    @BindView(R.id.treasureView)
    TreasureView mTreasureView;
    @BindView(R.id.hide_treasure)
    RelativeLayout mHideTreasure;
    private BaiduMap mBaiduMap;
    private static LatLng mCurrentLocation;
    private boolean isFirst = true;
    private LocationClient mLocationClient;
    private LatLng mCurrentStatus;
    private ActivityUtils mActivityUtils;
    private BitmapDescriptor mTreasure_dot;
    private Marker mCurrentMarker;
    private BitmapDescriptor mTreasure_expanded;
    private LatLng mTarget;
    private InfoWindow mInfoWindow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, null);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
        }
        return view;
    }

    //当onCreateView执行完毕之后执行
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        mActivityUtils = new ActivityUtils(this);
        //初始化地图相关
        initView();
        //初始化位置相关
        initLocation();
    }

    private static final int TREASURE_MODE_NORMAL = 0;
    private static final int TREASURE_MODE_SELECTED = 1;
    private static final int TREASURE_MODE_HIDE = 2;
    private int TREASURE_MODE_CURRENT = TREASURE_MODE_NORMAL;

    public void changeUiMode(int mode) {
        if (mode == TREASURE_MODE_CURRENT) {
            return;
        }
        TREASURE_MODE_CURRENT = mode;
        switch (mode) {
            case TREASURE_MODE_NORMAL:
                if (mCurrentMarker != null) {
                    mCurrentMarker.setVisible(true);
                }
                mBaiduMap.hideInfoWindow();
                mLayoutBottom.setVisibility(View.GONE);
                mCenterLayout.setVisibility(View.INVISIBLE);
                break;
            case TREASURE_MODE_SELECTED:
                mLayoutBottom.setVisibility(View.VISIBLE);
                mTreasureView.setVisibility(View.VISIBLE);
                mCenterLayout.setVisibility(View.GONE);
                mBaiduMap.showInfoWindow(mInfoWindow);
                mHideTreasure.setVisibility(View.GONE);
                break;
            case TREASURE_MODE_HIDE:
                if (mCurrentMarker != null) {
                    mCurrentMarker.setVisible(true);
                }
                mLayoutBottom.setVisibility(View.GONE);
                mTreasureView.setVisibility(View.GONE);
                mCenterLayout.setVisibility(View.VISIBLE);
                mBaiduMap.hideInfoWindow();
                mBtnHideHere.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLayoutBottom.setVisibility(View.VISIBLE);
                        mTreasureView.setVisibility(View.GONE);
                        mCenterLayout.setVisibility(View.VISIBLE);
                        mHideTreasure.setVisibility(View.VISIBLE);
                    }
                });
                break;
        }
    }

    //初始化位置相关
    private void initLocation() {
        //激活百度地图图层的定位功能
        mBaiduMap.setMyLocationEnabled(true);
        //第一步，初始化LocationClient类
        mLocationClient = new LocationClient(getContext().getApplicationContext());
        //第二步，配置定位SDK参数
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll"); //设置默认坐标系，默认GCJ02，误差大
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        mLocationClient.setLocOption(option);

        //第三步，注册位置监听
        mLocationClient.registerLocationListener(mBDLocationListener);
        //第四补，开启定位
        mLocationClient.start();
    }

    private BDLocationListener mBDLocationListener = new BDLocationListener() {
        //获得位置后执行
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            double latitude = bdLocation.getLatitude();//纬度
            double longitude = bdLocation.getLongitude();//经度
            mCurrentLocation = new LatLng(latitude, longitude);
            String addrStr = bdLocation.getAddrStr();//位置描述
            Log.e("TAG", "当前位于:" + addrStr + "经纬度是" + longitude + ":" + latitude);
            MyLocationData myLocationData = new MyLocationData.Builder()
                    .accuracy(100f)//精度
                    .latitude(latitude)
                    .longitude(longitude)
                    .build();
            mBaiduMap.setMyLocationData(myLocationData);
            updateView(mCurrentLocation);
            if (isFirst) {
                moveToLocation();
                isFirst = false;
            }

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };

    private void initView() {
        mTreasure_dot = BitmapDescriptorFactory.fromResource(R.mipmap.treasure_dot);
        mTreasure_expanded = BitmapDescriptorFactory.fromResource(R.mipmap.treasure_expanded);
        MapStatus mapStatus = new MapStatus.Builder()
                .overlook(0f)
                .rotate(0f)
                .zoom(11)
                .build();
        BaiduMapOptions options = new BaiduMapOptions();
        options.mapStatus(mapStatus);
        options.scaleControlEnabled(false); //隐藏比例尺
        options.zoomControlsEnabled(false); //隐藏缩放控件
        options.zoomGesturesEnabled(true);  //允许手势缩放
        //创建MapView对象
        MapView mapView = new MapView(getContext(), options);
        mBaiduMap = mapView.getMap();
        //将MapView填充到FrameLayout
        mMapFrame.addView(mapView, 0);
        //设置地图状态改变监听
        mBaiduMap.setOnMapStatusChangeListener(mOnMapStatusChangeListener);
        //设置地图覆盖物的点击监听
        mBaiduMap.setOnMarkerClickListener(mOnMarkerClickListener);
    }

    //设置地图覆盖物的点击监听
    private BaiduMap.OnMarkerClickListener mOnMarkerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if (mCurrentMarker != null) {
                mCurrentMarker.setVisible(true);
            }
            mCurrentMarker = marker;
            mCurrentMarker.setVisible(false);
            mInfoWindow = new InfoWindow(mTreasure_expanded, marker.getPosition(), 0, new InfoWindow.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick() {
                /* mBaiduMap.hideInfoWindow();
                    mCurrentMarker.setVisible(true);
                    mLayoutBottom.setVisibility(View.GONE);*/
                    changeUiMode(0);
                }
            });
            mBaiduMap.showInfoWindow(mInfoWindow);
            Bundle bundle = marker.getExtraInfo();
            int treasure_id = bundle.getInt("treasure_id");
            Treasure treasure = TreasureRepo.getInstance().getTreasure(treasure_id);
            mTreasureView.bindView(treasure);
          /*
            mLayoutBottom.setVisibility(View.VISIBLE);*/
            changeUiMode(1);
            return false;
        }
    };
    //设置地图状态改变监听
    private BaiduMap.OnMapStatusChangeListener mOnMapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {
        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {
        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            //地图状态改变之后
            //获取地图状态改变之后的位置
            mTarget = mapStatus.target;
            if (mTarget != mCurrentStatus) {
                //此时认为地图状态真的改变了
                updateView(mTarget);
                mCurrentStatus = mTarget;
            }
        }
    };

    //拿到宝藏
    private void updateView(LatLng mapStatus) {
        double latitude = mapStatus.latitude;//纬度
        double longitude = mapStatus.longitude;//经度
        Area area = new Area();
        area.setMaxLat(Math.ceil(latitude));
        area.setMaxLng(Math.ceil(longitude));
        area.setMinLat(Math.floor(latitude));
        area.setMinLng(Math.floor(longitude));
        new MapFragmentPresenter(this).getTreasureInArea(area);

    }

    //--------------------------------给地图上的控件添加点击事件------------------------
    //点击定位
    @OnClick({R.id.tv_located})
    public void moveToLocation() {

        MapStatus mapStatus = new MapStatus.Builder()
                .zoom(18)
                .target(mCurrentLocation)
                .build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
    }

    //切换地图类型
    @OnClick({R.id.tv_satellite})
    public void switchMapType() {
        //获取当前的地图类型
        int mapType = mBaiduMap.getMapType();
        //根据当前地图类型决定接下来要切换的地图类型
        mapType = mapType == BaiduMap.MAP_TYPE_NORMAL ? BaiduMap.MAP_TYPE_SATELLITE : BaiduMap.MAP_TYPE_NORMAL;
        String msg = mapType == BaiduMap.MAP_TYPE_NORMAL ? "卫星" : "普通";

        mBaiduMap.setMapType(mapType);
        mTvSatellite.setText(msg);
    }

    //指南针
    @OnClick({R.id.tv_compass})
    public void compass() {
        boolean compassEnabled = mBaiduMap.getUiSettings().isCompassEnabled();
        mBaiduMap.getUiSettings().setCompassEnabled(!compassEnabled);
    }

    //地图放大缩小按钮
    @OnClick({R.id.iv_scaleUp, R.id.iv_scaleDown})
    public void switchMapScale(View view) {
        switch (view.getId()) {
            case R.id.iv_scaleUp:
                //放大
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
                break;
            case R.id.iv_scaleDown:
                //缩小
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
                break;
        }
    }

    @OnClick({R.id.treasureView})
    public void navigateToTreasureDetailActivity() {
        Bundle bundle = mCurrentMarker.getExtraInfo();
        int treasure_id = bundle.getInt("treasure_id");
        Treasure treasure = TreasureRepo.getInstance().getTreasure(treasure_id);
        TreasureDetailActivity.open(getContext(),treasure);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationClient.requestLocation();
                } else {
                    Toast.makeText(getContext(), "权限不足", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //--------------------------实现自视图接口的方法------------------------
    @Override
    public void showMessage(String message) {
        mActivityUtils.showToast(message);
    }

    @Override
    public void setTreasureData(List<Treasure> treasureList) {
        mBaiduMap.clear();
        for (Treasure mTreasure : treasureList) {
            Bundle bundle = new Bundle();
            bundle.putInt("treasure_id", mTreasure.getId());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.anchor(0.5f, 0.5f);
            markerOptions.extraInfo(bundle);
            markerOptions.icon(mTreasure_dot);
            markerOptions.position(new LatLng(mTreasure.getLatitude(), mTreasure.getLongitude()));
            mBaiduMap.addOverlay(markerOptions);
        }
    }

    public static LatLng getLocation() {
        return mCurrentLocation;
    }
}
