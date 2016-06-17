package com.example.mahe.shareplay;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by MAHE on 6/17/2016.
 */
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> implements CompoundButton.OnCheckedChangeListener {

    SparseBooleanArray mCheckStates;


    private ArrayList<Song> songs;
    private LayoutInflater songInf;



    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView title,artist;
        public CheckBox checkBox;





        public MyViewHolder(View view){
            super(view);
            title=(TextView)view.findViewById(R.id.song_title);
            artist=(TextView)view.findViewById(R.id.song_artist);
            checkBox=(CheckBox)view.findViewById(R.id.checkBox);


        }
    }





    public SongAdapter(ArrayList<Song> theSongs){
        songs=theSongs;
        //songInf=LayoutInflater.from(c);
        mCheckStates=new SparseBooleanArray(theSongs.size());
    }






    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_layout,parent,false);

        return new MyViewHolder(itemView);


    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Song song=songs.get(position);

        holder.title.setText(song.getTitle());
        holder.artist.setText(song.getArtist());



        holder.checkBox.setTag(position);
        holder.checkBox.setChecked(mCheckStates.get(position,false));
        holder.checkBox.setOnCheckedChangeListener(this);



    }


    @Override
    public int getItemCount() {
        return songs.size();
    }







    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }


    public boolean isChecked(int position){
        return mCheckStates.get(position,false);
    }




    public void setChecked(int position,boolean isChecked){
        mCheckStates.put(position,isChecked);
    }





    public void toggle(int position){
        setChecked(position,!isChecked(position));
    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        mCheckStates.put((Integer)buttonView.getTag(),isChecked);

    }











    /*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //map to song layout
        LinearLayout songLay = (LinearLayout)songInf.inflate(R.layout.song, parent, false);
        //get title and artist views
        TextView songView = (TextView)songLay.findViewById(R.id.song_title);
        TextView artistView = (TextView)songLay.findViewById(R.id.song_artist);
        CheckBox checkBox=(CheckBox)songLay.findViewById(R.id.checkBox);
        //get song using position
        Song currSong = songs.get(position);
        //get title and artist strings

        songView.setText(currSong.getTitle());
        artistView.setText(currSong.getArtist());

        // checkbox handling//
        checkBox.setTag(position);
        checkBox.setChecked(mCheckStates.get(position,false));
        checkBox.setOnCheckedChangeListener(this);



        //set position as tag
        songLay.setTag(position);

        return songLay;
    }

    */



}