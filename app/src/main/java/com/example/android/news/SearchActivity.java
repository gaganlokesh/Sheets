package com.example.android.news;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private android.support.v7.widget.Toolbar searchToolbar;
    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private ArrayList<News> mNews;
    private LoaderManager mLoaderManager;
    private ProgressBar searchLoadingIndicator;
    private EditText searchText;
    private int Loader_ID = 10;

    private static String requestUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        createToolbar();

        searchLoadingIndicator = findViewById(R.id.search_progress_bar);

        mRecyclerView = findViewById(R.id.search_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNews = new ArrayList<>();
        mAdapter = new NewsAdapter(this, mNews);

        mRecyclerView.setAdapter(mAdapter);

        searchText = findViewById(R.id.search_query);

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager mgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    requestUrl = buildStringUrl(searchText.getText().toString().trim());

                    initialiseLoader();

                    return true;
                }

                return false;
            }
        });

    }

    private void createToolbar(){

        searchToolbar = findViewById(R.id.search_tool_bar);
        setSupportActionBar(searchToolbar);

        if(getSupportActionBar()!= null){

            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initialiseLoader(){

        mLoaderManager = getLoaderManager();
        mLoaderManager.initLoader(Loader_ID, null, this);
        Loader_ID++;
    }

    private String buildStringUrl(String query){
        String builtString;
        query = query.replaceAll("\\s","%20");
        builtString = ("https://newsapi.org/v2/everything?q="+ query +"&language=en&sortBy=relevancy,publishedAt&pageSize=20&apiKey=" + Constants.API_KEY);

        return builtString;
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        mAdapter.clear();
        searchLoadingIndicator.setVisibility(View.VISIBLE);
        return new SearchLoader(this, requestUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> searchNews) {

        searchLoadingIndicator.setVisibility(View.GONE);

        if(mAdapter.getItemCount() == 0){

            if(searchNews != null && !searchNews.isEmpty()){

                mAdapter.addAll(searchNews);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {

        mAdapter.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//
//        requestUrl = getIntent().getStringExtra(Constants.SAVED_SOURCE);
//        initialiseLoader();
        super.onBackPressed();

        Log.i(SearchActivity.class.getSimpleName(),"Back pressed");
    }
}
