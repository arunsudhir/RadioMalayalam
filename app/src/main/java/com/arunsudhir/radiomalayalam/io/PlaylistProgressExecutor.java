package com.arunsudhir.radiomalayalam.io;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.arunsudhir.radiomalayalam.R;
import com.arunsudhir.radiomalayalam.SongListActivity;
import com.arunsudhir.radiomalayalam.communication.CommunicationConstants;
import com.arunsudhir.radiomalayalam.playlist.PlaylistItem;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardGridView;

/**
 * Created by Arun on 11/17/2015.
 */
public class PlaylistProgressExecutor implements AsyncTaskPreAndPostExecutor<PlaylistItem>
{
    ProgressDialog pDialog;
    Activity containingActivity;

    public PlaylistProgressExecutor(Activity a)
    {
        containingActivity = a;
    }
    @Override
    public void PreExecute() {
        pDialog = new ProgressDialog(containingActivity);
        pDialog.setMessage("Loading Playlists ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    public void PostExecute(ArrayList<PlaylistItem> result) {
        pDialog.dismiss();
        ArrayList<Card> cards = new ArrayList<>();
        for(int i=0; i< result.size(); i++)
        {
            final PlaylistItem item = result.get(i);
            // create a card ofr each item
            //Create a Card
            Card card = new Card(containingActivity);

            //Create a CardHeader
           // CardHeader header = new CardHeader(containingActivity);
            //header.setTitle(item.playlistName);
            //Add Header to cards
            //card.addCardHeader(header);
            card.setExpanded(false);
            card.setTitle(item.playlistName);
            CardThumbnail ct = new CardThumbnail(containingActivity);
            String heroImage = item.getHeroImageUrl();
            if(heroImage != null && !heroImage.isEmpty()) {
                ct.setUrlResource(item.getHeroImageUrl());
            }
            card.addCardThumbnail(ct);
            card.setClickable(true);
            card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    Intent songListIntent = new Intent(containingActivity, SongListActivity.class);
                    String encodedString = item.getPlaylistUrl();
                    encodedString = encodedString.replace(" ","%20");
                    songListIntent.putExtra("url", encodedString);
                    containingActivity.startActivity(songListIntent);
                }
            });
            cards.add(card);
        }
        CardGridArrayAdapter mCardArrayAdapter = new CardGridArrayAdapter(containingActivity,cards);

        CardGridView gridView = (CardGridView) containingActivity.findViewById(R.id.myGrid);
        if (gridView!=null){
            gridView.setAdapter(mCardArrayAdapter);
        }

        //serialize the currentPlaylists onto file
        FileOutputStream fos;
        ObjectOutputStream oos;

        try {
            fos = containingActivity.openFileOutput(CommunicationConstants.CurrentPlaylist, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(result);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
