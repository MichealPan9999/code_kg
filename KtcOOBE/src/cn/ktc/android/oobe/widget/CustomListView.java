package cn.ktc.android.oobe.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.ListView;

/**
 * 
 * @author yejb yejinbing@gmail.com
 * 
 */
public class CustomListView extends ListView {

	public CustomListView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	
	public CustomListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	
	public CustomListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//		switch (keyCode) {
//
//			/**
//			 * 最顶部按“上”键，选中最后一项
//			 */
//			case KeyEvent.KEYCODE_DPAD_UP: {
//				if (getSelectedItemPosition() == 0) {
//					setSelection(getAdapter().getCount() - 1);
//					return true;
//				}
//			} break;
//
//			/**
//			 * 最底部按“下”键，选中第一项
//			 */
//			case KeyEvent.KEYCODE_DPAD_DOWN: {
//				if (getSelectedItemPosition() == getAdapter().getCount() - 1) {
//					setSelection(0);
//					return true;
//				}
//			} break;
//
//			/**
//			 * 将“频道+”键映射为“上翻页”键
//			 */
//			case KeyEvent.KEYCODE_CHANNEL_UP: {
//				KeyEvent e = new KeyEvent(event.getDownTime(), event.getEventTime(),
//						event.getAction(), KeyEvent.KEYCODE_PAGE_UP,
//						event.getRepeatCount(), event.getMetaState(),
//						event.getDeviceId(), event.getScanCode(), event.getFlags());
//
//				super.onKeyDown(KeyEvent.KEYCODE_PAGE_UP, e);
//			} break;
//
//			/**
//			 * 将“频道-”键映射为“下翻页”键
//			 */
//			case KeyEvent.KEYCODE_CHANNEL_DOWN: {
//				KeyEvent e = new KeyEvent(event.getDownTime(), event.getEventTime(),
//						event.getAction(), KeyEvent.KEYCODE_PAGE_DOWN,
//						event.getRepeatCount(), event.getMetaState(),
//						event.getDeviceId(), event.getScanCode(), event.getFlags());
//
//				super.onKeyDown(KeyEvent.KEYCODE_PAGE_DOWN, e);
//			} break;
//
//			default:break;
//		}
//
//		return super.onKeyDown(keyCode, event);
//	}

}
