package com.example.administrator.dilixunbao.net;

import com.example.administrator.dilixunbao.Register.RegisterResult;
import com.example.administrator.dilixunbao.User;
import com.example.administrator.dilixunbao.login.LoginResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/8/2.
 */

public interface NetRequest {
    //登录
    @POST("/Handler/UserHandler.ashx?action=login")
    Call<LoginResult> login(@Body User user);

    //注册
    @POST("/Handler/UserHandler.ashx?action=register")
    Call<RegisterResult> register(@Body User user);
}
