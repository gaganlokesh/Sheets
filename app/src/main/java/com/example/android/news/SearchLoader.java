package com.example.android.news;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Gagan Lokesh on 03-03-2018.
 */

public class SearchLoader extends AsyncTaskLoader<List<News>> {
    private static String mUrl;

    public SearchLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if(mUrl==null){
            return null;
        }

        List<News> mNews = QueryUtils.fetchNewsData(mUrl);
        return mNews;
    }
}
