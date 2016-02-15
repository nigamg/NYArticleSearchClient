package com.gn.demo.nyarticlesearchclient.model;

/**
 * Created by workboard on 2/14/16.
 */
public class SearchFilter {

    String beginDate;
    String sortOrder;
    String deskValues;
    boolean atleastOneValueIsSet=false;

    public boolean getAtleastOneValueIsSet() {
        return atleastOneValueIsSet;
    }

    public void setAtleastOneValueIsSet(boolean atleastOneValueIsSet) {
        this.atleastOneValueIsSet = atleastOneValueIsSet;
        this.setAtleastOneValueIsSet(true);
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
        this.setAtleastOneValueIsSet(true);
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getDeskValues() {
        return deskValues;
    }

    public void setDeskValues(String deskValues) {
        this.deskValues = deskValues;
        this.setAtleastOneValueIsSet(true);
    }
}
