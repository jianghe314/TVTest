package ybolo.szrearch.com.tvtest;

import android.content.Context;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class mSeekBar extends AppCompatSeekBar {

    private boolean hasMove=false;

    public mSeekBar(Context context) {
        super(context);
    }

    public mSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //获取焦点后监听OK或者enter事件，然后才能滑动seekbar,滑动完成点击enter或者ok确认滑动移除滑动响应
        //return super.onKeyDown(keyCode, event);
        switch (keyCode){
            case KeyEvent.KEYCODE_ENTER:
                if(!hasMove){
                    hasMove=true;
                    super.onKeyDown(keyCode, event);
                }else {
                    hasMove=false;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                super.onKeyDown(keyCode, event);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if(hasMove){
                    super.onKeyDown(keyCode, event);
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if(hasMove){
                    super.onKeyDown(keyCode, event);
                }
                break;
            case KeyEvent.KEYCODE_MINUS:
                super.onKeyDown(keyCode, event);
                break;
            case KeyEvent.KEYCODE_PLUS:
                super.onKeyDown(keyCode, event);
                break;
        }
        return hasMove;
    }


}
