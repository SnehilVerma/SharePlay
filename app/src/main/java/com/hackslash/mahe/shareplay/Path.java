package com.hackslash.mahe.shareplay;

/**
 * Created by Snehil Verma on 7/21/2016.
 */
public class Path {
    private String title;
    private String path;


    public Path(String stitle,String spath){
        title=stitle;
        path=spath;
    }

    public String getPath(){
        return path;
    }
    public String getTitle(){
        return title;
    }
}
