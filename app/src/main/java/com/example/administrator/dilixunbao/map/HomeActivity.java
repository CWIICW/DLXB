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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.administrator.dilixunbao.MainActivity;
import com.example.administrator.dilixunbao.R;
import com.example.administrator.dilixunbao.UserPrefs;
import com.example.administrator.dilixunbao.account.AccountActivity;
import com.example.administrator.dilixunbao.commons.ActivityUtils;
import com.example.administrator.dilixunbao.treasure.TreasureRepo;
import com.example.administrator.dilixunbao.treasure.list.TreasureListFragment;
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
    private FragmentManager mFragmentManager;
    private TreasureListFragment mTreasureListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mBind = ButterKnife.bind(this);
        mUtils = new ActivityUtils(this);
        mFragmentManager = getSupportFragmentManager();
        mMapFragment = (MapFragment) mFragmentManager.findFragmentById(R.id.mapFragment);
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
                mUtils.startActivity(AccountActivity.class);
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

    //当activity创建的时候执行，在activity的每一次生命周期中只执行一次
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //当你执行invalidateOptionsMenu()就会执行一次
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.getItem(0);
        if (mTreasureListFragment != null && mTreasureListFragment.isAdded()) {
            item.setIcon(R.drawable.ic_map);
        } else {
            item.setIcon(R.drawable.ic_view_list);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_toggle:
                showTreasureList();
                //唤起onPrepareOptionsMenu
                invalidateOptionsMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showTreasureList() {
        mFragmentManager.popBackStack();
        if (mTreasureListFragment != null && mTreasureListFragment.isAdded()) {
            //移除TreasureListFragment
            mFragmentManager.beginTransaction().remove(mTreasureListFragment).commit();
            return;
        }
        mTreasureListFragment = new TreasureListFragment();
        //添加TreasureListFragment
        mFragmentManager.beginTransaction().replace(R.id.fragment_container, mTreasureListFragment).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        if (mMapFragment.isNomalMode()) {
            super.onBackPressed();
        } else {
            mMapFragment.changeUiMode(0);
        }
    }
}
