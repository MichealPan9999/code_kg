package com.ktc.launcher.boardsouce;

import android.content.Context;


import com.ktc.launcher.bean.InputSourceItem;

import java.util.ArrayList;

/**
 * Created by xiacf on 2017/11/3.
 */

public abstract class AbBoardSourceState {
    public static final int DTV = 0;
    public static final int ATV = 1;
    public static final int COMPONENT = 2;
    public static final int HDMI1 =3 ;
    public static final int HDMI2 = 4;
    public static final int AV = 5;
    public static final int VGA = 6;

    protected int[] typeFlag = { VGA, ATV, AV, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, COMPONENT,-1, -1, -1, -1, -1, -1, HDMI1,
            HDMI2, -1, -1, -1, DTV, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1 };

    public abstract ArrayList<InputSourceItem> getSupportSourceList(Context context);
    public abstract boolean switchSource(int position);
    public abstract int getCurrentFocusPosition(int inputSource);
}
