package com.example.android.miwok;

import android.app.Activity;
import android.media.Image;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by EndUser on 7/29/2017.
 */

public class WordAdapter extends ArrayAdapter<Words> {
    private int colorResourceId;
    public WordAdapter(Activity context, ArrayList<Words> words,int colorResource){
        super(context,0,words);
        colorResourceId = colorResource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
      //assigns the current listview to the a view varibal called listItemView
       View listItemView = convertView;
       // if the view is empty then we inflate its resources
       if(listItemView == null){
           listItemView = LayoutInflater.from(getContext()).inflate(R.layout.word_layout,parent,false);
       }
       // makes a word from the current position of our word arraylist
        Words currentWord = getItem(position);
        //finds the textview for the japanese word
        TextView japaneseTextView = (TextView) listItemView.findViewById(R.id.japanese_text_view);
        //setting the text for the japanese word
        japaneseTextView.setText(currentWord.japaneseTranslation());
        //find the text view for the english word
        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.default_text_view);
        //sets the text for the english word
        defaultTextView.setText(currentWord.defaultTranslation());


        //find the image view in the view
        ImageView resourceImage = (ImageView) listItemView.findViewById(R.id.word_image_view);

        if(currentWord.hasImage()) {
            //set the desired image to that the image view
            resourceImage.setImageResource(currentWord.getImageResourceId());
            resourceImage.setVisibility(View.VISIBLE);
    }
    else{
            //resourceImage.setImageResource(R.mipmap.ic_launcher);
            resourceImage.setVisibility(View.GONE);

    }
        View colorChange = (View) listItemView.findViewById(R.id.color_container);
        int color = ContextCompat.getColor(getContext(),colorResourceId);
        colorChange.setBackgroundColor(color);



        //returns the whole list item layout (containing 2 textviews
        return listItemView;


    }
}
