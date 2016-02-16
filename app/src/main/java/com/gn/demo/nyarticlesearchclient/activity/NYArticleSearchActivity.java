package com.gn.demo.nyarticlesearchclient.activity;

import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import com.gn.demo.nyarticlesearchclient.R;
import com.gn.demo.nyarticlesearchclient.adapter.GridAdapter;
import com.gn.demo.nyarticlesearchclient.model.NYArticle;
import com.gn.demo.nyarticlesearchclient.model.SearchFilter;

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

public class NYArticleSearchActivity extends AppCompatActivity implements SearchFilterFragment.OnFragmentInteractionListener{

    RecyclerView gridRecyclerView;
    GridAdapter adapter;
    ArrayList<NYArticle> articles;

    SearchFilter sF = new SearchFilter();

    private static final String apiRootUrl = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
    private AsyncHttpClient client;
    RequestParams requestParams;
    StringBuilder fqValue;

    /***
     * Cached query for subsequent requests
     */
    private String cachedQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nyarticle_search);

        gridRecyclerView = (RecyclerView) findViewById(R.id.gridItems);

        articles = new ArrayList<>();

        gridRecyclerView.setVisibility(View.GONE);

        adapter = new GridAdapter(this, articles);
        gridRecyclerView.setAdapter(adapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        gridRecyclerView.setLayoutManager(staggeredGridLayoutManager);

        gridRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.i("DEBUG" ,"==================loading more items =================");
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                customLoadMoreDataFromApi(page);
            }
        });

        GridSpaceDecorator decorator = new GridSpaceDecorator(4);
        gridRecyclerView.addItemDecoration(decorator);


        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(500);
        itemAnimator.setRemoveDuration(500);
        gridRecyclerView.setItemAnimator(itemAnimator);
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


        int currSize = adapter.getItemCount();
        fetchMoreData(offset, currSize);
    }


    /***
     * fetches more data, on top of already built client and view
     * @param page
     */
    public void fetchMoreData(int page, final int curSize){

        if(client != null && requestParams != null){
            requestParams.put("page", page);

            requestParams.put("q", cachedQuery);
            if(sF.getAtleastOneValueIsSet()){
                fqValue = new StringBuilder();
                try{
                    if(sF.getAtleastOneValueIsSet()){


                        if(sF.getSortOrder() != null){
                            requestParams.put("sort_order", sF.getSortOrder());
                        }

                        if(sF.getDeskValues() != null){
                            requestParams.put("fq", String.format("news_desk:(%s)", sF.getDeskValues()));
                        }

                        if(sF.getBeginDate() != null){
                            requestParams.put("begin_date", sF.getBeginDate());
                        }

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                requestParams.put("fq", fqValue.toString());
            }
            Log.i("DEBUG" ,"Request param string value "+requestParams.toString());
            try{
                client.get(apiRootUrl, requestParams, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        //super.onSuccess(statusCode, headers, response);

                        try {
                            JSONArray articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                            Log.d("DEBUG", articleJsonResults.toString());
                            articles.addAll(NYArticle.fromJSONArray(articleJsonResults));

                            //gridRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));

                            /*adapter = new GridAdapter(NYArticleSearchActivity.this, articles);
                            gridRecyclerView.setAdapter(adapter);*/

                            adapter.notifyItemRangeInserted(curSize, articles.size() - 1);

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
        if(searchItem != null){

            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // perform query here
                    // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used

                    cachedQuery = query;

                    if(query != null){
                        articles = new ArrayList<>();

                        client = new AsyncHttpClient();
                        requestParams = new RequestParams();

                        requestParams.put("api-key", "56d5422c914f8280507735460fcf757e:1:74388174");
                        requestParams.put("page", 0);

                        requestParams.put("q", query);
                        if(sF.getAtleastOneValueIsSet()){
                            fqValue = new StringBuilder();
                            try{
                                if(sF.getAtleastOneValueIsSet()){
                                    if(sF.getSortOrder() != null){
                                        requestParams.put("sort_order", sF.getSortOrder());
                                    }

                                    if(sF.getDeskValues() != null){
                                        requestParams.put("fq", String.format("news_desk:(%s)", sF.getDeskValues()));
                                    }

                                    if(sF.getBeginDate() != null){
                                        requestParams.put("begin_date", sF.getBeginDate());
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            requestParams.put("fq", fqValue.toString());
                        }
                        Log.i("DEBUG" ,"Request param string value "+requestParams.toString());
                        try{
                            client.get(apiRootUrl, requestParams, new JsonHttpResponseHandler(){
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    //super.onSuccess(statusCode, headers, response);

                                    try {
                                        JSONArray articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                                        Log.d("DEBUG", articleJsonResults.toString());
                                        articles.addAll(NYArticle.fromJSONArray(articleJsonResults));

                                        //gridRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));

                                        /*adapter = new GridAdapter(NYArticleSearchActivity.this, articles);
                                        gridRecyclerView.setAdapter(adapter);*/

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
        }
        return super.onCreateOptionsMenu(menu);

    }

    /**
     * Item click event for search filter
     * @param item
     */
    public void showSearchFilter(MenuItem item) {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        SearchFilterFragment searchFilterFragment = SearchFilterFragment.newInstance("Filter Your Search", sF);
        searchFilterFragment.show(fm, "fragment_search_filter");

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
