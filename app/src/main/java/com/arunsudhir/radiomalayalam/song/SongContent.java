package com.arunsudhir.radiomalayalam.song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class SongContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<SongItem> ITEMS = new ArrayList<SongItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, SongItem> ITEM_MAP = new HashMap<String, SongItem>();

    static {
        // Add 3 sample items.
        addItem(new SongItem("1", "Item 1"));
        addItem(new SongItem("2", "Item 2"));
        addItem(new SongItem("3", "Item 3"));
    }

    private static void addItem(SongItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * An item representing a song
     */
    public static class SongItem {
        public String id;
        public String content;

        public SongItem(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
