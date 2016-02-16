package com.gn.demo.nyarticlesearchclient.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by workboard on 2/12/16.
 */
public class NYArticle implements Parcelable {

    private String webUrl;
    private String headLine;
    private String thumbNail;

    public NYArticle(JSONObject jsonObject) {
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headLine = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multiMedia = jsonObject.getJSONArray("multimedia");
            if(multiMedia.length() > 0 ){
                JSONObject multiMediaJson =  multiMedia.getJSONObject(0);
                this.thumbNail = "http://www.nytimes.com/" + multiMediaJson.getString("url");
            }else{
                this.thumbNail = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected NYArticle(Parcel in) {
        webUrl = in.readString();
        headLine = in.readString();
        thumbNail = in.readString();
    }

    public static final Creator<NYArticle> CREATOR = new Creator<NYArticle>() {
        @Override
        public NYArticle createFromParcel(Parcel in) {
            return new NYArticle(in);
        }

        @Override
        public NYArticle[] newArray(int size) {
            return new NYArticle[size];
        }
    };

    public  static ArrayList<NYArticle> fromJSONArray(JSONArray array){
        ArrayList<NYArticle> results = new ArrayList<>();
        for (int i = 0 ; i < array.length(); i++){
            try{
                results.add(new NYArticle(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getHeadLine() {
        return headLine;
    }

    public void setHeadLine(String headLine) {
        this.headLine = headLine;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(webUrl);
        dest.writeString(headLine);
        dest.writeString(thumbNail);
    }
}
