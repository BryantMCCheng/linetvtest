package com.linetv.linetvtest.DB;

import android.net.Uri;

public class ContentDataList {
    public static final String AUTHORITY = "com.linetv.linetvtest";

    public static class DramaData {
        public static final Uri CONTACT_URI = Uri.parse("content://" + AUTHORITY + "/" + Table_Names.DRAMADATA);
        public static final String ID = "_id";
        public static final String DRAMA_ID = "drama_id";
        public static final String NAME = "name";
        public static final String TOTAL_VIEWS = "total_views";
        public static final String CREATED_AT = "created_at";
        public static final String THUMB = "thumb";
        public static final String RATING = "rating";
    }

    public interface Table_Names {
        String DRAMADATA = "dramadata";
    }
}
