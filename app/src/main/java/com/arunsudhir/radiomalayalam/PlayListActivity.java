package com.arunsudhir.radiomalayalam;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.arunsudhir.radiomalayalam.io.PlaylistProgressExecutor;
import com.arunsudhir.radiomalayalam.io.PlaylistReaderAsyncTask;

public class PlayListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            new PlaylistReaderAsyncTask(new PlaylistProgressExecutor(this)).execute();
        }
        catch(java.net.ConnectException c)
        {
            setContentView(R.layout.activity_no_connection);
        }
    }
}
