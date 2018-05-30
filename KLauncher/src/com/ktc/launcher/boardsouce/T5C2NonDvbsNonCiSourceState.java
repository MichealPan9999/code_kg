package com.ktc.launcher.boardsouce;

import android.content.Context;

import com.ktc.launcher.bean.InputSourceItem;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.ktc.launcher.R;

import java.util.ArrayList;

/**
 * Created by xiacf on 2017/11/3.
 */

public class T5C2NonDvbsNonCiSourceState extends AbBoardSourceState {
    private  final int[] mT5C2NonDvbsNonCiSourceList = {
            TvCommonManager.INPUT_SOURCE_DTV,
            TvCommonManager.INPUT_SOURCE_DTV,
            TvCommonManager.INPUT_SOURCE_ATV,
            TvCommonManager.INPUT_SOURCE_CVBS,
            TvCommonManager.INPUT_SOURCE_YPBPR,
            TvCommonManager.INPUT_SOURCE_HDMI,
            TvCommonManager.INPUT_SOURCE_HDMI2,
    };
    private ArrayList<InputSourceItem> mT5C2SourceListArray = new ArrayList<InputSourceItem>();
    String[] t8c2NonsDvbsNonCiSourceName = null;
    @Override
    public ArrayList<InputSourceItem> getSupportSourceList(Context context) {
        t8c2NonsDvbsNonCiSourceName = context.getResources().getStringArray(
                R.array.str_arr_input_source_t5c2_nondvbs_nonci);
        mT5C2SourceListArray.clear();
        for (int i = 0; i < mT5C2NonDvbsNonCiSourceList.length; i++){
            InputSourceItem inputSourceItem = new InputSourceItem();
            inputSourceItem.setSourceName(t8c2NonsDvbsNonCiSourceName[i]);
            inputSourceItem.setPoistion(mT5C2NonDvbsNonCiSourceList[i]);

            inputSourceItem.setTypeFlag(typeFlag[mT5C2NonDvbsNonCiSourceList[i]]);
            mT5C2SourceListArray.add(inputSourceItem);
        }
        return mT5C2SourceListArray;
    }

    @Override
    public boolean switchSource(int position) {
        return false;
    }

    @Override
    public int getCurrentFocusPosition(int inputSource) {
        switch(inputSource){
            case TvCommonManager.INPUT_SOURCE_DTV:
                switch (TvChannelManager.getInstance().getCurrentDtvRouteIndex()){
                    case TvCommonManager.TV_SYSTEM_DVBT:
                        return 0;
                    case TvCommonManager.TV_SYSTEM_DVBC:
                        return 1;
                    default:
                        return 0;
                }
            case TvCommonManager.INPUT_SOURCE_ATV:
                return 2;
            case TvCommonManager.INPUT_SOURCE_CVBS:
                return 3;
            case TvCommonManager.INPUT_SOURCE_YPBPR:
                return 4;
            case TvCommonManager.INPUT_SOURCE_HDMI:
                return 5;
            case TvCommonManager.INPUT_SOURCE_HDMI2:
                return 6;
            default:
                return 0;
        }
    }
}
