package com.cms.gamehub;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<GameNews>>
{
    private static final String REQUEST_URL = "https://content.guardianapis.com/search";
    private static final String API_KEY = "e39134db-4f5f-413c-9478-2ed4f4eac06c";
    private static final int LOADER_ID = 1;

    private GameNewsAdapter mAdapter;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAdapter = new GameNewsAdapter(this, new ArrayList<GameNews>());

        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(mAdapter);
        mEmptyStateTextView = findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyStateTextView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                GameNews currentGN = mAdapter.getItem(position);
                Uri gameNewsUri = Uri.parse(currentGN.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, gameNewsUri);
                if (websiteIntent.resolveActivity(getPackageManager()) != null)         //check if the user's device has an app that can handle this intent
                {
                    startActivity(websiteIntent);
                }
            }
        });


        ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if(activeNetwork != null && activeNetwork.isConnectedOrConnecting())
        {
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(LOADER_ID, null, this);
        }
        else
        {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            Toast.makeText(this, "Setting Page is not yet ready.", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public Loader<List<GameNews>> onCreateLoader(int i, Bundle bundle)
    {
        Uri baseUri = Uri.parse(REQUEST_URL);

        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("section", "games");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("page-size", "25");
        uriBuilder.appendQueryParameter("api-key", API_KEY);

        return new GameNewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<GameNews>> loader, List<GameNews> gameNewsList)
    {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_news);
        mAdapter.clear();

        if (gameNewsList != null && !gameNewsList.isEmpty())
        {
            mAdapter.addAll(gameNewsList);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<GameNews>> loader)
    {
        mAdapter.clear();
    }
}
