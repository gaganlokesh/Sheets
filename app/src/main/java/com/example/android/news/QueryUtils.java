package com.example.android.news;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gagan Lokesh on 14-01-2018.
 */

public final class QueryUtils {

private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {

    }

    public static List<News> fetchNewsData(String requestUrl){


        URL url = makeUrl(requestUrl);

        String JsonResponse = null;

        try{
            JsonResponse = makeHttpRequest(url);
        }
        catch (IOException e){
            Log.e(LOG_TAG,"Error Making Http Request.",e);
        }

        List<News> newsData = extractDataFromJson(JsonResponse);

        return newsData;

    }

    private static URL makeUrl(String url){

        URL newUrl = null;

        try{
            newUrl = new URL(url);
        }
        catch (MalformedURLException e){
            Log.e(LOG_TAG,"Error Building Url.",e);

        }
        return newUrl;
    }

    private static String makeHttpRequest(URL url) throws IOException{

        String jsonResponse = "";

        if(url==null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDefaultUseCaches(true);
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error Response Code =" + urlConnection.getResponseCode());
            }
        }
        catch(IOException e){
            Log.e(LOG_TAG,"Error retrieving Json results",e);
        }
        finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }

            if (inputStream!=null){
                inputStream.close();
            }
        }
        return jsonResponse;

    }

    private static String readFromStream(InputStream inputStream) throws IOException{

        StringBuilder output = new StringBuilder();


            if (inputStream != null) {

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = bufferedReader.readLine();

                while (line != null) {
                    output.append(line);
                    line = bufferedReader.readLine();
                }

            }


            return output.toString();
    }



    private static List<News> extractDataFromJson(String JSONdata) {


        if(TextUtils.isEmpty(JSONdata)){
            return null;
        }

        List<News> newsArray = new ArrayList<>();

        try {

            JSONObject baseNewsData = new JSONObject(JSONdata);

//            JSONObject baseObject = baseNewsData.getJSONObject("response");

            JSONArray jsonarray = baseNewsData.getJSONArray("articles");


            for(int i=0;i < jsonarray.length();i++) {

                JSONObject jsonObject = jsonarray.getJSONObject(i);

                String Title = jsonObject.getString("title");

                String Content = jsonObject.getString("description");
                String Url = jsonObject.getString("url");
                String Image = jsonObject.getString("urlToImage");
                String date = jsonObject.getString("publishedAt");

                News currentNews = new News(Title,Url,Image,Content,date);

                newsArray.add(currentNews);
            }

        }
        catch (JSONException e){

            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);

        }

        return newsArray;
    }


}