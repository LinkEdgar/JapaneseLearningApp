package com.example.android.miwok;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class NumberActivity extends AppCompatActivity {
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


        audioManager = (AudioManager) this.getSystemService(this.AUDIO_SERVICE);


        //ArrayList of strings which contain japanese numbers
        final ArrayList<Words> numbers = new ArrayList<>();
        numbers.add(new Words("一","one",R.drawable.number_one,R.raw.one));
        numbers.add(new Words("二","two",R.drawable.number_two,R.raw.two));
        numbers.add(new Words("三","three",R.drawable.number_three,R.raw.three));
        numbers.add(new Words("四","four",R.drawable.number_four,R.raw.four));
        numbers.add(new Words("五","five",R.drawable.number_five,R.raw.five));
        numbers.add(new Words("六","six",R.drawable.number_six,R.raw.six));
        numbers.add(new Words("七","seven",R.drawable.number_seven,R.raw.seven));
        numbers.add(new Words("八","eight",R.drawable.number_eight,R.raw.eight));
        numbers.add(new Words("九", "nine",R.drawable.number_nine,R.raw.nine));
        numbers.add(new Words("十", "ten",R.drawable.number_ten,R.raw.ten));



        // An ArrayAdapter is used to control what the list view displays and keeps track of everything we need
        WordAdapter itemsAdapter = new WordAdapter(this, numbers,R.color.category_numbers);
        //we must find out listview
        ListView listView = (ListView) findViewById(R.id.item_list);
        //connnect the adapter to the listview
        listView.setAdapter(itemsAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();

                Words word = numbers.get(position);
                releaseMediaPlayer();

                //request audio focus for playback
                //first param is the onchange listener
                //second is the audio type
                //third is the length
                int result = audioManager.requestAudioFocus(afChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT );
                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mMediaPlayer = MediaPlayer.create(NumberActivity.this, word.getAudioFileResourceId());
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
        }
    }

}
