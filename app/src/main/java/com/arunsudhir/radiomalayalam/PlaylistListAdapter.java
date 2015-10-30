package com.arunsudhir.radiomalayalam;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.arunsudhir.radiomalayalam.playlist.PlaylistItem;

import java.util.List;

/**
 * Created by Arun on 10/19/2015.
 */
public class PlaylistListAdapter extends ArrayAdapter {

    private Context context;

    public PlaylistListAdapter(Context context, List<PlaylistItem> items) {
        super(context, android.R.layout.simple_list_item_1, items);
        this.context = context;
    }

    /**
     * Holder for the list items.
     */
    private class ViewHolder {
        TextView playListNameText;
        // TextView albumText;
    }

    /**
     * @param position vvcc
     * @param convertView ffgg
     * @param parent fffgfg
     * @return fgfg
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        PlaylistItem item = (PlaylistItem) getItem(position);
        View viewToUse = null;

        // This block exists to inflate the settings list item conditionally based on whether
        // we want to support a grid or list view.
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            boolean useList = true;
            if (useList) {
                viewToUse = mInflater.inflate(R.layout.playlistgriditem, null);
            } else {
                viewToUse = mInflater.inflate(R.layout.playlistgriditem, null);
            }
            // cache the view atrefacts into holder for efficiency
            holder = new ViewHolder();
            holder.playListNameText = (TextView) viewToUse.findViewById(R.id.playListName);
            viewToUse.setTag(holder);
        } else {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }

        holder.playListNameText.setText(item.getPlaylistName());
        return viewToUse;
    }
}
