package com.example.rss_reader;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class RssFeedActivity extends AppCompatActivity {
    ArrayList<Feed> newFeed = new ArrayList<Feed>();
    private ListView listView;
    private ProgressDialog progressDialog;
    private EditText rssLink;
    private String stringUrl;
    private DownloadPage task = new DownloadPage();
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;
    private Boolean userIsSub;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_feed);


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userPro = snapshot.getValue(User.class);
                if(userPro !=null){
                    userIsSub = userPro.getSub();
                    if(userIsSub==true){
                        stringUrl = userPro.getRssLink();
                        task.execute();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        auth = FirebaseAuth.getInstance();
        rssLink = findViewById(R.id.rss_Input);
        listView = findViewById(R.id.customeListView);
    }

    public class DownloadPage extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RssFeedActivity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
            SettingUpPeriodicWork();
        }

        @Override
        protected String doInBackground(String... params) {
            GetRssFeed getRssFeed = new GetRssFeed(userId,stringUrl);
            newFeed = getRssFeed.getFeeds();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            NewAdaper newAdaper = new NewAdaper(getApplicationContext(),R.layout.new_item,newFeed);
            listView.setAdapter(newAdaper);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Feed feed = newFeed.get(i);
                    saveFeed(feed);
                    newFeed.get(i);
                    Intent intent = new Intent(getApplicationContext(),FeedViewActivity.class);
                    intent.putExtra("title",newFeed.get(i).getTitle());
                    intent.putExtra("description",newFeed.get(i).getDescription());
                    intent.putExtra("pubDate",newFeed.get(i).getPubDate());
                    intent.putExtra("link",newFeed.get(i).getLink());
                    intent.putExtra("imageLink",newFeed.get(i).getImageLink());
                    startActivity(intent);

                }
            });
            saveLastNew(newFeed.get(0));
            progressDialog.dismiss();
        }
    }

    public void  SubToLink(View view){
        if(rssLink.getText().toString().isEmpty()){
            return;
        }
        newFeed = new ArrayList<Feed>();
        stringUrl = rssLink.getText().toString();
        updateSub(true,stringUrl);
        task = new DownloadPage();
        task.execute();
    }

    public void LogOutUser(View view){
        auth.signOut();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void updateSub(Boolean sub,String link){
        HashMap newSub = new HashMap();
        newSub.put("sub",sub);
        newSub.put("rssLink",link);
        reference.child(userId).updateChildren(newSub).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){

                }
                else {

                }
            }
        });
    }
    public void saveFeed(Feed feed){
        HashMap newFed = new HashMap();
        String feedId = FirebaseDatabase.getInstance().getReference("FeedViewed").push().getKey();
        newFed.put("userId",feed.getUserId());
        newFed.put("tittle",feed.getTitle());
        newFed.put("description",feed.getDescription());
        newFed.put("date",feed.getPubDate());
        newFed.put("imageLink",feed.getImageLink());
        newFed.put("newLink",feed.getLink());
        FirebaseDatabase.getInstance().getReference("FeedViewed")
                .child(feedId)
                .setValue(newFed).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                        }
                        else{
                            System.out.println("nini lisusu");
                        }
                    }
                });
    }

    private void SettingUpPeriodicWork() {
        // Create Network constraint
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .setRequiresStorageNotLow(true)
                .build();

        PeriodicWorkRequest periodicSendDataWork =
                new PeriodicWorkRequest.Builder(CheckRssUpdate.class, 15, TimeUnit.MINUTES)
                        .addTag("CHECK IT")
                        .setConstraints(constraints)
                        //.setBackoffCriteria(BackoffPolicy.LINEAR, PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                        .build();

        WorkManager workManager = WorkManager.getInstance(this);
        workManager.enqueue(periodicSendDataWork);
    }
    public void saveLastNew(Feed feed){
        HashMap newSub = new HashMap();
        newSub.put("lastNewListed",feed.getTitle());
        reference.child(userId).updateChildren(newSub).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){

                }
                else {

                }
            }
        });
    }
}