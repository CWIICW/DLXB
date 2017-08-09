package com.example.administrator.dilixunbao.map;


import com.example.administrator.dilixunbao.treasure.Treasure;

import java.util.List;

/**
 * Created by MACHENIKE on 2017/8/7.
 */

public interface MapFragmentView {
    void showMessage(String message);

    void setTreasureData(List<Treasure> treasureList);
}
