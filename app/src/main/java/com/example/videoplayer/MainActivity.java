package com.example.videoplayer;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private  VideoView videoView;
    Button gallery;
    private static final int PICK_Video=45;
    private static final int PICK_Audio=4;
    private final int VIDEO_REQUEST_CODE= 101;
    int stopPosition=0;


    private  Uri videoUri = null;
    private  Uri audioUri = null;
    Button play;
    TextView pah;
    Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView = findViewById(R.id.videoView);
        play=findViewById(R.id.btnplay);




        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.setVideoURI(videoUri);
                videoView.start();
            }
        });
        findViewById(R.id.btnpause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPosition = videoView.getCurrentPosition(); //stopPosition is an int
                videoView.pause();

            }
        });

        findViewById(R.id.btneremuse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.seekTo(stopPosition);
                videoView.start(); //Or use resume() if it doesn't work. I'm not sure
            }
        });
        //btn audio like play,pause
        findViewById(R.id.btnPlayAudio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                MediaPlayer mediaPlayer = new MediaPlayer();


                mediaPlayer.setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                );
                try {
                    mediaPlayer.setDataSource(getApplicationContext(), audioUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(mediaPlayer.isPlaying()){

                    mediaPlayer.stop();
                }
                else {

                    try {
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                }

               }



        });



        //Audio
        findViewById(R.id.btnSelAudio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Audio"),PICK_Audio);
                audioUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
            }
        });

        //gallery
        gallery=findViewById(R.id.btn_gallery);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Video"),PICK_Video);
                videoUri = MediaStore.Video.Media.INTERNAL_CONTENT_URI;



            }
        });



    }


    public void captureVideo(View view) {

        Intent camIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (camIntent.resolveActivity(getPackageManager())!=null) {

            startActivityForResult(camIntent, VIDEO_REQUEST_CODE);
        }
        else {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEO_REQUEST_CODE && resultCode == RESULT_OK) {

            videoUri = data.getData();
            pah= findViewById(R.id.tvPath);
                String pth = data.getData().getPath();

               pah.setText(pth);
            Toast.makeText(getApplicationContext(), "video sucessfully recorded", Toast.LENGTH_LONG).show();

        }
        //code for audio
       else if(requestCode == PICK_Audio && resultCode == RESULT_OK){
            if(data.getData() != null){
                audioUri =data.getData();
            }
        }
        //code for gallery
      else   if (requestCode == PICK_Video && resultCode == RESULT_OK) {

            if (data.getData() != null) {


                videoUri = data.getData();
                pah= findViewById(R.id.tvPath);
                String pth = data.getData().getPath();

               pah.setText(pth);

            }
        } else {
            Toast.makeText(this, "faild ", Toast.LENGTH_SHORT).show();
        }

    }




}


