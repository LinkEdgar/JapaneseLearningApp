package com.example.android.miwok;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static android.media.AudioManager.AUDIOFOCUS_LOSS;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;

public class ColorsActivity extends AppCompatActivity {
    private MediaPlayer mMediaPlayer;
    private MediaPlayer.OnCompletionListener mComplete = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };
    private AudioManager audioManager;
    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
               //pause for either case and reset the audio to zero
                if(mMediaPlayer != null){
                    mMediaPlayer.pause();
                    mMediaPlayer.seekTo(0);
                }
            }
            else if(focusChange == AudioManager.AUDIOFOCUS_GAIN){
                //there is no resume method .start does this
                if(mMediaPlayer != null) {
                    mMediaPlayer.start();
                }
            }
            else if(focusChange == AudioManager.AUDIOFOCUS_LOSS){
                releaseMediaPlayer();
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_items);
        //enable up button

        //ArrayLisr for colors
        final ArrayList<Words> colors = new ArrayList<>();

        //AudioManager instantiation
        audioManager = (AudioManager) this.getSystemService(this.AUDIO_SERVICE);


        colors.add(new Words("赤","red",R.drawable.color_red,R.raw.red));
        colors.add(new Words("緑","green",R.drawable.color_green,R.raw.green));
        colors.add(new Words("黒","black",R.drawable.color_black,R.raw.black));
        colors.add(new Words("白","white",R.drawable.color_white,R.raw.white));
        colors.add(new Words("茶色","brown",R.drawable.color_brown,R.raw.brown));
        colors.add(new Words("灰色","gray",R.drawable.color_gray,R.raw.gray));


        WordAdapter colorAdapter = new WordAdapter(this, colors,R.color.category_colors);
        ListView colorListView = (ListView) findViewById(R.id.item_list);
        colorListView.setAdapter(colorAdapter);

        colorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();

                Words word = colors.get(position);
                releaseMediaPlayer();

                //request audio focus for playback
                //first param is the onchange listener
                //second is the audio type
                //third is the length
                int result = audioManager.requestAudioFocus(afChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT );
                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mMediaPlayer = MediaPlayer.create(ColorsActivity.this, word.getAudioFileResourceId());
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(mComplete);
                }

            }
        });


    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;
            audioManager.abandonAudioFocus(afChangeListener);
        }
    }
}
