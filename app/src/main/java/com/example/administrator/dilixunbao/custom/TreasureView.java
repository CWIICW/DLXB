package com.example.administrator.dilixunbao.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.administrator.dilixunbao.R;
import com.example.administrator.dilixunbao.map.MapFragment;
import com.example.administrator.dilixunbao.treasure.Treasure;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/9.
 */

public class TreasureView extends RelativeLayout {
    @BindView(R.id.tv_treasureTitle)
    TextView mTvTreasureTitle;
    @BindView(R.id.tv_distance)
    TextView mTvDistance;
    @BindView(R.id.tv_treasureLocation)
    TextView mTvTreasureLocation;
    @BindView(R.id.iv_arrow)
    ImageView mIvArrow;

    public TreasureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TreasureView(Context context) {
        super(context);
        init();
    }

    public TreasureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_treasure, this, true);
        ButterKnife.bind(this);
    }
    public void bindView(Treasure treasure){
        String title = treasure.getTitle();
        String location = treasure.getLocation();
        double latitude = treasure.getLatitude();
        double longitude = treasure.getLongitude();
        LatLng treasureLocation = new LatLng(latitude, longitude);
        LatLng mCurrentLocation = MapFragment.getLocation();

        double distance = DistanceUtil.getDistance(treasureLocation, mCurrentLocation);

        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String s = decimalFormat.format(distance / 1000) + "km";

        mTvTreasureTitle.setText(title);
        mTvTreasureLocation.setText(location);
        mTvDistance.setText(s);
    }
}
