package com.example.android.news;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DetailActivity extends AppCompatActivity{

    private Toolbar detailToolbar;
    private String contentUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        createToolbar();
        initialize();

        initializeButton();
    }

    private void createToolbar() {

        detailToolbar = findViewById(R.id.detail_tool_bar);
        setSupportActionBar(detailToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initialize(){


        String title = getIntent().getStringExtra(Constants.INTENT_TITLE);
        String content = getIntent().getStringExtra(Constants.INTENT_DESCRIPTION);
        contentUrl = getIntent().getStringExtra(Constants.INTENT_URL);
        String imageUrl = getIntent().getStringExtra(Constants.INTENT_IMAGE_URL);
        String date = getIntent().getStringExtra(Constants.INTENT_DATE);

        TextView titleTextView = findViewById(R.id.detail_title);
        titleTextView.setText(title);

        String expectedDateFormat = getDateFromString(date,"yyyy-MM-dd'T'HH:mm:ss'Z'","LLL dd, yyyy");
        TextView dateView = findViewById(R.id.detail_date);
        dateView.setText(expectedDateFormat);

        TextView contentTextView = findViewById(R.id.detail_content);

        if(content.endsWith("View Entire Post ›")){
            content = content.replaceAll("View Entire Post ›", "");
        }
        contentTextView.setText(content);

        ImageView contentImage = findViewById(R.id.detail_image);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.DATA);

        Glide.with(this)
                .load(imageUrl)
                .apply(options)
                .into(contentImage);
    }

    private void initializeButton(){

        Button detailButton = findViewById(R.id.detailButton);

        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String detailurl = getIntent().getStringExtra(Constants.INTENT_URL);
//
//                Uri uri = Uri.parse(detailurl);
//                Intent webIntent = new Intent(Intent.ACTION_VIEW,uri);
//                startActivity(webIntent);

                Intent webViewIntent = new Intent(DetailActivity.this, WebviewActivity.class);
                webViewIntent.putExtra(Constants.INTENT_WEBURL, detailurl);
                startActivity(webViewIntent);
            }
        });
    }


    private void shareNews(){

        Intent sharingIntent = new Intent();
        sharingIntent.setAction(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Check this out! Sent from Sheets App.\n" + Uri.parse(contentUrl));
        startActivity(Intent.createChooser(sharingIntent, "Share with"));

    }

    public static String getDateFromString(String dateInString, String actualformat, String exceptedFormat) {
        SimpleDateFormat form = new SimpleDateFormat(actualformat);

        String formatedDate = null;
        Date date;
        try {
            date = form.parse(dateInString);
            SimpleDateFormat postFormater = new SimpleDateFormat(exceptedFormat);
            formatedDate = postFormater.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatedDate;
    }
     /*
            * Override the Up/Home Button
            * */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home :
                onBackPressed();
                return true; //"return true" instead of "break"

            case R.id.Share :
                shareNews();
                return true;

             default: break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Override on back pressed

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
