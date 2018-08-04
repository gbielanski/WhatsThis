package com.bielanski.whatsthis.network.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiUrls {

    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("metadata")
    @Expose
    private String metadata;
    @SerializedName("references")
    @Expose
    private String references;
    @SerializedName("media")
    @Expose
    private String media;
    @SerializedName("edit_html")
    @Expose
    private String editHtml;
    @SerializedName("talk_page_html")
    @Expose
    private String talkPageHtml;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getEditHtml() {
        return editHtml;
    }

    public void setEditHtml(String editHtml) {
        this.editHtml = editHtml;
    }

    public String getTalkPageHtml() {
        return talkPageHtml;
    }

    public void setTalkPageHtml(String talkPageHtml) {
        this.talkPageHtml = talkPageHtml;
    }

}
