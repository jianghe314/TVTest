package ybolo.szrearch.com.tvtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MulitPlayerTest extends AppCompatActivity {

    private String path="http://vod.ybolo.com/live/10001-026-L180517-2007.m3u8";
    private String path2="http://video.ybolo.com/10001/1f453d2fe93e4d1ba07b86e8c4e54667/SD/resource/videos/1_1.mp4";
    private List<String> sources=new ArrayList<>();

    private YboloMulitPlayer mulitPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mulit_player_test);
        initView();
    }

    private void initView() {
        mulitPlayer=findViewById(R.id.mulit_player);
        sources.add(path);
        sources.add(path2);
        //sources.add(path);
        mulitPlayer.setDataSources(sources);
    }
}
