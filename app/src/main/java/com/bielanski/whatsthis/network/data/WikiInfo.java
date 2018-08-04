package com.bielanski.whatsthis.network.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WikiInfo {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("displaytitle")
    @Expose
    private String displaytitle;
    @SerializedName("namespace")
    @Expose
    private Namespace namespace;
    @SerializedName("titles")
    @Expose
    private Titles titles;
    @SerializedName("pageid")
    @Expose
    private Integer pageid;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("dir")
    @Expose
    private String dir;
    @SerializedName("revision")
    @Expose
    private String revision;
    @SerializedName("tid")
    @Expose
    private String tid;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("content_urls")
    @Expose
    private ContentUrls contentUrls;
    @SerializedName("api_urls")
    @Expose
    private ApiUrls apiUrls;
    @SerializedName("extract")
    @Expose
    private String extract;
    @SerializedName("extract_html")
    @Expose
    private String extractHtml;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplaytitle() {
        return displaytitle;
    }

    public void setDisplaytitle(String displaytitle) {
        this.displaytitle = displaytitle;
    }

    public Namespace getNamespace() {
        return namespace;
    }

    public void setNamespace(Namespace namespace) {
        this.namespace = namespace;
    }

    public Titles getTitles() {
        return titles;
    }

    public void setTitles(Titles titles) {
        this.titles = titles;
    }

    public Integer getPageid() {
        return pageid;
    }

    public void setPageid(Integer pageid) {
        this.pageid = pageid;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ContentUrls getContentUrls() {
        return contentUrls;
    }

    public void setContentUrls(ContentUrls contentUrls) {
        this.contentUrls = contentUrls;
    }

    public ApiUrls getApiUrls() {
        return apiUrls;
    }

    public void setApiUrls(ApiUrls apiUrls) {
        this.apiUrls = apiUrls;
    }

    public String getExtract() {
        return extract;
    }

    public void setExtract(String extract) {
        this.extract = extract;
    }

    public String getExtractHtml() {
        return extractHtml;
    }

    public void setExtractHtml(String extractHtml) {
        this.extractHtml = extractHtml;
    }

}




