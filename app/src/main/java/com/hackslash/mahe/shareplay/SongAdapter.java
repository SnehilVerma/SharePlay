package com.hackslash.mahe.shareplay;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mahe.shareplay.R;

import java.util.ArrayList;

/**
 * Created by MAHE on 6/17/2016.
 */
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> {

    //SparseBooleanArray mCheckStates;


    private ArrayList<Song> songs;
    Context context;
    ImageView p;
    SeekBar seekBar;
    MediaPlayer mediaPlayer;
    Handler seekHandler;
    Runnable run;
    int flag=0;
//    public CheckBox box;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, artist;
        public CheckBox checkBox;
        public ImageView imageView;



        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.song_title);
            artist = (TextView) view.findViewById(R.id.song_artist);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            imageView=(ImageView)view.findViewById(R.id.play_song);


        }
    }


    public SongAdapter(Context context) {
        this.context=context;

    }


    public SongAdapter(ArrayList<Song> theSongs,Context context) {
        this.context=context;
        songs = theSongs;


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_layout, parent, false);


        return new MyViewHolder(itemView);


    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Song song = songs.get(position);

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


        holder.imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(context,songs.get(position).getPath().toString(),Toast.LENGTH_LONG).show();
                mediaPlayer=new MediaPlayer();
                seekHandler = new Handler();
                try {

                        //TODO: SET MUSIC PLAYER

                    final Dialog dialog=new Dialog(context);
                    dialog.setContentView(R.layout.music_popup);
                    dialog.setCancelable(false);

                    TextView tv1=(TextView)dialog.findViewById(R.id.song_title);
                    TextView tv2=(TextView)dialog.findViewById(R.id.song_artist);

                    tv1.setText(songs.get(position).getTitle().toString());
                    tv2.setText(songs.get(position).getArtist().toString());

                    p=(ImageView)dialog.findViewById(R.id.pause);
                    ImageView c=(ImageView) dialog.findViewById(R.id.cancel);


                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(songs.get(position).getPath());
                    mediaPlayer.prepare();
                    int max_duration=mediaPlayer.getDuration();
                    mediaPlayer.start();

                    seekBar=(SeekBar)dialog.findViewById(R.id.seekbar);
                    seekBar.setMax(max_duration);
                    //seekBar.setProgress(mediaPlayer.getCurrentPosition());

                    seekUpdate();





                    p.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(flag==0){
                                flag=1;
                                mediaPlayer.pause();
                                p.setImageResource(R.drawable.play);


                            }else if(flag==1){
                                flag=0;
                                mediaPlayer.start();
                                p.setImageResource(R.drawable.pause);



                            }

                        }
                    });

                    c.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mediaPlayer.stop();
                            dialog.dismiss();

                        }
                    });

                    //Intent in = new Intent(Intent.ACTION_VIEW);
                    //in.setDataAndType(Uri.parse(songs.get(position).getPath()), "audio/*");
                    //context.startActivity(in);
                    dialog.show();

                }catch (Exception e){
                    e.printStackTrace();
                }



            }
        });



    }




    public void seekUpdate(){

        run=new Runnable() {
            @Override
            public void run() {
                seekUpdate();
            }
        };

        seekBar.setProgress(mediaPlayer.getCurrentPosition());
            //Toast.makeText(context,String.valueOf(mediaPlayer.getCurrentPosition()),Toast.LENGTH_SHORT).show();
        seekHandler.postDelayed(run,100);





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