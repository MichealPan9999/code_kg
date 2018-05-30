package com.ktc.launcher.boardsouce;

import android.content.Context;

import com.ktc.launcher.bean.InputSourceItem;

import java.util.ArrayList;

/**
 * Created by xiacf on 2017/11/3.
 */

public class SourceContext {
    private AbBoardSourceState abBoardSourceState;

    public SourceContext(AbBoardSourceState abBoardSourceState){
        this.abBoardSourceState = abBoardSourceState;
    }

    public void setAbBoardSourceState(AbBoardSourceState abBoardSourceState) {
        this.abBoardSourceState = abBoardSourceState;
    }

    public AbBoardSourceState getAbBoardSourceState() {
        return abBoardSourceState;
    }

    public ArrayList<InputSourceItem> getSupportSourceList(Context context){
        return abBoardSourceState.getSupportSourceList(context);
    }

    public boolean switchSource(int position){
        return abBoardSourceState.switchSource(position);
    }

    public int getCurrentFocusPosition(int source){
        return abBoardSourceState.getCurrentFocusPosition(source);
    }
}
