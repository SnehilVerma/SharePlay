package com.example.mahe.shareplay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

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
import java.util.List;

import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;



public class MainActivity extends AppCompatActivity {


    private ArrayList<Song> songList;


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
                new statusCheck().execute();        //begin the async task to generate a pdf and and start result activity.



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
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }
    }







    private class statusCheck extends AsyncTask<Void,Void,StringBuilder> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected StringBuilder doInBackground(Void... params) {
            result = new StringBuilder();


            ArrayList<Song> songs = songAdt.getSongList();


            for (int i = 0; i < songs.size(); i++) {
                Song singleSong = songs.get(i);
                if (singleSong.getIsSelected() == true) {

                    result.append(singleSong.getTitle().toString());
                    result.append("\n");

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


            return result;
        }


        @Override
        protected void onPostExecute(StringBuilder result) {





            addMetaData(document);


            try {
                addTitlePage(document);

            } catch (Exception e) {
                e.printStackTrace();
            }

            document.close();


            Intent i = new Intent(getApplicationContext(), Results.class);
            Bundle extras = new Bundle();
            extras.putString("list", String.valueOf(result));

            i.putExtras(extras);
            startActivity(i);


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



}
