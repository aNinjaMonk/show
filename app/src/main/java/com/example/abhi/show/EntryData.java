package com.example.abhi.show;

import android.graphics.Bitmap;

/**
 * Created by abhi on 20/01/15.
 */
public class EntryData {
    private String imageUrl;
    private String entryId;

    public EntryData(String imageUrl,String entryId){
        this.imageUrl = imageUrl;
        this.entryId = entryId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }
}
