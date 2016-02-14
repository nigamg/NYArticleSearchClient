package com.gn.demo.nyarticlesearchclient.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.gn.demo.nyarticlesearchclient.R;
import com.gn.demo.nyarticlesearchclient.adapter.GridAdapter;
import com.gn.demo.nyarticlesearchclient.model.NYArticle;
import com.gn.demo.nyarticlesearchclient.view.EndlessRecyclerViewScrollListener;
import com.gn.demo.nyarticlesearchclient.view.GridSpaceDecorator;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class NYArticleSearchActivity extends AppCompatActivity implements FilterFragment.OnFragmentInteractionListener {

    RecyclerView gridRecyclerView;
    GridAdapter adapter;
    ArrayList<NYArticle> articles = new ArrayList<>();

    private static final String apiRootUrl = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
    private AsyncHttpClient client;
    RequestParams requestParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nyarticle_search);

        gridRecyclerView = (RecyclerView) findViewById(R.id.gridItems);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        gridRecyclerView.setLayoutManager(staggeredGridLayoutManager);

        adapter = new GridAdapter(this, articles);
        gridRecyclerView.setAdapter(adapter);

        GridSpaceDecorator decorator = new GridSpaceDecorator(4);
        gridRecyclerView.addItemDecoration(decorator);


        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        gridRecyclerView.setItemAnimator(itemAnimator);

        // Add the scroll listener
        gridRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                customLoadMoreDataFromApi(page);
            }
        });

    }
    // Append more data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void customLoadMoreDataFromApi(int offset) {
        // Send an API request to retrieve appropriate data using the offset value as a parameter.
        // Deserialize API response and then construct new objects to append to the adapter
        // Add the new objects to the data source for the adapter
        //articles.addAll(moreItems);
        // For efficiency purposes, notify the adapter of only the elements that got changed
        // curSize will equal to the index of the first element inserted because the list is 0-indexed
       // int curSize = adapter.getItemCount();
        //adapter.notifyItemRangeInserted(curSize, items.size() - 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.ny_article_search:
                // User chose the "search" item..
                return true;

            case R.id.ny_search_filter:
                // User chose the "filter" action..
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.grid_menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.ny_article_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used


                if(query != null){
                    client = new AsyncHttpClient();
                    requestParams = new RequestParams();

                    requestParams.put("api-key", "56d5422c914f8280507735460fcf757e:1:74388174");
                    requestParams.put("page", 0);

                    requestParams.put("q", query);
                    try{
                        client.get(apiRootUrl, requestParams, new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                //super.onSuccess(statusCode, headers, response);

                                try {
                                    JSONArray articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                                    Log.d("DEBUG", articleJsonResults.toString());
                                    articles.addAll(NYArticle.fromJSONArray(articleJsonResults));

                                    gridRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));

                                    adapter = new GridAdapter(NYArticleSearchActivity.this, articles);
                                    gridRecyclerView.setAdapter(adapter);

                                    adapter.notifyDataSetChanged();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                            }
                        });
                    }catch (Exception e){

                    }
                }
                searchView.clearFocus();
                //Toast.makeText(getApplicationContext(), "Searching for " +query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });
        return super.onCreateOptionsMenu(menu);

    }

    /**
     * Item click event for search filter
     * @param item
     */
    public void showSearchFilter(MenuItem item) {

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        FilterFragment editTodoItemDialogFragmentDialog = FilterFragment.newInstance("Filter Search");

        editTodoItemDialogFragmentDialog.show(fm, "fragment_filter");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
