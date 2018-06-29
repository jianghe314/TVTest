package ybolo.szrearch.com.tvtest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;

import java.io.IOException;
import java.util.List;

/**
 * 最后添加硬件解码功能,如果出现异常，三个视频都要暂停实现同步
 */

public class YboloMulitPlayer extends RelativeLayout implements View.OnFocusChangeListener,View.OnClickListener{

    private Context context;
    private SurfaceView surfaceView1,surfaceView2,surfaceView3;
    private SurfaceHolder surfaceHolder1,surfaceHolder2,surfaceHolder3;
    private RelativeLayout relativeLayout;
    private KSYMediaPlayer ksyMediaPlayer1,ksyMediaPlayer2,ksyMediaPlayer3;
    private MCallBack1 mCallBack1;
    private MCallBack2 mCallBack2;
    private MCallBack3 mCallBack3;
    private ProgressBar progressBar;

    //判别是否进行大屏切换
    private boolean isBig;

    public YboloMulitPlayer(Context context) {
        this(context,null);
        this.context=context;
    }

    public YboloMulitPlayer(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        this.context=context;
    }

    public YboloMulitPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        View view= LayoutInflater.from(context).inflate(R.layout.ybolo_mulit_player_layout,null,false);
        progressBar=view.findViewById(R.id.progress_bar);
        relativeLayout=view.findViewById(R.id.rela_layout);
        surfaceView1=view.findViewById(R.id.surface_view1);
        surfaceView2=view.findViewById(R.id.surface_view2);
        surfaceView3=view.findViewById(R.id.surface_view3);
        surfaceView2.setOnClickListener(this);
        surfaceView3.setOnClickListener(this);
        //设置标志，用于判断大小屏的切换
        surfaceView2.setTag(false);
        surfaceView3.setTag(false);
        addView(view);
    }


    public void setDataSources(List<String> sources){
        if(sources.size()>0){
            if(sources.size()==1){
                //单屏显示
                relativeLayout.setVisibility(GONE);
                surfaceHolder1=surfaceView1.getHolder();
                ksyMediaPlayer1=new KSYMediaPlayer.Builder(context).build();
                mCallBack1=new MCallBack1();

                ksyMediaPlayer1.setOnPreparedListener(onPreparedListener);
                ksyMediaPlayer1.setOnInfoListener(onInfoListener);
                ksyMediaPlayer1.setOnCompletionListener(onCompletionListener);
                ksyMediaPlayer1.setOnErrorListener(onErrorListener);
                ksyMediaPlayer1.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);

                surfaceHolder1.addCallback(mCallBack1);
                mCallBack1.setKSYMediaPlayer(ksyMediaPlayer1);

                //设置资源
                try {
                    ksyMediaPlayer1.setDataSource(sources.get(0));
                    ksyMediaPlayer1.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(sources.size()==2){
                //双屏显示
                surfaceView3.setVisibility(GONE);
                surfaceHolder1=surfaceView1.getHolder();
                surfaceHolder2=surfaceView2.getHolder();
                ksyMediaPlayer1=new KSYMediaPlayer.Builder(context).build();
                ksyMediaPlayer2=new KSYMediaPlayer.Builder(context).build();
                mCallBack1=new MCallBack1();
                mCallBack2=new MCallBack2();

                ksyMediaPlayer1.setOnPreparedListener(onPreparedListener);
                ksyMediaPlayer1.setOnInfoListener(onInfoListener);
                ksyMediaPlayer1.setOnCompletionListener(onCompletionListener);
                ksyMediaPlayer1.setOnErrorListener(onErrorListener);
                ksyMediaPlayer1.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);

                ksyMediaPlayer2.setOnPreparedListener(onPreparedListener);
                ksyMediaPlayer2.setOnInfoListener(onInfoListener);
                ksyMediaPlayer2.setOnCompletionListener(onCompletionListener);
                ksyMediaPlayer2.setOnErrorListener(onErrorListener);
                ksyMediaPlayer2.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);

                surfaceHolder1.addCallback(mCallBack1);
                surfaceHolder2.addCallback(mCallBack2);
                mCallBack1.setKSYMediaPlayer(ksyMediaPlayer1);
                mCallBack2.setKSYMediaPlayer(ksyMediaPlayer2);

                //设置资源
                try {
                    ksyMediaPlayer1.setDataSource(sources.get(0));
                    ksyMediaPlayer1.prepareAsync();
                    ksyMediaPlayer2.setDataSource(sources.get(1));
                    ksyMediaPlayer2.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else if(sources.size()==3){
                //多屏显示
                surfaceHolder1=surfaceView1.getHolder();
                surfaceHolder2=surfaceView2.getHolder();
                surfaceHolder3=surfaceView3.getHolder();

                ksyMediaPlayer1=new KSYMediaPlayer.Builder(context).build();
                ksyMediaPlayer2=new KSYMediaPlayer.Builder(context).build();
                ksyMediaPlayer3=new KSYMediaPlayer.Builder(context).build();
                mCallBack1=new MCallBack1();
                mCallBack2=new MCallBack2();
                mCallBack3=new MCallBack3();

                ksyMediaPlayer1.setOnPreparedListener(onPreparedListener);
                ksyMediaPlayer1.setOnInfoListener(onInfoListener);
                ksyMediaPlayer1.setOnCompletionListener(onCompletionListener);
                ksyMediaPlayer1.setOnErrorListener(onErrorListener);
                ksyMediaPlayer1.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);

                ksyMediaPlayer2.setOnPreparedListener(onPreparedListener);
                ksyMediaPlayer2.setOnInfoListener(onInfoListener);
                ksyMediaPlayer2.setOnCompletionListener(onCompletionListener);
                ksyMediaPlayer2.setOnErrorListener(onErrorListener);
                ksyMediaPlayer2.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);

                ksyMediaPlayer3.setOnPreparedListener(onPreparedListener);
                ksyMediaPlayer3.setOnInfoListener(onInfoListener);
                ksyMediaPlayer3.setOnCompletionListener(onCompletionListener);
                ksyMediaPlayer3.setOnErrorListener(onErrorListener);
                ksyMediaPlayer3.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);

                surfaceHolder1.addCallback(mCallBack1);
                surfaceHolder2.addCallback(mCallBack2);
                surfaceHolder3.addCallback(mCallBack3);
                mCallBack1.setKSYMediaPlayer(ksyMediaPlayer1);
                mCallBack2.setKSYMediaPlayer(ksyMediaPlayer2);
                mCallBack3.setKSYMediaPlayer(ksyMediaPlayer3);
                //设置资源
                try {
                    ksyMediaPlayer1.setDataSource(sources.get(0));
                    ksyMediaPlayer2.setDataSource(sources.get(1));
                    ksyMediaPlayer3.setDataSource(sources.get(2));

                    ksyMediaPlayer1.prepareAsync();
                    ksyMediaPlayer2.prepareAsync();
                    ksyMediaPlayer3.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            v.setBackgroundResource(R.drawable.move_foacus);
        }else {
            v.setBackgroundResource(0);
        }
    }

    /**
     * 卡顿信息监听，出现某一个视频出现卡顿的时候其他视频也要暂停等待，实现同步播放
     */

    private IMediaPlayer.OnInfoListener onInfoListener=new IMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer mp, int what, int extra) {
            switch (what){
                //开始缓存数据
                case KSYMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    progressBar.setVisibility(VISIBLE);
                    //视频暂停
                    ksyMediaPlayer1.pause();
                    ksyMediaPlayer2.pause();
                    ksyMediaPlayer3.pause();

                    break;
                //数据缓存完毕
                case KSYMediaPlayer.MEDIA_INFO_BUFFERING_END:
                    progressBar.setVisibility(GONE);
                    ksyMediaPlayer1.start();
                    ksyMediaPlayer2.start();
                    ksyMediaPlayer3.start();
                    break;
            }
            return false;
        }
    };

    private IMediaPlayer.OnPreparedListener onPreparedListener=new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer mp) {
            if(mp!=null){
                mp.start();
                progressBar.setVisibility(GONE);
            }
        }
    };

    private IMediaPlayer.OnErrorListener onErrorListener=new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer mp, int what, int extra) {
            switch (what){
                case KSYMediaPlayer.MEDIA_INFO_RELOADED:

                    break;
                case KSYMediaPlayer.MEDIA_ERROR_IO:
                    Toast.makeText(context,"链接超时,请重试",Toast.LENGTH_SHORT).show();
                    break;
                case KSYMediaPlayer.MEDIA_ERROR_UNKNOWN:
                    Toast.makeText(context,"无效的播放路径",Toast.LENGTH_SHORT).show();
                    break;
                case KSYMediaPlayer.MEDIA_ERROR_SERVER_DIED:
                    Toast.makeText(context,"多媒体服务器出错，请重试",Toast.LENGTH_SHORT).show();
                    break;
                case KSYMediaPlayer.MEDIA_ERROR_INVALID_DATA:
                    Toast.makeText(context,"无效的媒体数据",Toast.LENGTH_SHORT).show();
                    break;
                case KSYMediaPlayer.MEDIA_ERROR_TIMED_OUT:
                    Toast.makeText(context,"操作超时，请重试",Toast.LENGTH_SHORT).show();
                    break;
                case KSYMediaPlayer.MEDIA_ERROR_DNS_PARSE_FAILED:
                    Toast.makeText(context,"DNS解析失败，请检查网络",Toast.LENGTH_SHORT).show();
                    break;
                case KSYMediaPlayer.MEDIA_ERROR_CONNECT_SERVER_FAILED:
                    Toast.makeText(context,"连接服务器失败，请检查网络",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    private IMediaPlayer.OnCompletionListener onCompletionListener=new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer mp) {
            //如果一路播放完毕，则其他的多暂停
            /*ksyMediaPlayer1.stop();
            ksyMediaPlayer2.stop();
            ksyMediaPlayer3.stop();*/
            Toast.makeText(context.getApplicationContext(),"播放完毕",Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.surface_view2:
                if(!(boolean)surfaceView2.getTag()){
                    surfaceHolder1.removeCallback(mCallBack1);
                    surfaceHolder2.removeCallback(mCallBack2);
                    surfaceHolder1.addCallback(mCallBack2);
                    surfaceHolder2.addCallback(mCallBack1);
                    ksyMediaPlayer2.setDisplay(surfaceHolder1);
                    ksyMediaPlayer1.setDisplay(surfaceHolder2);
                    surfaceView2.setTag(true);
                }else {
                    surfaceHolder1.removeCallback(mCallBack2);
                    surfaceHolder2.removeCallback(mCallBack1);
                    surfaceHolder1.addCallback(mCallBack1);
                    surfaceHolder2.addCallback(mCallBack2);
                    ksyMediaPlayer2.setDisplay(surfaceHolder2);
                    ksyMediaPlayer1.setDisplay(surfaceHolder1);
                    surfaceView2.setTag(false);
                }
                break;
            case R.id.surface_view3:
                if(!(boolean)surfaceView3.getTag()){
                    surfaceHolder1.removeCallback(mCallBack1);
                    surfaceHolder3.removeCallback(mCallBack3);
                    surfaceHolder1.addCallback(mCallBack3);
                    surfaceHolder3.addCallback(mCallBack1);
                    ksyMediaPlayer3.setDisplay(surfaceHolder1);
                    ksyMediaPlayer1.setDisplay(surfaceHolder3);
                    surfaceView3.setTag(true);
                }else {
                    surfaceHolder1.removeCallback(mCallBack3);
                    surfaceHolder3.removeCallback(mCallBack1);
                    surfaceHolder1.addCallback(mCallBack1);
                    surfaceHolder3.addCallback(mCallBack3);
                    ksyMediaPlayer3.setDisplay(surfaceHolder3);
                    ksyMediaPlayer1.setDisplay(surfaceHolder1);
                    surfaceView3.setTag(false);
                }
                break;
        }
    }

    private class MCallBack1 implements SurfaceHolder.Callback{

        private KSYMediaPlayer ksyMediaPlayer;

        public void setKSYMediaPlayer(KSYMediaPlayer ksyMediaPlayer) {
            this.ksyMediaPlayer = ksyMediaPlayer;
        }

       @Override
       public void surfaceCreated(SurfaceHolder holder) {

       }

       @Override
       public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
           if(ksyMediaPlayer!= null) {
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
   }

    private class MCallBack2 implements SurfaceHolder.Callback{

        private KSYMediaPlayer ksyMediaPlayer;

        public void setKSYMediaPlayer(KSYMediaPlayer ksyMediaPlayer) {
            this.ksyMediaPlayer = ksyMediaPlayer;
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if(ksyMediaPlayer!= null) {
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
    }


    private class MCallBack3 implements SurfaceHolder.Callback{

        private KSYMediaPlayer ksyMediaPlayer;

        public void setKSYMediaPlayer(KSYMediaPlayer ksyMediaPlayer) {
            this.ksyMediaPlayer = ksyMediaPlayer;
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if(ksyMediaPlayer!= null) {
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
    }



    public void onPause(){
        ksyMediaPlayer1.pause();
        ksyMediaPlayer2.pause();
        ksyMediaPlayer3.pause();
    }

    public void onDestory(){
        ksyMediaPlayer1.stop();
        ksyMediaPlayer2.stop();
        ksyMediaPlayer3.stop();
        ksyMediaPlayer1.release();
        ksyMediaPlayer2.release();
        ksyMediaPlayer3.release();
    }
}
