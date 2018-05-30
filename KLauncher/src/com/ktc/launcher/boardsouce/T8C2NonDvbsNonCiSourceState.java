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

public class T8C2NonDvbsNonCiSourceState extends AbBoardSourceState {
    private  final int[] mT8C2NonDvbsNonCiSourceList = {
            TvCommonManager.INPUT_SOURCE_DTV,
            TvCommonManager.INPUT_SOURCE_DTV,
            TvCommonManager.INPUT_SOURCE_ATV,
            TvCommonManager.INPUT_SOURCE_CVBS,
            TvCommonManager.INPUT_SOURCE_YPBPR,
            TvCommonManager.INPUT_SOURCE_HDMI,
            TvCommonManager.INPUT_SOURCE_HDMI2,
            TvCommonManager.INPUT_SOURCE_VGA
    };
    private ArrayList<InputSourceItem> mT8C2SourceListArray = new ArrayList<InputSourceItem>();
    String[] t8c2NonDvbsNonCiSourceName = null;
    @Override
    public ArrayList<InputSourceItem> getSupportSourceList(Context context) {
        t8c2NonDvbsNonCiSourceName = context.getResources().getStringArray(
                R.array.str_arr_input_source_t8c2_nondvbs_nonci);
        mT8C2SourceListArray.clear();
        for (int i = 0; i < mT8C2NonDvbsNonCiSourceList.length; i++){
            InputSourceItem inputSourceItem = new InputSourceItem();
            inputSourceItem.setSourceName(t8c2NonDvbsNonCiSourceName[i]);
            inputSourceItem.setPoistion(mT8C2NonDvbsNonCiSourceList[i]);

            inputSourceItem.setTypeFlag(typeFlag[mT8C2NonDvbsNonCiSourceList[i]]);
            mT8C2SourceListArray.add(inputSourceItem);
        }
        return mT8C2SourceListArray;
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
            case TvCommonManager.INPUT_SOURCE_VGA:
                return 7;
            default:
                return 0;
        }
    }
}
