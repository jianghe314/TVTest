package ybolo.szrearch.com.tvtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private YboloTvPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        player=findViewById(R.id.ybolo_tv_player);
        player.setDataSources("http://vod.ybolo.com/live/10001-026-L180517-2007.m3u8");
    }


    @Override
    protected void onDestroy() {
        player.OnDestroy();
        super.onDestroy();
    }
}
