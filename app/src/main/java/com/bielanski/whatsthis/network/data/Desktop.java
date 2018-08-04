package com.bielanski.whatsthis.network.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Desktop {

    @SerializedName("page")
    @Expose
    private String page;
    @SerializedName("revisions")
    @Expose
    private String revisions;
    @SerializedName("edit")
    @Expose
    private String edit;
    @SerializedName("talk")
    @Expose
    private String talk;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getRevisions() {
        return revisions;
    }

    public void setRevisions(String revisions) {
        this.revisions = revisions;
    }

    public String getEdit() {
        return edit;
    }

    public void setEdit(String edit) {
        this.edit = edit;
    }

    public String getTalk() {
        return talk;
    }

    public void setTalk(String talk) {
        this.talk = talk;
    }

}
