package com.ktc.launcher.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class MarqueeTextViewAllTime extends TextView {
    public MarqueeTextViewAllTime(Context con) {
        super(con);
    }

    public MarqueeTextViewAllTime(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextViewAllTime(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
            Rect previouslyFocusedRect) {
    }



}
