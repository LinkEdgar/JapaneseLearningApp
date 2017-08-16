package com.example.android.miwok;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity {
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


        final ArrayList<Words> familyList = new ArrayList<>();

        familyList.add(new Words("兄","older brother",R.drawable.family_older_brother,R.raw.olderbrother));
        familyList.add(new Words("弟","younger brother",R.drawable.family_younger_brother,R.raw.youngerbrother));
        familyList.add(new Words("姉","older sister",R.drawable.family_older_sister,R.raw.oldersister));
        familyList.add(new Words("妹","younger sister",R.drawable.family_younger_sister,R.raw.youngersister));
        familyList.add(new Words("お母さん","mother",R.drawable.family_mother,R.raw.mom));
        familyList.add(new Words("お父さん","father",R.drawable.family_father,R.raw.father));
        familyList.add(new Words("お祖母さん","grandmother",R.drawable.family_grandmother,R.raw.grandmother));
        familyList.add(new Words("お祖父さん","grandfather",R.drawable.family_grandfather,R.raw.grandfather));
        familyList.add(new Words("息子","son",R.drawable.family_son,R.raw.knowmysteez));
        familyList.add(new Words("娘","daughter",R.drawable.family_daughter,R.raw.knowmysteez));


        WordAdapter familyAdapter = new WordAdapter(this, familyList,R.color.category_family);
        ListView familyListView = (ListView) findViewById(R.id.item_list);
        familyListView.setAdapter(familyAdapter);


        familyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();

                Words word = familyList.get(position);
                releaseMediaPlayer();

                //request audio focus for playback
                //first param is the onchange listener
                //second is the audio type
                //third is the length
                int result = audioManager.requestAudioFocus(afChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT );
                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mMediaPlayer = MediaPlayer.create(FamilyActivity.this, word.getAudioFileResourceId());
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
