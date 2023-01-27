package com.example.rss_reader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class FeedViewActivity extends AppCompatActivity {
    TextView tittleView, descriptionView, linkView, dateView;
    ImageView imageView;
    String imageLink, link;
    ProgressDialog progressDialog;
    Bitmap bitmapImage = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_view);

        tittleView = findViewById(R.id.newTille);
        descriptionView = findViewById(R.id.newDescription);
        linkView = findViewById(R.id.newLink);
        dateView = findViewById(R.id.newDate);
        imageView = findViewById(R.id.newImage);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String pubDate = getIntent().getStringExtra("pubDate");
        link = getIntent().getStringExtra("link");
        imageLink = getIntent().getStringExtra("imageLink");

        tittleView.setText(title);
        descriptionView.setText(description);
        dateView.setText(pubDate);
        linkView.setText(link);
        DownloadImage downloadImage = new DownloadImage();
        downloadImage.execute();
    }

    public void backToHomePage(View view){
        Intent intent = new Intent(getApplicationContext(),RssFeedActivity.class);
        startActivity(intent);
    }

    public class DownloadImage extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(FeedViewActivity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String current = "";
            try {
                URL url = new URL(imageLink);
                bitmapImage = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch(IOException e) {
                System.out.println(e);
            }
            return current;
        }
        @Override
        protected void onPostExecute(String s) {
            imageView.setImageBitmap(bitmapImage);
            progressDialog.dismiss();
        }
    }
    public void openLinkInBrower(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        startActivity(intent);
    }
}