package com.example.administrator.dilixunbao.register;

/**
 * Created by Administrator on 2017/8/2.
 */

public interface RegisterView {
    //显示进度条
    void showProgress();

    //隐藏进度条
    void hideProgress();

    //显示信息
    void showMessage(String message);

    //跳转到HomeActivity
    void navigateToHomeActivity();
}
