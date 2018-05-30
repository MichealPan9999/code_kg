package cn.ktc.android.oobe.widget;

import android.app.Instrumentation;
import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.IWindowManager;
import android.view.KeyEvent;
import android.widget.EditText;

public class InputIPAddress extends EditText {

	public static boolean isForwardRightWithTextChange = true;

	public InputIPAddress(Context context) {
		super(context);
		setSelectAllOnFocus(true);
	}

	public InputIPAddress(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setSelectAllOnFocus(true);
	}

	public InputIPAddress(Context context, AttributeSet attrs) {
		super(context, attrs);
		setSelectAllOnFocus(true);
	}

	@Override
	protected void onTextChanged(CharSequence text, int start, int before,
			int after) {
		super.onTextChanged(text, start, before, after);

		String input = text.toString().trim();
		// //[2012-1-14 ]由于输入后没有删除功能，所以当初人超出ip范围是时，将文本设置为最后一次输入的值。
		if (0 != input.length()) {

			int cursorLoc = getSelectionStart();// /获取光标位置。
			int index = cursorLoc > 0 ? (cursorLoc - 1) : 0;
			char lastChar = input.charAt(index);

			if (input.length() <= 3) {
				int ip = Integer.parseInt(input);
				if (ip >= 256) {
					setText(String.valueOf(lastChar));
					setSelection(length());
				} else if (input.length() == 3) {
					if (isForwardRightWithTextChange)
						sendKeyEvent(KeyEvent.KEYCODE_DPAD_RIGHT);
				}
			}
			// else {
			// setText(String.valueOf(lastChar));
			// setSelection(length());
			// }
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			if (this.hasSelection()) {
				this.setCursorVisible(false);
				sendKeyEvent(KeyEvent.KEYCODE_DPAD_LEFT);
			}
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			if (this.hasSelection()) {
				this.setCursorVisible(false);
				sendKeyEvent(KeyEvent.KEYCODE_DPAD_RIGHT);
			}
		} else if (keyCode == KeyEvent.KEYCODE_ENTER) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 *
	 * <p>
	 * Title: sendKeyEvent.
	 * </p>
	 * <p>
	 * Description: 当ip地址输入的长度为3个字符，并且值小于256时，则光标自动右移.
	 * </p>
	 *
	 * @param keycode
	 */
	private void sendKeyEvent(int keycode) {
		try {          
            Instrumentation inst=new Instrumentation();
            inst.sendKeyDownUpSync(keycode);
        } catch (Exception e) {
            // TODO: handle exception
        	e.printStackTrace();
        }
//		long now = SystemClock.uptimeMillis();
//		try {
//			KeyEvent downEvent = new KeyEvent(now, now, KeyEvent.ACTION_DOWN,
//					keycode, 0);
//			KeyEvent upEvent = new KeyEvent(now, now, KeyEvent.ACTION_UP,
//					keycode, 0);
//
//			(IWindowManager.Stub.asInterface(ServiceManager
//					.getService(Context.WINDOW_SERVICE))).injectInputEventNoWait(downEvent);
//			(IWindowManager.Stub.asInterface(ServiceManager
//					.getService(Context.WINDOW_SERVICE))).injectInputEventNoWait(upEvent);
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
	}
}
