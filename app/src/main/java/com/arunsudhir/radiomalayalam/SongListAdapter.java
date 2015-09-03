package com.arunsudhir.radiomalayalam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.app.Activity;

import com.arunsudhir.radiomalayalam.song.SongItem;

import java.util.List;
/**
 * Created by Arun on 9/2/2015.
 */
public class SongListAdapter extends ArrayAdapter {

    private Context context;
    private boolean useList = true;

    public SongListAdapter(Context context, List items) {
        super(context, android.R.layout.simple_list_item_1, items);
        this.context = context;
    }

    /**
     * Holder for the list items.
     */
    private class ViewHolder{
        TextView songNameText;
        TextView albumText;
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        SongItem item = (SongItem)getItem(position);
        View viewToUse = null;

        // This block exists to inflate the settings list item conditionally based on whether
        // we want to support a grid or list view.
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            if(useList){
                viewToUse = mInflater.inflate(R.layout.songgriditem, null);
            } else {
                viewToUse = mInflater.inflate(R.layout.songgriditem, null);
            }
            // cache the view atrefacts into holder for efficiency
            holder = new ViewHolder();
            holder.songNameText = (TextView)viewToUse.findViewById(R.id.songName);
            holder.albumText = (TextView)viewToUse.findViewById(R.id.album);
            viewToUse.setTag(holder);
        } else {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }

        holder.songNameText.setText(item.getSongName());
        holder.albumText.setText(item.getAlbum());
        return viewToUse;
    }
}