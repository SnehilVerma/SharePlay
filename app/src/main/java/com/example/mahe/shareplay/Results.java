package com.example.mahe.shareplay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by MAHE on 6/19/2016.
 */
public class Results extends AppCompatActivity{

    Bundle user_input;
    String[] userdata;
    ListView mlist;
    ArrayAdapter<String> adapter;

    ArrayList<String> songlist = new ArrayList<>();
    // Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mresult);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);


        userdata = new String[1];
        user_input = getIntent().getExtras();
        userdata[0] = user_input.getString("list");
        mlist = (ListView) findViewById(R.id.list);
        for (int i = 0; i < userdata.length; i++) {
            songlist.add(i, userdata[i]);
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, songlist);


        new shareIt().execute();







    }

    private class shareIt extends AsyncTask<Void, Void, Void> {

        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(Results.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Generating a shareable PDF");
            // Set progressdialog message
            mProgressDialog.setMessage("Almost there!");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();


        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Thread.sleep(2000);











            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            TextView tv1 = (TextView) findViewById(R.id.output);

            tv1.setText("MY  PLAYLIST");

            mlist.setAdapter(adapter);


            mProgressDialog.dismiss();
        }
    }





       public void shareIt(View view) {
           //sharing implementation here


           Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);

           sharingIntent.setType("text/plain");
           String shareBody = userdata[0];
           sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
           sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
           startActivity(Intent.createChooser(sharingIntent, "Share via"));
       }



    public void viewPdf(View view){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF/playlist.pdf");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

}
