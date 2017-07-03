package com.guaju.videodemo;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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
    private FrameLayout left,right;
    float oldLeftY=0;
    float oldRightY=0;
    int dp=50;


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

    }

    private void initView() {
        vv = (VideoView) findViewById(R.id.videoview);
        start = (Button) findViewById(R.id.bt_start);
        stop = (Button) findViewById(R.id.bt_stop);
        pause = (Button) findViewById(R.id.bt_pause);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        left= (FrameLayout) findViewById(R.id.left);
        right= (FrameLayout) findViewById(R.id.right);

        initEvent();
    }

    private void initEvent() {
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        pause.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    vv.seekTo(progress);
                    startVideo(progress);
                }


                //
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                if (oldLeftY==0){
                    oldLeftY=event.getRawY();
                }
                }
                if (event.getAction()==MotionEvent.ACTION_MOVE){
                    float newLeftY=event.getRawY();
                    float degree = newLeftY - oldLeftY;
                    float abs = Math.abs(degree);
                    if (newLeftY - oldLeftY>0){
                         ToastUtils.show(MainActivity.this,"downdown");
                         if (abs>=4*dp){
                             //四个
                             ToastUtils.show(MainActivity.this,"sige");
                         }else if (abs>=3*dp){
                             ToastUtils.show(MainActivity.this,"sange");

                         }else if (abs>=2*dp){
                             ToastUtils.show(MainActivity.this,"liangge");
                         }
                         else if (abs>=1*dp){
                             ToastUtils.show(MainActivity.this,"yige");;

                         }
                         else{
                             Toast.makeText(MainActivity.this, "cancle", Toast.LENGTH_SHORT).show();
                         }


                     }else{
                         ToastUtils.show(MainActivity.this,"upup");
                        if (abs>=4*dp){
                            //四个
                        }else if (abs>=3*dp){

                        }else if (abs>=2*dp){

                        }
                        else if (abs>=1*dp){

                        }
                        else{

                        }

                    }
                }
                if(event.getAction()==MotionEvent.ACTION_UP){
                    oldLeftY=0;
                }


                return true;
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
                startVideo(0);
                seekBar.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_stop:
                if ((vv!=null)&&file.exists()&&vv.canPause()){
                    if(timer!=null){
                    timer.purge();
                    timer=null;
                    }
                    seekBar.setProgress(0);
                    vv.pause();
                    playing_status=PlayStatus.STOP;


                }
                break;
            case R.id.bt_pause:
                if ((vv!=null)&&file.exists()&&vv.canPause()){
                    if(timer!=null){
                    timer.purge();
                    timer=null;
                    }
                    vv.pause();
                    playing_status=PlayStatus.PAUSE;
                }
                break;
        }
    }

    private void startVideo(int position) {
        if ((vv!=null)&&(file.exists())&&(PlayStatus.START!=playing_status)){
            if (playing_status==PlayStatus.STOP){
                vv.seekTo(0);
            }
            if(position==0){
            vv.start();
            }else{
                vv.seekTo(position);
                vv.start();
            }


            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    video_progress=vv.getCurrentPosition();
                    if (duration!=0){
                        seekBar.setProgress(video_progress);
                    }
                }
            },0,100);
            playing_status=PlayStatus.START;
        }
    }

    public enum PlayStatus{
        STOP,PAUSE,START;
    }

}
