package com.example.android.news;

/**
 * Created by Gagan Lokesh on 14-01-2018.
 */

public class News {


    public String mTitle,mContent,mUrl, mImage, mDate;

    public News(String title, String url, String content, String Date){
     mTitle = title;
     mUrl = url;
     mContent = content;
     mDate = Date;
    }

    public News(String title, String url, String Image, String content, String Date){
        mTitle = title;
        mUrl = url;
        mImage = Image;
        mContent = content;
        mDate = Date;

    }

    public String getContentTitle(){
        return mTitle;
    }

    public String getContent(){
        return mContent;
    }

    public String getUrl(){
        return mUrl;
    }

    String getImageUrl(){
        return mImage;
    }

    String getDate(){
        return mDate;
    }

}
