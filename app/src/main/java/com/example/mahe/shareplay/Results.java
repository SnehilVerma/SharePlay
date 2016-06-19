package com.example.mahe.shareplay;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

/**
 * Created by MAHE on 6/19/2016.
 */
public class Results extends Activity {

    Bundle user_input;
    String[] userdata;
    // Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mresult);

        userdata = new String[1];

        user_input = getIntent().getExtras();
        userdata[0] = user_input.getString("list");


        TextView tv1 = (TextView) findViewById(R.id.textView);

        tv1.setText(userdata[0]);

/*
        Button button= (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                //Intent intent = new Intent(MainActivity.this, NewMessageActivity.class);
                //startActivity(intent);
                new shareIt().execute();


            }
        });*/
        // Close progressdialog


    }

   /* private class shareIt extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);



            // sharing pdf attempts
            /*File fileWithinMyDir = new File("/PDF/" + "playlist.pdf");

            if(fileWithinMyDir.exists()) {
                sharingIntent.setType("application/pdf");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+"/PDF/" + "playlist.pdf"));

                sharingIntent.putExtra(Intent.EXTRA_SUBJECT,
                        "Sharing File...");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

                startActivity(Intent.createChooser(sharingIntent, "Share File"));




                */


    //sharing via msg
 /*   File fileWithinMyDir = new File("/PDF/" + "playlist.pdf");

    if(fileWithinMyDir.exists())

    {
        sharingIntent.setType("application/pdf");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + "/PDF/" + "playlist.pdf"));

        sharingIntent.putExtra(Intent.EXTRA_SUBJECT,
                "Sharing File...");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

        startActivity(Intent.createChooser(sharingIntent, "Share File"));
    }

    return null;

*/



       public void shareIt(View view){
//sharing implementation here


        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);

       sharingIntent.setType("text/plain");
        String shareBody = userdata[0];
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));


      /* File fileWithinMyDir = new File("/PDF/" + "playlist.pdf");

        if(fileWithinMyDir.exists()) {
            sharingIntent.setType("application/pdf");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+"/PDF/" + "playlist.pdf"));

            sharingIntent.putExtra(Intent.EXTRA_SUBJECT,
                    "Sharing File...");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

            startActivity(Intent.createChooser(sharingIntent, "Share File"));
        }*/
    }

}
