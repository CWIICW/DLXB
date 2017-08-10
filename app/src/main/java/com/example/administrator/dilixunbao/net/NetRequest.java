package com.example.administrator.dilixunbao.net;

import com.example.administrator.dilixunbao.register.RegisterResult;
import com.example.administrator.dilixunbao.User;
import com.example.administrator.dilixunbao.login.LoginResult;
import com.example.administrator.dilixunbao.treasure.Area;
import com.example.administrator.dilixunbao.treasure.Treasure;
import com.example.administrator.dilixunbao.treasure.detail.TreasureDetail;
import com.example.administrator.dilixunbao.treasure.detail.TreasureDetailResult;
import com.example.administrator.dilixunbao.treasure.hide.HideTreasure;
import com.example.administrator.dilixunbao.treasure.hide.HideTreasureResult;

import java.util.List;

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

    //获取宝藏信息
    @POST("/Handler/\n" +
            "TreasureHandler.ashx?action=show")
    Call<List<Treasure>> getTreasure(@Body Area area);

    //获取宝藏详细信息
    @POST("/Handler/TreasureHandler.ashx?action=tdetails")
    Call<List<TreasureDetailResult>> getTreasureDetail(@Body TreasureDetail treasureDetail);

    //上传宝藏
    @POST("/Handler/TreasureHandler.ashx?action=hide")
    Call<HideTreasureResult> hideTreasure(@Body HideTreasure hideTreasure);
}
