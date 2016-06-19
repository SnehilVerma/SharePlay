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

    SongAdapter songAdt;
    StringBuilder result;


    Song songs[];

    Document document;
    String FILE;
    String line;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songView = (RecyclerView) findViewById(R.id.song_list);
        songList = new ArrayList<Song>();

        getSongList();

        //sorting acc to title names


        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        //Recycler View

        songAdt = new SongAdapter(songList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        songView.setLayoutManager(mLayoutManager);
        songView.setItemAnimator(new DefaultItemAnimator());
        songAdt.notifyDataSetChanged();
        songView.setAdapter(songAdt);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //sahre button

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.icon_share);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                //Intent intent = new Intent(MainActivity.this, NewMessageActivity.class);
                //startActivity(intent);
                new statusCheck().execute();


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


    private class statusCheck extends AsyncTask<Void,Void,StringBuilder>{


        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(MainActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Generating a shareable PDF");
            // Set progressdialog message
            mProgressDialog.setMessage("Almost there!");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();



        }

        @Override
        protected StringBuilder doInBackground(Void... params) {
            result = new StringBuilder();



            ArrayList<Song> songs= ((SongAdapter)songAdt).getSongList();


            for (int i = 0; i < songs.size(); i++) {
                Song singleSong = songs.get(i);
                if (singleSong.getIsSelected() == true) {

                    result.append(singleSong.getTitle().toString());
                    result.append("\n");

                }

            }
            try {
                Thread.sleep(2000);

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



            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }



        @Override
        protected void onPostExecute(StringBuilder result) {

            mProgressDialog.dismiss();
            //displayToast(result);
            addMetaData(document);


            try{
                addTitlePage(document);

            }catch (Exception e){
                e.printStackTrace();
            }

            /*File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    +"/PDF/playlist.pdf");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);*/



            document.close();



            Toast t = Toast.makeText(MainActivity.this,result, Toast.LENGTH_SHORT);
            t.show();
            Intent i=new Intent(getApplicationContext(),Results.class);
            Bundle extras=new Bundle();
            extras.putString("list", String.valueOf(result));

            i.putExtras(extras);
            startActivity(i);

        }

    }

        ////////////////////////// DO the sharing part now /////////////////////////////



   /* public void shareIt() {
//sharing implementation here



        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);

        sharingIntent.setType("text/plain");
        String shareBody = list;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

*/





    public void displayToast(StringBuilder result){
        Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();

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

        //displayToast(line);
    }



}
