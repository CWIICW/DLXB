package com.example.administrator.dilixunbao.treasure.detail;

import java.util.List;

/**
 * Created by Administrator on 2017/8/9.
 */

public interface TreasureDetailActivityView {
    void showMessage(String message);
    void setTreasureDetailResultData(List<TreasureDetailResult> list);
}
