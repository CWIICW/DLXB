package com.example.administrator.dilixunbao.Register;

import com.example.administrator.dilixunbao.User;
import com.example.administrator.dilixunbao.UserPrefs;
import com.example.administrator.dilixunbao.login.LoginResult;
import com.example.administrator.dilixunbao.net.NetClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Administrator on 2017/8/2.
 */

public class RegisterPresenter {
    private RegisterView mRegisterView;

    public RegisterPresenter(RegisterView registerView) {
        mRegisterView = registerView;
    }

    public void register(User user) {
        mRegisterView.showProgress();
        NetClient.getInstance().getNetRequest().register(user).enqueue(new Callback<RegisterResult>() {
            @Override
            public void onResponse(Call<RegisterResult> call, Response<RegisterResult> response) {
                //隐藏进度条
                mRegisterView.hideProgress();
                if (response.isSuccessful()) {
                    RegisterResult registerResult = response.body();
                    if (registerResult == null) {
                        //显示信息，未知错误
                        mRegisterView.showMessage("未知错误!");
                        return;
                    }
                    if (registerResult.getErrcode() != 1) {
                        mRegisterView.showMessage(registerResult.getErrmsg());
                        return;
                    }
                    mRegisterView.showMessage("注册成功");
                    //缓存TokenId
                    UserPrefs.getInstance().setTokenid(registerResult.getTokenid());
                    //跳转到HomeActivity
                    mRegisterView.navigateToHomeActivity();
                }
            }

            @Override
            public void onFailure(Call<RegisterResult> call, Throwable t) {
                //隐藏进度条
                mRegisterView.hideProgress();
                //显示错误信息
                mRegisterView.showMessage("请求失败" + t.getMessage());
            }


        });
    }
}
