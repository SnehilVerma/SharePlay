package com.hackslash.mahe.shareplay;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.mahe.shareplay.R;

import java.util.ArrayList;

/**
 * Created by MAHE on 6/17/2016.
 */
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> {

    //SparseBooleanArray mCheckStates;


    private ArrayList<Song> songs;
//    public CheckBox box;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, artist;
        public CheckBox checkBox;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.song_title);
            artist = (TextView) view.findViewById(R.id.song_artist);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);


        }
    }


    public SongAdapter() {

    }


    public SongAdapter(ArrayList<Song> theSongs) {
        songs = theSongs;


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_layout, parent, false);


        return new MyViewHolder(itemView);


    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Song song = songs.get(position);

        holder.title.setText(song.getTitle());
        holder.artist.setText(song.getArtist());

        holder.checkBox.setChecked(song.getIsSelected());
        holder.checkBox.setTag(song);


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Song song = (Song) cb.getTag();

                song.setSelected(cb.isChecked());
                songs.get(position).setSelected(cb.isChecked());
            }
        });


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


    public ArrayList<Song> getSongList() {
        return songs;
    }



    public void selectAll(int flag,ArrayList<Song> list) {
        //ArrayList<Song> list;
        //list=getSongList();

        if (flag == 1) {
            for (int i = 0; i < list.size(); i++) {

                list.get(i).setSelected(true);
            }
        } else if(flag== 0) {
            for(int i=0;i<list.size();i++)
                list.get(i).setSelected(false);
        }


    }
}