package com.example.android.news;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class FeedbackActivity extends AppCompatActivity {

    private EditText feedbackTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        setTitle("Contact Us");

        feedbackTextView = findViewById(R.id.feedback_text);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feedback, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.home:
                NavUtils.navigateUpFromSameTask(FeedbackActivity.this);
                return true;

            case R.id.send_feedback:

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto: gaganlokesh@gmail.com"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Sheets App Feedback");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Device: " + Build.MANUFACTURER + "-"+ Build.MODEL
                         + "\n" + "Android Version: "+ Build.VERSION.RELEASE + "\n"
                          + "\n" + feedbackTextView.getText().toString());
                startActivity(Intent.createChooser(emailIntent, "Send Email"));

                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
