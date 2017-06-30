package com.guaju.videodemo;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener
{

    private VideoView vv;
    private Button start,stop,pause;
    private File file;
    private PlayStatus playing_status;
    private SeekBar seekBar;
    private int duration=0;
    private int video_progress=0;
    private Runnable runnable;
    private TimerTask task;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initVideoListener();
        file = new File(Environment.getExternalStorageDirectory() + "/gongyu.mp4");
        if (file.exists()){
        vv.setVideoPath(file.getAbsolutePath());
            duration = vv.getDuration();
            vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });


        }else{
            Toast.makeText(this, "该视频不存在", Toast.LENGTH_SHORT).show();
        }
    }

    private void initVideoListener() {
        runnable = new Runnable() {
            @Override
            public void run() {
                video_progress=vv.getCurrentPosition();
                if (duration!=0){
                seekBar.setProgress(video_progress);
                }
            }
        };
        task = new TimerTask() {
            @Override
            public void run() {
                video_progress=vv.getCurrentPosition();
                if (duration!=0){
                    seekBar.setProgress(video_progress);
                }
            }
        };

    }

    private void initView() {
        vv = (VideoView) findViewById(R.id.videoview);
        start = (Button) findViewById(R.id.bt_start);
        stop = (Button) findViewById(R.id.bt_stop);
        pause = (Button) findViewById(R.id.bt_pause);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        initEvent();
    }

    private void initEvent() {
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        pause.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                video_progress = vv.getCurrentPosition();
                //
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_start:
                duration=vv.getDuration();
                seekBar.setMax(duration);
                Toast.makeText(this, "duration="+duration, Toast.LENGTH_SHORT).show();
                if ((vv!=null)&&(file.exists())&&(PlayStatus.START!=playing_status)){
                    if (playing_status==PlayStatus.STOP){
                    vv.seekTo(0);
                    }
                    vv.start();
                    timer = new Timer();
                    timer.schedule(task,0,100);
                    playing_status=PlayStatus.START;
                }
                break;
            case R.id.bt_stop:
                if ((vv!=null)&&file.exists()&&vv.canPause()){
                    timer.cancel();
                    seekBar.setProgress(0);
                    vv.pause();
                    playing_status=PlayStatus.STOP;


                }
                break;
            case R.id.bt_pause:
                if ((vv!=null)&&file.exists()&&vv.canPause()){
                    timer.cancel();
                    vv.pause();
                    playing_status=PlayStatus.PAUSE;
                }
                break;
        }
    }

    public enum PlayStatus{
        STOP,PAUSE,START;
    }

}
