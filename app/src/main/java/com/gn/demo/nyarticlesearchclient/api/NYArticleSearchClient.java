package com.gn.demo.nyarticlesearchclient.api;

import android.util.Log;

import com.gn.demo.nyarticlesearchclient.adapter.GridAdapter;
import com.gn.demo.nyarticlesearchclient.model.NYArticle;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by workboard on 2/12/16.
 */
public class NYArticleSearchClient {

    public NYArticleSearchClient(){

    }

    /****
     * Return search result
     * @param query
     */
    public ArrayList<NYArticle> getSearchResult(String query){
        return null;
    }
}
