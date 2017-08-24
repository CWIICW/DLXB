package com.example.administrator.dilixunbao.account;

import com.example.administrator.dilixunbao.UserPrefs;
import com.example.administrator.dilixunbao.net.NetClient;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/14.
 */

public class AccountPresenter {
    private AccountView mAccountView;

    public AccountPresenter(AccountView accountView) {
        mAccountView = accountView;
    }

    public void uploadHeadIcon(File file) {
        mAccountView.showProgress();
        RequestBody requestBody = RequestBody.create(null, file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("上传头像", "icon.jpg", requestBody);
        NetClient.getInstance().getNetRequest().uploadImage(part).enqueue(new Callback<UploadResult>() {
            @Override
            public void onResponse(Call<UploadResult> call, Response<UploadResult> response) {
                mAccountView.hideProgress();
                if (response.isSuccessful()) {
                    UploadResult uploadResult = response.body();
                    if (uploadResult == null) {
                        mAccountView.showMessage("未知错误");
                        return;
                    }
                    if (uploadResult.getCount() == 0) {
                        mAccountView.showMessage(uploadResult.getMsg());
                        return;
                    }
                    int tokenid = UserPrefs.getInstance().getTokenid();
                    final String url = uploadResult.getUrl();
                    String photo = url.substring(url.lastIndexOf("/") + 1);
                    Update update = new Update(tokenid, photo);
                    mAccountView.showProgress();
                    NetClient.getInstance().getNetRequest().updateImage(update).enqueue(new Callback<UpdateResult>() {
                        @Override
                        public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
                            mAccountView.hideProgress();
                            if (response.isSuccessful()) {
                                UpdateResult updateResult = response.body();
                                if (updateResult == null) {
                                    mAccountView.showMessage("未知错误");
                                    return;
                                }
                                if (updateResult.getCode() != 1) {
                                    mAccountView.showMessage(updateResult.getMsg());
                                    return;
                                }
                                //缓存头像
                                UserPrefs.getInstance().setPhoto(NetClient.BASE_URL + url);
                                //更新头像
                                mAccountView.updatePhoto(NetClient.BASE_URL + url);
                            }
                        }

                        @Override
                        public void onFailure(Call<UpdateResult> call, Throwable t) {
                            mAccountView.hideProgress();
                            mAccountView.showMessage("更新失败");
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<UploadResult> call, Throwable t) {
                mAccountView.hideProgress();
                mAccountView.showMessage("上传失败");
            }
        });
    }
}
