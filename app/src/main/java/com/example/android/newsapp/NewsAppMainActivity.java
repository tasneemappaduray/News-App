package com.example.android.newsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class NewsAppMainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String LOG_TAG = NewsAppMainActivity.class.getName();

    /** Adapter for the list of news articles */
    private NewsAdapter mAdapter;

    /**URL for news data from the guardian API */
    private String NEWS_REQUEST_URL;

    /** Constant value for the news loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.*/
    private static final int NEWS_LOADER_ID = 1;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_app_main);

        /** Find a reference to the {@link ListView} in the layout*/
        ListView newsListView = findViewById(R.id.list);

        /** Create a new adapter that takes an empty list of news articles as input*/
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        /** Set the adapter on the {@link ListView} so the list can be populated in the user interface*/
        newsListView.setAdapter(mAdapter);


        /** Set an item click listener on the ListView, which sends an intent to a web browser
         * to open a website with more information about the selected news article.*/
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                /** Find the current news article that was clicked on*/
                News currentNews = mAdapter.getItem(position);

                /** Convert the String URL into a URI object (to pass into the Intent constructor)*/
                Uri newsUri = Uri.parse(currentNews.getUrl());

                /** Create a new intent to view the earthquake URI*/
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                /** Send the intent to launch a new activity*/
                startActivity(websiteIntent);
            }
        });

        mEmptyStateTextView = findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);


        /** Get a reference to the ConnectivityManager to check state of network connectivity*/
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        /** Get details on the currently active default data network*/
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        /** If there is a network connection, fetch data*/
        if (networkInfo != null && networkInfo.isConnected()) {
            /** Get a reference to the LoaderManager, in order to interact with loaders.*/
            android.support.v4.app.LoaderManager loaderManager = getSupportLoaderManager();
            /** Initialize the loader. Pass in the int ID constant defined above and pass in null for
             * the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
             * because this activity implements the LoaderCallbacks interface).*/
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            /** Otherwise, display error. First, hide loading indicator so error message will be visible*/
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            /**Update empty state with no connection error message*/
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
        Log.i(LOG_TAG, "News Activity onCreate() called ...");
    }

    @Override
    /** onCreateLoader instantiates and returns a new Loader for the given ID*/
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        /** getString retrieves a String value from the preferences. The second parameter is the default value for this preference.*/
        String articleCategory = sharedPrefs.getString(
                getString(R.string.article_category_key),
                getString(R.string.article_category_default)
        );

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("content.guardianapis.com")
                .appendPath("search")
                .appendQueryParameter("api-key", "182fdc09-7345-4eb9-b463-b36bbbc2a2be")
                .appendQueryParameter("sectionId", articleCategory)
                .appendQueryParameter("sectionName", articleCategory)
                .appendQueryParameter("show-tags", "contributor");
        NEWS_REQUEST_URL = builder.build().toString();
        return new NewsLoader(this, NEWS_REQUEST_URL);

    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        /** Hide loading indicator because the data has been loaded */
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        /** Set empty state text to display "No news found."*/
        mEmptyStateTextView.setText(R.string.no_news);
        /** Clear the adapter of previous news data*/
        mAdapter.clear();

        /** If there is a valid list of {@link News}, then add them to the adapter's
         * data set. This will trigger the ListView to update.*/
        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        /** Loader reset, so we can clear out our existing data.*/
        mAdapter.clear();
    }

    @Override
    /** This method initialize the contents of the Activity's options menu.*/
    public boolean onCreateOptionsMenu(Menu menu) {
        /** Inflate the Options Menu we specified in XML*/
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
