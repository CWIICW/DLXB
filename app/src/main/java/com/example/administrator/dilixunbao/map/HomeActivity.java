package com.example.administrator.dilixunbao.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.administrator.dilixunbao.MainActivity;
import com.example.administrator.dilixunbao.R;
import com.example.administrator.dilixunbao.UserPrefs;
import com.example.administrator.dilixunbao.commons.ActivityUtils;
import com.example.administrator.dilixunbao.treasure.TreasureRepo;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;
    @BindView(R.id.navigation)
    NavigationView mNavigation;
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    private Unbinder mBind;
    private ActivityUtils mUtils;
    private ImageView mUserIcon;
    private MapFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mBind = ButterKnife.bind(this);
        mUtils = new ActivityUtils(this);
        FragmentManager manager = getSupportFragmentManager();
        mMapFragment = (MapFragment) manager.findFragmentById(R.id.mapFragment);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //将DrawerLayout和Toolbar的状态进行同步
        actionBarDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        //给侧滑页面设置点击监听
        mNavigation.setNavigationItemSelectedListener(this);
        mUserIcon = (ImageView) mNavigation.getHeaderView(0).findViewById(R.id.iv_usericon);
        mUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/8/3 跳转到个人信息页面
                mUtils.showToast("跳转到个人信息页面");
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        String photo = UserPrefs.getInstance().getPhoto();
        if (photo != null) {
            Picasso.with(this).load(photo).into(mUserIcon);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        TreasureRepo.getInstance().clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_hide:
                mMapFragment.changeUiMode(2);
                break;
            case R.id.menu_my_list:
                mUtils.showToast("我的列表");
                break;
            case R.id.menu_help:
                mUtils.showToast("帮助");
                break;
            case R.id.menu_logout:
                UserPrefs.getInstance().clearUser();
                mUtils.startActivity(MainActivity.class);
                finish();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}
