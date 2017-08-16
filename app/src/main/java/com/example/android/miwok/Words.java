package com.example.android.miwok;

/**
 * Created by EndUser on 7/29/2017.
 */

public class Words {
    private int audioFileResourceId;
    private String englishWord;
    private String japaneseWord;
    private static final int NO_IMAGE_PROVIDED =-1;
    private  int imageResourceId = NO_IMAGE_PROVIDED;
    public Words(String japanese, String english){
        englishWord = english;
        japaneseWord = japanese;
    }
    public Words(String japanese, String english, int audioFile){
        englishWord = english;
        japaneseWord = japanese;
        audioFileResourceId = audioFile;
    }

    public Words(String japanese, String english, int imageID , int audioFile ){
        englishWord = english;
        japaneseWord = japanese;
        imageResourceId = imageID;
        audioFileResourceId = audioFile;
    }
    public String defaultTranslation(){
        return englishWord;
    }
    public String japaneseTranslation(){
        return japaneseWord;
    }
    public int getImageResourceId(){return imageResourceId;};
    public boolean hasImage(){
    return imageResourceId != NO_IMAGE_PROVIDED;
    }
    public int getAudioFileResourceId(){return audioFileResourceId;}

}
