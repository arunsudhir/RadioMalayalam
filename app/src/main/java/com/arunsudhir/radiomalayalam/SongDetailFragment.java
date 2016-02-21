package com.arunsudhir.radiomalayalam;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arunsudhir.radiomalayalam.communication.DownloadImageTask;
import com.arunsudhir.radiomalayalam.io.PlayerStateKeeper;
import com.arunsudhir.radiomalayalam.service.PlayerService;
import com.arunsudhir.radiomalayalam.song.SongItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.common.base.Function;

import java.util.ArrayList;

/**
 * A fragment representing a single Song detail screen.
 * This fragment is either contained in a {@link SongListActivity}
 * in two-pane mode (on tablets) or a {@link SongDetailActivity}
 * on handsets.
 */
public class SongDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_SONG_NAME = "SongName";

    /**
     * The dummy content this fragment is presenting.
     */
    private SongItem mItem;
    private String songname;
    private boolean _paused = false;
    View _rootView;
    private Bitmap _backupIcon;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SongDetailFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _backupIcon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_song_icon);
        Bundle extras =getActivity().getIntent().getExtras();
        if(extras != null) {
            if (extras.containsKey(ARG_SONG_NAME)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                songname =extras.getString(ARG_SONG_NAME);
                //mItem = SongContent.ITEM_MAP.get(getArguments().getString(ARG_SONG_NAME));
            }

            if (extras.containsKey("songItem")) {
                mItem = extras.getParcelable("songItem");
            }
        }
    }

    public LinearLayout ChangeBackground(Bitmap b)
    {
        if(_rootView != null)
        {
            LinearLayout view = ((LinearLayout) _rootView.findViewById(R.id.song_albumart));
            view.setBackground(new BitmapDrawable(b));
            return view;
        }
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_song_detail, container, false);
        AdView mAdView = (AdView) _rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        // Show the dummy content as text in a TextView.

        updateViewAndAlbumArtFromSongItem(mItem, _rootView, _backupIcon);
        final Activity thisActivity = getActivity();

        final FloatingActionButton playButton = (FloatingActionButton) _rootView.findViewById(R.id.play_button);
        playButton.setImageResource(android.R.drawable.ic_media_pause);
        _paused = true;
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(thisActivity, PlayerService.class);
                try {
                    //LOG.info("Toggling play/pause");
                    if(_paused)
                    {
                        playButton.setImageResource(android.R.drawable.ic_media_play);
                        _paused = false;
                    }
                    else
                    {
                        playButton.setImageResource(android.R.drawable.ic_media_pause);
                        _paused = true;
                    }
                    serviceIntent.putExtra("serviceCommand", "toggle");

                    thisActivity.startService(serviceIntent);
                } catch (Exception e) {
                    //LOG.error(e, "Failed to toggle state");
                }
            }
        });

        FloatingActionButton backButton = (FloatingActionButton) _rootView.findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<SongItem> playlist = PlayerStateKeeper.ReadCurrentPlaylist(thisActivity);
                int songIndex = PlayerStateKeeper.ReadCurrentSongIndex(thisActivity);
                songIndex = (songIndex -1) % (playlist.size());
                updateViewAndAlbumArtFromSongItem(playlist.get(songIndex), thisActivity.findViewById(android.R.id.content), _backupIcon);
                Intent serviceIntent = new Intent(thisActivity, PlayerService.class);
                try {
                    //LOG.info("Toggling play/pause");
                    serviceIntent.putExtra("serviceCommand", "skipBack");
                    thisActivity.startService(serviceIntent);
                } catch (Exception e) {
                    //LOG.error(e, "Failed to toggle state");
                }
            }
        });

        FloatingActionButton fwdButton = (FloatingActionButton) _rootView.findViewById(R.id.fwd_button);

        fwdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<SongItem> playlist = PlayerStateKeeper.ReadCurrentPlaylist(thisActivity);
                int songIndex = PlayerStateKeeper.ReadCurrentSongIndex(thisActivity);
                songIndex = (songIndex + 1) % (playlist.size());
                updateViewAndAlbumArtFromSongItem(playlist.get(songIndex), thisActivity.findViewById(android.R.id.content), _backupIcon);
                Intent serviceIntent = new Intent(thisActivity, PlayerService.class);
                try {
                    //LOG.info("Toggling play/pause");
                    serviceIntent.putExtra("serviceCommand", "skipFwd");
                    thisActivity.startService(serviceIntent);
                } catch (Exception e) {
                    //LOG.error(e, "Failed to toggle state");
                }
            }
        });
        return _rootView;
    }

    static void updateViewAndAlbumArtFromSongItem(SongItem mItem, final View rootView, Bitmap icon)
    {
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.song_name)).setText(mItem.getSongName());
            ((TextView) rootView.findViewById(R.id.song_album)).setText(mItem.album);

            Function<Bitmap,LinearLayout> changeBack = new Function<Bitmap, LinearLayout>() {
                @Override
                public LinearLayout apply(Bitmap input) {
                    if(rootView != null)
                    {
                        LinearLayout view = ((LinearLayout) rootView.findViewById(R.id.song_albumart));
                        view.setBackground(new BitmapDrawable(input));
                        return view;
                    }
                    return null;
                }
            };
            DownloadImageTask<LinearLayout> albumArtTask = new DownloadImageTask<LinearLayout>(changeBack, icon);
            albumArtTask.execute(mItem.getAlbumArtPath().replace(" ", "%20"));
        }
    }
}
