package com.example.mca003.linkapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
public  class MainActivity extends AppCompatActivity{
    private ArrayList<Song> songList;
    private ListView songView;

    @Override

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songList = new ArrayList<Song>();
        SongAdapter songAdt = new SongAdapter(this, songList);
        songView = (ListView)findViewById(R.id.song_list);
        songView.setAdapter(songAdt);

        getSongList();


    }
    public void getSongList() {
        //retrieve song info
        ContentResolver musicResolver = getContentResolver();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 225);
        }


        Uri musicUri;

            musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;


        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        //getSongList();
        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        if(musicCursor!=null && musicCursor.moveToFirst()){
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


}

