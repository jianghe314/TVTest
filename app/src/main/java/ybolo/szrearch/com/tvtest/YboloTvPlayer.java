package ybolo.szrearch.com.tvtest;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYHardwareDecodeWhiteList;
import com.ksyun.media.player.KSYMediaPlayer;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static com.ksyun.media.player.KSYHardwareDecodeWhiteList.KSY_STATUS_OK;


public class YboloTvPlayer extends FrameLayout implements View.OnFocusChangeListener,View.OnKeyListener,View.OnClickListener{

    private Context context;

    private RelativeLayout control_layout;
    private SurfaceView surfaceView;
    private ProgressBar progressBar;
    private ImageView play;
    private TextView loadTime,totalTime;
    private mSeekBar seekBar;

    private KSYMediaPlayer ksyMediaPlayer;
    private SurfaceHolder surfaceHolder;
    private Timer timer;
    private long duration;

    private Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    //更新视频播放时间
                    long tt=ksyMediaPlayer.getCurrentPosition();
                    Log.e("location","-->"+tt);
                    if(tt>0){
                        loadTime.setText(ComputerTime(tt));
                        float progress=((float)tt/(float) duration)*100f;
                        Log.e("progress","-->"+progress);
                        seekBar.setProgress((int)progress);
                    }
                    break;
                case 2:
                    //隐藏媒体工具栏
                    //control_layout.setVisibility(GONE);
                    break;
            }
        }
    };


    public YboloTvPlayer(@NonNull Context context) {
        this(context,null);
        this.context=context;
    }

    public YboloTvPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
        this.context=context;
    }

    public YboloTvPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view= LayoutInflater.from(context).inflate(R.layout.ybolo_layout1,null,false);
        surfaceView=view.findViewById(R.id.surface_view);
        progressBar=view.findViewById(R.id.progress_bar);
        play=view.findViewById(R.id.img_control);
        //视频默认自动播放
        play.setTag(true);
        loadTime=view.findViewById(R.id.ybolo_tv_loadTime);
        totalTime=view.findViewById(R.id.ybolo_player_totleTime);
        seekBar=view.findViewById(R.id.seek_bar);
        control_layout=view.findViewById(R.id.control_layout);
        //默认不能滑动
        seekBar.setTag(false);

        play.setOnFocusChangeListener(this);
        play.setOnKeyListener(this);
        play.setOnClickListener(this);
        seekBar.setOnFocusChangeListener(this);
        seekBar.setOnKeyListener(this);
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);

        ksyMediaPlayer=new KSYMediaPlayer.Builder(context).build();
        ksyMediaPlayer.setOnErrorListener(onErrorListener);
        ksyMediaPlayer.setOnCompletionListener(onCompletionListener);
        ksyMediaPlayer.setOnInfoListener(onInfoListener);
        ksyMediaPlayer.setOnPreparedListener(onPreparedListener);
        surfaceView.setOnClickListener(this);
        surfaceView.setOnKeyListener(this);

        surfaceHolder=surfaceView.getHolder();
        surfaceHolder.addCallback(callback);
        timer=new Timer();

        addView(view);
        handler.sendEmptyMessageDelayed(2,8*1000);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            v.setBackgroundResource(R.drawable.move_foacus);
        }else {
            v.setBackgroundResource(0);
        }
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        switch (v.getId()){
            case R.id.img_control:
                Toast.makeText(context.getApplicationContext(),"播放/暂停",Toast.LENGTH_SHORT).show();
                if((keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if((boolean)play.getTag()){
                        play.setImageResource(R.drawable.start_icon);
                        play.setTag(false);
                    }else {
                        play.setImageResource(R.drawable.stop_icon);
                        play.setTag(true);
                    }

                }
                break;
            case R.id.seek_bar:
                //当点击确认之后，才可以滑动seekbar,再点击确认，完成滑动调整
                if((keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if((boolean)seekBar.getTag()){
                        seekBar.onKeyDown(keyCode,event);
                    }else {
                        seekBar.setTag(true);
                    }
                }
                break;
            case R.id.surface_view:
                //监听surfaceview的 key事件，是否显示工具栏
                Toast.makeText(context.getApplicationContext(),"显示/隐藏",Toast.LENGTH_SHORT).show();
                if((keyCode == KeyEvent.KEYCODE_MENU)||(keyCode == KeyEvent.KEYCODE_TV_CONTENTS_MENU)||(keyCode == KeyEvent.KEYCODE_TV_MEDIA_CONTEXT_MENU)){
                    if(control_layout.isShown()){
                        handler.removeMessages(2);
                        control_layout.setVisibility(GONE);
                        handler.sendEmptyMessageDelayed(2,8*1000);
                    }else {
                        handler.removeMessages(2);
                        control_layout.setVisibility(VISIBLE);
                        handler.sendEmptyMessageDelayed(2,8*1000);
                    }
                }
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_control:
                if((boolean)play.getTag()){
                    play.setImageResource(R.drawable.start_icon);
                    play.setTag(false);
                    ksyMediaPlayer.pause();
                }else {
                    play.setImageResource(R.drawable.stop_icon);
                    play.setTag(true);
                    ksyMediaPlayer.start();
                }
                break;
            case R.id.surface_view:
                if(control_layout.isShown()){
                    handler.removeMessages(2);
                    control_layout.setVisibility(GONE);
                    control_layout.setTag(true);
                    handler.sendEmptyMessageDelayed(2,8*1000);
                }else {
                    handler.removeMessages(2);
                    control_layout.setVisibility(VISIBLE);
                    control_layout.setTag(false);
                    handler.sendEmptyMessageDelayed(2,8*1000);
                }
                break;
        }
    }

    /**
     * 设置播放源
     */
    public void setDataSources(String source){
        if(KSYHardwareDecodeWhiteList.getInstance().getCurrentStatus()==KSY_STATUS_OK){
            if(KSYHardwareDecodeWhiteList.getInstance().supportHardwareDecodeH264()){
                ksyMediaPlayer.setDecodeMode(KSYMediaPlayer.KSYDecodeMode.KSY_DECODE_MODE_AUTO);
            }

            if(KSYHardwareDecodeWhiteList.getInstance().supportHardwareDecodeH265()){
                ksyMediaPlayer.setDecodeMode(KSYMediaPlayer.KSYDecodeMode.KSY_DECODE_MODE_AUTO);
            }
        }
        try {
            ksyMediaPlayer.setDataSource(source);
            ksyMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //实在视频缓存最长时间
    public void setMaxBufferTimeSize(float timeSize){
        //设置播放器缓存最长时间，默认2秒
        ksyMediaPlayer.setBufferTimeMax(timeSize);
    }

    public void setTimeOut(int prepareTimeOut,int readTimeOut){
        //设置视频读取和准备超时时间
        ksyMediaPlayer.setTimeout(prepareTimeOut,readTimeOut);
    }

    public void setBufferSize(int size){
        //设置缓存大小，单位M，默认15M
        ksyMediaPlayer.setBufferSize(size);
    }


    //设置硬解码方法
    public void setDecodeMode(){
        ksyMediaPlayer.setDecodeMode(KSYMediaPlayer.KSYDecodeMode.KSY_DECODE_MODE_AUTO);
    }

    /**
     * 更新视频时间
     */
    private void showTime(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        },0,1000);
    }




    private SurfaceHolder.Callback callback=new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if(ksyMediaPlayer != null) {
                ksyMediaPlayer.setDisplay(holder);
                ksyMediaPlayer.setScreenOnWhilePlaying(true);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if(ksyMediaPlayer != null) {
                ksyMediaPlayer.setDisplay(null);
                ksyMediaPlayer.setScreenOnWhilePlaying(false);
            }
        }
    };


    //播放器信息监听
    private IMediaPlayer.OnPreparedListener onPreparedListener=new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer mp) {
            if(ksyMediaPlayer!=null){
                duration=ksyMediaPlayer.getDuration();
                Log.e("duration","-->"+duration);
                totalTime.setText(ComputerTime(duration));
                ksyMediaPlayer.start();
                progressBar.setVisibility(GONE);
                showTime();

            }
        }
    };

    private IMediaPlayer.OnCompletionListener onCompletionListener=new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer mp) {
            seekBar.setProgress(100);
            //Toast.makeText(context.getApplicationContext(),"播放完毕",Toast.LENGTH_SHORT).show();
        }
    };

    private IMediaPlayer.OnInfoListener onInfoListener=new IMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer mp, int what, int extra) {
            switch (what){
                //开始缓存数据
                case KSYMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    progressBar.setVisibility(VISIBLE);
                    break;
                //数据缓存完毕
                case KSYMediaPlayer.MEDIA_INFO_BUFFERING_END:
                    progressBar.setVisibility(GONE);
                    break;
            }
            return false;
        }
    };

    private IMediaPlayer.OnErrorListener onErrorListener=new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer mp, int what, int extra) {
            switch (what){
                case KSYMediaPlayer.MEDIA_INFO_RELOADED:

                    break;
                case KSYMediaPlayer.MEDIA_ERROR_IO:
                    Toast.makeText(context.getApplicationContext(),"链接超时,请重试",Toast.LENGTH_SHORT).show();
                    break;
                case KSYMediaPlayer.MEDIA_ERROR_UNKNOWN:
                    Toast.makeText(context.getApplicationContext(),"无效的播放路径",Toast.LENGTH_SHORT).show();
                    break;
                case KSYMediaPlayer.MEDIA_ERROR_SERVER_DIED:
                    Toast.makeText(context.getApplicationContext(),"多媒体服务器出错，请重试",Toast.LENGTH_SHORT).show();
                    break;
                case KSYMediaPlayer.MEDIA_ERROR_INVALID_DATA:
                    Toast.makeText(context.getApplicationContext(),"无效的媒体数据",Toast.LENGTH_SHORT).show();
                    break;
                case KSYMediaPlayer.MEDIA_ERROR_TIMED_OUT:
                    Toast.makeText(context.getApplicationContext(),"操作超时，请重试",Toast.LENGTH_SHORT).show();
                    break;
                case KSYMediaPlayer.MEDIA_ERROR_DNS_PARSE_FAILED:
                    Toast.makeText(context.getApplicationContext(),"DNS解析失败，请检查网络",Toast.LENGTH_SHORT).show();
                    break;
                case KSYMediaPlayer.MEDIA_ERROR_CONNECT_SERVER_FAILED:
                    Toast.makeText(context.getApplicationContext(),"连接服务器失败，请检查网络",Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    };

    /**
     * seekbar位置监听
     */
    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener=new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                float seek_progress=progress/100f;
                long set_progress= (long) (seek_progress*duration);
                Log.e("set_progress","-->"+set_progress);
                ksyMediaPlayer.seekTo(set_progress,true);
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            handler.removeMessages(2);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            handler.removeMessages(2);
            handler.sendEmptyMessageDelayed(2,8*1000);
        }
    };


    private String ComputerTime(long time){
        int seconds= (int) (time/1000);
        int temp=0;
        StringBuffer sb=new StringBuffer();
        temp = seconds/3600;
        sb.append((temp<10)?"0"+temp+":":""+temp+":");

        temp=seconds%3600/60;
        sb.append((temp<10)?"0"+temp+":":""+temp+":");

        temp=seconds%3600%60;
        sb.append((temp<10)?"0"+temp:""+temp);
        return sb.toString();
    }


    public void OnPause(){
        if(null!=ksyMediaPlayer){
            ksyMediaPlayer.pause();
        }
    }


    public void OnStop(){
        if(null!=ksyMediaPlayer){
            timer.purge();
            timer.cancel();
            ksyMediaPlayer.stop();
        }
    }


    public void OnDestroy(){
        if(null!=ksyMediaPlayer){
            handler.removeMessages(1);
            handler.removeMessages(2);
            timer.purge();
            timer.cancel();
            ksyMediaPlayer.stop();
            ksyMediaPlayer.release();
            ksyMediaPlayer=null;
        }
    }

}
