package com.example.myapp.firebasevideoplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    VideoView videoView;
    ProgressBar progressBarVideo;
    TextView textViewTotalTime;
    TextView textViewLeftTime;
    ProgressBar progressBarStream;
    ImageView playBtn,frwdBtn,revBtn;
    RecyclerView recyclerView;

    int duration;
    int current;

    public boolean isPlaying;
    private StorageReference mStorageRef;
    Uri videoUri;

    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds


    List<VideoDetailModel> videoUriList=new ArrayList<VideoDetailModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView=(VideoView)findViewById(R.id.videoview_video);
        //progressBarVideo=(ProgressBar)findViewById(R.id.progressbar_video);
        textViewTotalTime=(TextView) findViewById(R.id.textview_totaltime);
        //textViewLeftTime=(TextView)findViewById(R.id.textview_lefttime);

        progressBarStream=(ProgressBar)findViewById(R.id.progressbar_stream);
        playBtn=(ImageView)findViewById(R.id.imageview_playbtn);

        frwdBtn=(ImageView)findViewById(R.id.imageview_frwdbtn);
        revBtn=(ImageView)findViewById(R.id.imageview_revbtn);

        videoUriList.add(new VideoDetailModel("calm-blue-sea","https://firebasestorage.googleapis.com/v0/b/fir-videoplayer-db2cb.appspot.com/o/videos%2Fmixkit-multicolor-ink-swirls-in-water-286.mp4?alt=media&token=74d58aa4-92d7-46a7-879b-5bf06624f5ff"));
        videoUriList.add(new VideoDetailModel("mulitcolor-link","https://firebasestorage.googleapis.com/v0/b/fir-videoplayer-db2cb.appspot.com/o/videos%2Fmixkit-aerial-shot-of-calm-blue-sea-1080.mp4?alt=media&token=cc387fd3-e740-4f36-bc62-646b22ef629c"));
        videoUriList.add(new VideoDetailModel("smiley-face-breakfast","https://firebasestorage.googleapis.com/v0/b/fir-videoplayer-db2cb.appspot.com/o/videos%2Fmixkit-smiley-face-breakfast-2693.mp4?alt=media&token=b25924b5-4f68-43a1-8ec8-7b9fe4898a84"));
        videoUriList.add(new VideoDetailModel("flower-pot","https://firebasestorage.googleapis.com/v0/b/fir-videoplayer-db2cb.appspot.com/o/videos%2Fmixkit-watering-a-flower-pot-1780.mp4?alt=media&token=25816f16-f0bc-4a24-a7e9-01fc9c6c57fd"));
        videoUriList.add(new VideoDetailModel("stars-in-space","https://firebasestorage.googleapis.com/v0/b/fir-videoplayer-db2cb.appspot.com/o/videos%2Fmixkit-smiley-face-breakfast-2693.mp4?alt=media&token=b25924b5-4f68-43a1-8ec8-7b9fe4898a84"));



        recyclerView=(RecyclerView)findViewById(R.id.recylerview_videolist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        videoUri=Uri.parse("https://firebasestorage.googleapis.com/v0/b/fir-videoplayer-db2cb.appspot.com/o/videos%2Fmixkit-multicolor-ink-swirls-in-water-286.mp4?alt=media&token=74d58aa4-92d7-46a7-879b-5bf06624f5ff");
        // videoUri=Uri.parse(mStorageRef.toString());
        Log.d("@@videoUri",videoUri.toString());
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.start();
        isPlaying=true;



        //setting custum onItemClickListener
        recyclerView.setAdapter(new VideoViewAdapter(videoUriList,new VideoViewAdapter.RecyclerViewClickListener(){
            @Override
            public void onClick(VideoDetailModel videoDetailModelitem){
                Toast.makeText(getApplicationContext(), videoDetailModelitem.getTitle()+" "+" is selected", Toast.LENGTH_LONG).show();
                videoUri=Uri.parse(videoDetailModelitem.getVideoUri());
                // videoUri=Uri.parse(mStorageRef.toString());
                Log.d("@@videoUri",videoUri.toString());
                videoView.setVideoURI(videoUri);
                videoView.requestFocus();
                videoView.start();
                isPlaying=true;
             //  new VideoProgress().execute();
            }
        }));


        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                duration=mediaPlayer.getDuration()/1000;
                String durationString=String.format("%02d:%02d",duration/60,duration%60);
                textViewTotalTime.setText(durationString);
            }
        });

//        progressBarVideo.setMax(100);
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            //want contains type info and warning
            public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {

                if(what == mediaPlayer.MEDIA_INFO_BUFFERING_START){
                    progressBarStream.setVisibility(View.VISIBLE);
                }
                else if(what == mediaPlayer.MEDIA_INFO_BUFFERING_END){
                    progressBarStream.setVisibility(View.INVISIBLE);
                }
                else
                {
                    progressBarStream.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });


        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlaying){
                    videoView.pause();
                    isPlaying=false;
                    playBtn.setImageResource(android.R.drawable.ic_media_pause);
                }
                else{
                    videoView.start();
                    isPlaying=true;
                    playBtn.setImageResource(android.R.drawable.ic_media_play);
                }
            }
        });


        frwdBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // get current song position
                int currentPos = videoView.getCurrentPosition();
                if (currentPos + seekForwardTime <= videoView.getDuration()) {
                    // forward song
                    videoView.seekTo(seekBackwardTime -currentPos);
                }
                return  false;
            }
        });


        revBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // get current song position
                int currentPos = videoView.getCurrentPosition();
                if (currentPos - seekForwardTime >= 0) {
                    // forward song
                    videoView.seekTo(currentPos + seekForwardTime);
                }
                return  false;
            }
        });
    }



    @Override
    protected void onStop() {
        super.onStop();
        isPlaying=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isPlaying=false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPlaying=false;
    }
}
