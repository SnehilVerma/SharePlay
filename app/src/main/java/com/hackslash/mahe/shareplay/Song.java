package com.hackslash.mahe.shareplay;

/**
 * Created by MAHE on 6/17/2016.
 */
public class Song {
    private long id;
    private String title;
    private String artist;
    private boolean isSelected;

    public Song(long songID, String songTitle, String songArtist) {
        id=songID;
        title=songTitle;
        artist=songArtist;
    }

    public Song(long songID, String songTitle, String songArtist,boolean isCheck) {
        id=songID;
        title=songTitle;
        artist=songArtist;
        isSelected=isCheck;
    }

    public long getID(){return id;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}

    public boolean getIsSelected(){
        return isSelected;}

    public void setSelected(boolean isSelected){
        this.isSelected=isSelected;
    }


}
