package com.example.android.news;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private NewsAdapter mAdapter;
    private ArrayList<News> mNews;
    private List<News> newNewsData;
    private Drawer mdrawer;
    private Toolbar mtoolbar;
    private TextView toolBarTitle;
    private AccountHeader mAccountHeader;
    private LoaderManager mLoaderManager;
    private FloatingActionButton floatingActionButton;
    private ProgressBar loadingIndicator;
    private Parcelable listState;
    private RecyclerView mRecyclerView;
    private TextView internetTextView;



//    private static final String requestUrl = "http://content.guardianapis.com/search?api-key=test";
//    private static String requestUrl = "https://newsapi.org/v2/top-headlines?country=in&apiKey=5bb267fb21884b68a97573a19b3add11";
    private static final String BASE_URL = "https://newsapi.org/v2/";
    private String requestUrl;
    private static final int NEWS_LOADER_ID = 0;
    private String Source;
    private static final String[] SOURCE_ARRAY = {
            "bbc-news",                                          /*General*/
            "ars-technica,the-next-web,wired",                       /*Technology*/
            "ign,buzzfeed,entertainment-weekly,mashable", /*Entertainment*/
            "espn,espn-cric-info",                              /*Sports*/
            "bloomberg,business-insider,cnbc,the-economist",         /*Business*/
            "national-geographic,new-scientist,next-big-future",     /*Science*/
            "medical-news-today"                                     /*Health*/
    };


    private int FabButtonLoaderId = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity_main);


        createToolbar();

        mRecyclerView = findViewById(R.id.list_item_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNews = new ArrayList<>();
        mAdapter = new NewsAdapter(this, mNews);

        mRecyclerView.setAdapter(mAdapter);


        toolBarTitle.setText(R.string.default_toolbar_title);  //Assigning Default title to toolbar on startup

        floatingActionButton = findViewById(R.id.actionButton);
        loadingIndicator = findViewById(R.id.loading_indicator);

        Source = SOURCE_ARRAY[0];
        requestUrl = stringBuilder(Source);

            initialiseLoader(NEWS_LOADER_ID);

        createDrawer(savedInstanceState, mtoolbar);

    }


    private void initialiseLoader(int loaderID){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected())
        {
        mLoaderManager = getLoaderManager();
        mLoaderManager.initLoader(loaderID,null,this);

        }
        else {
            loadingIndicator.setVisibility(View.GONE);
            internetTextView = findViewById(R.id.Internet_textView);

            internetTextView.setVisibility(View.VISIBLE);
        }
    }

    private void createToolbar(){
        mtoolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(mtoolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolBarTitle = findViewById(R.id.tool_bar_title);

    }

    private String stringBuilder(String source){

        String builtUrl;

        builtUrl = (BASE_URL + "everything?" + "sources="+ source + "&pageSize=10" + "&sortBy=PublishedAt" + "&apiKey=" + Constants.API_KEY);

        Log.i(MainActivity.class.getSimpleName(),"URL = " + builtUrl);
        return builtUrl;
    }
//
//
    private void createDrawer(Bundle savedInstanceState, final Toolbar toolbar){

        PrimaryDrawerItem item0 = new PrimaryDrawerItem().withIdentifier(1).withIcon(R.drawable.ic_home)
                .withName("Home");

        SectionDrawerItem item1 = new SectionDrawerItem().withDivider(true).withName("CATEGORIES");

        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("Technology")
                .withIcon(R.drawable.ic_technology);

        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName("Entertainment")
                .withIcon(R.drawable.ic_entertainment);

        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName("Sports")
                .withIcon(R.drawable.ic_sport);

        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withName("Business")
                .withIcon(R.drawable.ic_business);

        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(6).withName("Science")
                .withIcon(R.drawable.ic_science);

        PrimaryDrawerItem item7 = new PrimaryDrawerItem().withIdentifier(7).withName("Health")
                .withIcon(R.drawable.ic_health);

//        SecondaryDrawerItem item8 = new SecondaryDrawerItem().withIdentifier(8).withName("MORE INFO")
//                .withSelectable(false);

        SectionDrawerItem item8 = new SectionDrawerItem().withDivider(true).withName("MORE INFO");
        PrimaryDrawerItem item9 = new PrimaryDrawerItem().withIdentifier(8).withName("About")
                .withIcon(R.drawable.ic_about);

        PrimaryDrawerItem item10 = new PrimaryDrawerItem().withIdentifier(9).withName("Contact Us")
                .withIcon(R.drawable.ic_feedback);


        mAccountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.img_swimming)
//                .withSavedInstance(savedInstanceState)
                .build();

        mdrawer = new DrawerBuilder()
                .withAccountHeader(mAccountHeader)
                .withActivity(this)
                .withToolbar(toolbar)
                .withSelectedItem(0)
                .addDrawerItems(item0,item1,item2,item3,item4,item5,item6,item7,item8,item9,item10)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        int selected = (int) (long) drawerItem.getIdentifier();

                        switch (selected){

                            case 1 :
                                mAdapter.clear();
                                Source = SOURCE_ARRAY[0];
                                requestUrl = stringBuilder(Source);
                                initialiseLoader(selected);
                                toolBarTitle.setText(R.string.app_name);
                                break;

                            case 2 :
                                mAdapter.clear();
                                Source = SOURCE_ARRAY[1];
                                requestUrl = stringBuilder(Source);
                                initialiseLoader(selected);
                                toolBarTitle.setText(((Nameable)drawerItem).getName().getText(MainActivity.this));
                                break;

                            case 3 :
                                mAdapter.clear();
                                Source = SOURCE_ARRAY[2];
                                requestUrl = stringBuilder(Source);
                                initialiseLoader(selected);
                                toolBarTitle.setText(((Nameable)drawerItem).getName().getText(MainActivity.this));
                                break;

                            case 4 :
                                mAdapter.clear();
                                Source = SOURCE_ARRAY[3];
                                requestUrl = stringBuilder(Source);
                                initialiseLoader(selected);
                                toolBarTitle.setText(((Nameable)drawerItem).getName().getText(MainActivity.this));
                                break;

                            case 5 :
                                mAdapter.clear();
                                Source = SOURCE_ARRAY[4];
                                requestUrl = stringBuilder(Source);
                                initialiseLoader(selected);
                                toolBarTitle.setText(((Nameable)drawerItem).getName().getText(MainActivity.this));
                                break;

                            case 6 :
                                mAdapter.clear();
                                Source = SOURCE_ARRAY[5];
                                requestUrl = stringBuilder(Source);
                                initialiseLoader(selected);
                                toolBarTitle.setText(((Nameable)drawerItem).getName().getText(MainActivity.this));
                                break;

                            case 7 :
                                mAdapter.clear();
                                Source = SOURCE_ARRAY[6];
                                requestUrl = stringBuilder(Source);
                                initialiseLoader(selected);
                                toolBarTitle.setText(((Nameable)drawerItem).getName().getText(MainActivity.this));
                                break;

                            case 8 :
                                Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
                                startActivity(aboutIntent);

                                break;

                            case 9:
                                Intent feedbackIntent = new Intent(MainActivity.this, FeedbackActivity.class);
                                startActivity(feedbackIntent);
                                break;

                            default: break;
                        }
                        return false;

                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

    }






    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        mAdapter.clear();
        loadingIndicator.setVisibility(View.VISIBLE);
        return new NewsLoader(this, requestUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {

        loadingIndicator.setVisibility(View.GONE);

        if(mAdapter.getItemCount()==0) //IMPORTANT - Data should be added to the adapter only if it's empty, else duplication occurs.
        {
            if (news != null & !news.isEmpty()) {

                mAdapter.addAll(news);
            }
        }

        floatingActionButton.setVisibility(View.VISIBLE);

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {

        mAdapter.clear();
    }

    //floating button click handling
    public void fabClick(View v){

        mAdapter.clear();
        initialiseLoader(FabButtonLoaderId);
        FabButtonLoaderId++;
    }


    //----------Menu------------


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_search:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState = mdrawer.saveInstanceState(outState);

        outState = mAccountHeader.saveInstanceState(outState);

        super.onSaveInstanceState(outState);

        listState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(Constants.RECYCLER_LIST_STATE, listState);
        outState.putString(Constants.SOURCE, Source);
        outState.putString(Constants.TITLE_STATE, toolBarTitle.getText().toString());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null) {

            Source = savedInstanceState.getString(Constants.SOURCE);
            createToolbar();
            listState = savedInstanceState.getParcelable(Constants.RECYCLER_LIST_STATE);
            toolBarTitle.setText(savedInstanceState.getString(Constants.TITLE_STATE));
            createDrawer(savedInstanceState, mtoolbar);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(listState != null){
            mRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }


}
