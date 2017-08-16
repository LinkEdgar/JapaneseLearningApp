package com.example.android.miwok;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {
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


        final ArrayList<Words> phrasesList = new ArrayList<>();
        phrasesList.add(new Words("ありがとう","Thank you",R.raw.thankyou));
        phrasesList.add(new Words("しつれいします","Excuse me",R.raw.excuseme));
        phrasesList.add(new Words("くそ！","Dammit",R.raw.shit));
        phrasesList.add(new Words("ゆっくり話してください","Please speak slowly",R.raw.speakslowly));
        phrasesList.add(new Words("もう一度お願いします","One more time please",R.raw.onemoretime));
        phrasesList.add(new Words("初めましてよろしくお願いします","Nice to meet you",R.raw.nicetomeetyou));
        phrasesList.add(new Words("〇〇にはどういけばいいですか？","How do I go __",R.raw.howtogo));
        phrasesList.add(new Words("死の匂いか？いつでも来い","Is that death? Come anytime!",R.raw.isthatdeath));
        phrasesList.add(new Words("鬼がいる！","Oni is here!",R.raw.oniishere));

        WordAdapter phraseAdapter = new WordAdapter(this, phrasesList,R.color.category_phrases);
        ListView phrasesListView = (ListView) findViewById(R.id.item_list);
        phrasesListView.setAdapter(phraseAdapter);




        phrasesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();

                Words word = phrasesList.get(position);
                //request audio focus for playback
                //first param is the onchange listener
                //second is the audio type
                //third is the length
                int result = audioManager.requestAudioFocus(afChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT );
                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, word.getAudioFileResourceId());
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
