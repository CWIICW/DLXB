package com.example.administrator.dilixunbao.login;

import com.example.administrator.dilixunbao.User;
import com.example.administrator.dilixunbao.UserPrefs;
import com.example.administrator.dilixunbao.net.NetClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/2.
 */

public class LoginPresenter {
    private LoginView mLoginView;

    public LoginPresenter(LoginView loginView) {
        mLoginView = loginView;
    }

    //登陆业务
    public void login(final User user) {
        //显示进度条
        mLoginView.showProgress();
        NetClient.getInstance().getNetRequest().login(user).enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                //隐藏进度条
                mLoginView.hideProgress();
                if (response.isSuccessful()) {
                    LoginResult mLoginResult = response.body();
                    if (mLoginResult == null) {
                        //显示信息，未知错误
                        mLoginView.showMessage("未知错误!");
                        return;
                    }
                    if (mLoginResult.getErrcode() != 1) {
                        mLoginView.showMessage(mLoginResult.getErrmsg());
                        return;
                    }
                    mLoginView.showMessage("登录成功");
                    String headpic = mLoginResult.getHeadpic();
                    int tokenid = mLoginResult.getTokenid();
                    //缓存头
                    UserPrefs.getInstance().setPhoto(NetClient.BASE_URL + headpic);
                    //缓存TokenId
                    UserPrefs.getInstance().setTokenid(tokenid);
                    //跳转到HomeActivity
                    mLoginView.navigateToHomeActivity();
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                //隐藏进度条
                mLoginView.hideProgress();
                //显示错误信息
                mLoginView.showMessage("请求失败" + t.getMessage());

            }
        });
    }
}
