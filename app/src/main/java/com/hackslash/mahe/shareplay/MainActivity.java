package com.hackslash.mahe.shareplay;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Menu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.widget.Toast;


import com.example.mahe.shareplay.R;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;



public class MainActivity extends AppCompatActivity {


    private ArrayList<Song> songList;


    ArrayList<Path> files=new ArrayList<Path>();    // to store path of music files
    StringBuilder resultpaths;

    AlertDialog.Builder alertDialog;


    private RecyclerView songView;


    SongAdapter songAdt;    // adapter for Song model.
    StringBuilder result;




    Document document;      //document(pdf) from IText Library
    String FILE;
    String line;

    AppCompatCheckBox checkBox;     //select all checkbox
    int FLAG=0;                     //flag to check the state of select all checkbox



    ArrayList<Song> list;

    SongAdapter s=new SongAdapter();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songView = (RecyclerView) findViewById(R.id.song_list);
        songList = new ArrayList<Song>();

        getSongList();



        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });


        songAdt = new SongAdapter(songList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        songView.setLayoutManager(mLayoutManager);
        songView.setItemAnimator(new DefaultItemAnimator());
        songAdt.notifyDataSetChanged();
        songView.setAdapter(songAdt);




        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //share button


        checkBox=(AppCompatCheckBox)findViewById(R.id.allcheck);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    if(checkBox.isChecked()==true){

                        FLAG=1;
                        list=songAdt.getSongList();
                        s.selectAll(FLAG,list);         //select all items

                    }
                    else{

                        FLAG=0;
                        list=songAdt.getSongList();
                        s.selectAll(FLAG,list);}    //deselect all items


                    songAdt.notifyDataSetChanged();     //refresh the recycler view items to update changes (VERY IMPORTANT)





            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.icon_share);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                //Intent intent = new Intent(MainActivity.this, NewMessageActivity.class);
                //startActivity(intent);
                openDialog();




            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void getSongList() {
        //retrieve song info
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int data=musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);



            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new Song(thisId, thisTitle, thisArtist));




                String path=musicCursor.getString(data);    // storing the path of all the song files in an array
                files.add(new Path(thisTitle,path));


            }
            while (musicCursor.moveToNext());
        }

            Collections.sort(files, new Comparator<Path>() {
            public int compare(Path a, Path b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });


    }



    private class loadIntent extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent i = new Intent(getApplicationContext(), Results.class);
            Bundle extras = new Bundle();
            extras.putString("list", String.valueOf(result));

            i.putExtras(extras);
            startActivity(i);



        }
    }



    private class songCheck extends AsyncTask<Void,Void,Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... params) {
            result = new StringBuilder();
            resultpaths = new StringBuilder();

            ArrayList<Song> songs = songAdt.getSongList();


            for (int i = 0; i < songs.size(); i++) {
                Song singleSong = songs.get(i);
                Path singlePath = files.get(i);

                if (singleSong.getIsSelected() == true) {


                    result.append(singleSong.getTitle().toString());
                    result.append("\n");


                    resultpaths.append(singlePath.getPath().toString());        //storing the paths of the songs selected
                    resultpaths.append("\n");


                }

            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {


            Intent share = new Intent(Intent.ACTION_SEND_MULTIPLE);
            share.setType("audio/mp3");
            String songpath[]=resultpaths.toString().split("\n");

            ArrayList<Uri> uris=new ArrayList<Uri>();

            for(int i=0;i<songpath.length;i++){
                Uri uri=Uri.parse("file:///"+songpath[i]);
                uris.add(uri);

            }

            share.putExtra(Intent.EXTRA_STREAM,uris);
            startActivity(Intent.createChooser(share,"Share files"));
        /*
        share.putExtra(Intent.EXTRA_SUBJECT,"Ringtone File : "+ getResources().getResourceEntryName(resId)+".mp3");
        share.putExtra(Intent.EXTRA_TEXT,"Ringtone File : "+getResources().getResourceEntryName(resId)+".mp3");
        share.putExtra(Intent.EXTRA_STREAM,Uri.parse("android.resource://com.my.android.soundfiles/"+resId));
        share.putExtra("sms_body","Ringtone File : "+ getResources().getResourceEntryName(resId)+".mp3");
        */


        }
    }









    private class statusCheck extends AsyncTask<Void,Void,Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... params) {
            result = new StringBuilder();
            resultpaths= new StringBuilder();

            ArrayList<Song> songs = songAdt.getSongList();



            for (int i = 0; i < songs.size(); i++) {
                Song singleSong = songs.get(i);
                Path singlePath= files.get(i);

                if (singleSong.getIsSelected() == true) {


                    result.append(singleSong.getTitle().toString());
                    result.append("\n");



                    resultpaths.append(singlePath.getPath().toString());        //storing the paths of the songs selected
                    resultpaths.append("\n");




                }

            }

            ////// setup the pdf and insert data into it //////////
            FILE = Environment.getExternalStorageDirectory().toString()
                    + "/PDF/" + "playlist.pdf";

            document = new Document(PageSize.A4);
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/PDF");
            myDir.mkdirs();

            try {
                PdfWriter.getInstance(document, new FileOutputStream(FILE));
            } catch (Exception e) {
                e.printStackTrace();
            }

            document.open();

            return null;
            //return result;
        }


        @Override
        protected void onPostExecute(Void aVoid) {





            addMetaData(document);


            try {
                addTitlePage(document);

            } catch (Exception e) {
                e.printStackTrace();
            }

            document.close();



        }
    }












    public void addMetaData(Document document)
    {
        document.addTitle("PLAYLIST");
        document.addSubject("LOVE MUSIC");

    }



    public void addTitlePage(Document document) throws DocumentException{

        Paragraph para = new Paragraph();

        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD );
        Paragraph prHead = new Paragraph();
        prHead.setFont(titleFont);
        prHead.setAlignment(Element.ALIGN_CENTER);
        prHead.add("PLAYLIST \n \n \n");
        document.add(prHead);

        String finres[]=result.toString().split("\n");

        Font paraFont = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD );
        para.setFont(paraFont);

        for(int i=0;i<finres.length;i++){
            para.add(i+1 + ")" + " ");
            para.add(finres[i]);
            para.add("\n");
        }



        document.add(para);


    }





    ///////////////////////UNUSED METHOD////////////////
    public String read_file(Context context, String playlist) {
        try {
            FileInputStream fis = context.openFileInput(playlist);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        } catch (IOException e) {
            return "";
        }


    }

    public void openDialog(){
        alertDialog=new AlertDialog.Builder(this);
        alertDialog.setMessage("What do you want to share?");


        alertDialog.setPositiveButton("Share songs", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new songCheck().execute();  // begin the async task to share the songs
            }
        });


        alertDialog.setNegativeButton("Share playlist", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new statusCheck().execute();        //begin the async task to generate a pdf and and start result activity.
                new loadIntent().execute();

            }
        });

        AlertDialog alertDialog1=alertDialog.create();
        alertDialog1.show();

    }




    /*
    public void shareSongs()
    {
        Intent share = new Intent(Intent.ACTION_SEND_MULTIPLE);
        share.setType("audio/mp3");
        String songpath[]=resultpaths.toString().split("\n");

        ArrayList<Uri> uris=new ArrayList<Uri>();

        for(int i=0;i<songpath.length;i++){
            Uri uri=Uri.parse("file:///"+songpath[i]);
            uris.add(uri);

        }

        share.putExtra(Intent.EXTRA_STREAM,uris);
        startActivity(Intent.createChooser(share,"Share files"));
        /*
        share.putExtra(Intent.EXTRA_SUBJECT,"Ringtone File : "+ getResources().getResourceEntryName(resId)+".mp3");
        share.putExtra(Intent.EXTRA_TEXT,"Ringtone File : "+getResources().getResourceEntryName(resId)+".mp3");
        share.putExtra(Intent.EXTRA_STREAM,Uri.parse("android.resource://com.my.android.soundfiles/"+resId));
        share.putExtra("sms_body","Ringtone File : "+ getResources().getResourceEntryName(resId)+".mp3");


    }*/



}
