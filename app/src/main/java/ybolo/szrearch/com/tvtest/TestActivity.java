package ybolo.szrearch.com.tvtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class TestActivity extends AppCompatActivity {

    private ImageView control;
    private mSeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
    }

    private void initView() {
        control=findViewById(R.id.play_control);
        seekBar=findViewById(R.id.play_seekbar);
    }
}
