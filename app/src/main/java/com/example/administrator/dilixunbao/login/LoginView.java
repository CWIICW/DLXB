package com.example.administrator.dilixunbao.login;

/**
 * Created by Administrator on 2017/8/2.
 */

public interface LoginView {
    //显示进度条
    void showProgress();
    //隐藏进度条
    void hideProgress();
    //显示信息
    void showMessage(String message);
    //跳转到HomeActivity
    void navigateToHomeActivity();
}