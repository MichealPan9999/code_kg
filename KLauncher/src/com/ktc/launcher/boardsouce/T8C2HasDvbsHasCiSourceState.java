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

public class T8C2HasDvbsHasCiSourceState extends AbBoardSourceState {
    private  final int[] mT8C2HasDvbsHasCiSourceList = {
            TvCommonManager.INPUT_SOURCE_DTV,
            TvCommonManager.INPUT_SOURCE_ATV,
            TvCommonManager.INPUT_SOURCE_CVBS,
            TvCommonManager.INPUT_SOURCE_HDMI,
            TvCommonManager.INPUT_SOURCE_HDMI2,
            TvCommonManager.INPUT_SOURCE_VGA
    };
    private ArrayList<InputSourceItem> mT8C2HasDvbsHasCiSourceListArray = new ArrayList<InputSourceItem>();
    String[] t8c2SourceName = null;
    @Override
    public ArrayList<InputSourceItem> getSupportSourceList(Context context) {
        t8c2SourceName = context.getResources().getStringArray(
                R.array.str_arr_input_source_t8c2_hasdvbs_hasci);
        mT8C2HasDvbsHasCiSourceListArray.clear();
        for (int i = 0; i < mT8C2HasDvbsHasCiSourceList.length; i++){
            InputSourceItem inputSourceItem = new InputSourceItem();
            inputSourceItem.setSourceName(t8c2SourceName[i]);
            inputSourceItem.setPoistion(mT8C2HasDvbsHasCiSourceList[i]);

            inputSourceItem.setTypeFlag(typeFlag[mT8C2HasDvbsHasCiSourceList[i]]);
            mT8C2HasDvbsHasCiSourceListArray.add(inputSourceItem);
        }
        return mT8C2HasDvbsHasCiSourceListArray;
    }

    @Override
    public boolean switchSource(int position) {
        return false;
    }

    @Override
    public int getCurrentFocusPosition(int inputSource) {
        switch(inputSource){
            case TvCommonManager.INPUT_SOURCE_DTV:
                return 0;
            case TvCommonManager.INPUT_SOURCE_ATV:
                return 1;
            case TvCommonManager.INPUT_SOURCE_CVBS:
                return 2;
            case TvCommonManager.INPUT_SOURCE_HDMI:
                return 3;
            case TvCommonManager.INPUT_SOURCE_HDMI2:
                return 4;
            case TvCommonManager.INPUT_SOURCE_VGA:
                return 5;
            default:
                return 0;
        }
    }
}
