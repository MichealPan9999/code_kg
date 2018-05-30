package cn.ktc.android.oobe.utils;

import android.app.Instrumentation;
import android.content.DialogInterface;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import com.mstar.android.MKeyEvent;

/**
 * Created by hjy on 2017/10/11.
 */

public class DialogOnKeyListener {
    public static DialogInterface.OnKeyListener mOnKeyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            Log.e("oobe", "DialogOnKeyListener onKey: " + String.valueOf(keyCode));
            if (event.getAction() == KeyEvent.ACTION_UP) {
                return false;
            }
//            String deviceName = InputDevice.getDevice(event.getDeviceId()).getName();
//            if (!deviceName.equals("MStar Smart TV Keypad"))
//                return false;

            switch (keyCode) {
                case KeyEvent.KEYCODE_PROG_RED:
                case KeyEvent.KEYCODE_PROG_GREEN:
                case KeyEvent.KEYCODE_PROG_YELLOW:
                case 294:
                case MKeyEvent.KEYCODE_EPG:
                case MKeyEvent.KEYCODE_TTX:
                    return true;

                case KeyEvent.KEYCODE_CHANNEL_UP:
                //    keyInjection(KeyEvent.KEYCODE_DPAD_UP);
                    return true;
                case KeyEvent.KEYCODE_CHANNEL_DOWN:
                 //   keyInjection(KeyEvent.KEYCODE_DPAD_DOWN);
                    return true;
                case KeyEvent.KEYCODE_VOLUME_UP:
                  //  keyInjection(KeyEvent.KEYCODE_DPAD_RIGHT);
                    return true;
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                  //  keyInjection(KeyEvent.KEYCODE_DPAD_LEFT);
                    return true;
                case KeyEvent.KEYCODE_TV_INPUT:
                  //  keyInjection(KeyEvent.KEYCODE_DPAD_CENTER);
                    return true;
                case KeyEvent.KEYCODE_MENU:
                 //   keyInjection(KeyEvent.KEYCODE_BACK);
                    return true;
                //case MKeyEvent.KEYCODE_FREEZE:
                //case MKeyEvent.KEYCODE_MTS:  //benq, zjd20150317
                //case MKeyEvent.KEYCODE_SOUND_MODE:   //benq, zjd20150317
                //case MKeyEvent.KEYCODE_PICTURE_MODE: //benq, zjd20150317

                default:
                    break;
            }

            return false;
        }
    };

    private static void keyInjection(final int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP
                || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT
                || keyCode == KeyEvent.KEYCODE_ENTER
                || KeyEvent.KEYCODE_DPAD_CENTER == keyCode
                || KeyEvent.KEYCODE_BACK == keyCode) {
            new Thread() {
                public void run() {
                    try {
                        Instrumentation inst = new Instrumentation();
                        inst.sendKeyDownUpSync(keyCode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

}
