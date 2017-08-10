package com.example.administrator.dilixunbao.treasure.hide;

import com.example.administrator.dilixunbao.net.NetClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/10.
 */

public class HideTreasurePresenter {
    private HideTreasureView mHideTreasureView;

    public HideTreasurePresenter(HideTreasureView hideTreasureView) {
        mHideTreasureView = hideTreasureView;
    }

    public void hideTreasure(final HideTreasure hideTreasure) {
        mHideTreasureView.showProgress();
        NetClient.getInstance().getNetRequest().hideTreasure(hideTreasure).enqueue(new Callback<HideTreasureResult>() {
            @Override
            public void onResponse(Call<HideTreasureResult> call, Response<HideTreasureResult> response) {
                mHideTreasureView.hideProgress();
                if (response.isSuccessful()) {
                    HideTreasureResult hideTreasureResult = response.body();
                    if (hideTreasureResult == null) {
                        return;
                    }
                    mHideTreasureView.backHome();
                    mHideTreasureView.showMessage("上传成功");
                }
            }

            @Override
            public void onFailure(Call<HideTreasureResult> call, Throwable t) {
                mHideTreasureView.hideProgress();
                mHideTreasureView.showMessage("上传失败");
            }
        });
    }
}
