package com.bielanski.whatsthis.database.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "wiki", indices = {@Index(value = {"title"})})
public class WikiEntity implements Parcelable{
    @PrimaryKey
    @NonNull
    String title;
    String description;

    public WikiEntity(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    protected WikiEntity(Parcel in) {
        title = in.readString();
        description = in.readString();
    }

    public static final Creator<WikiEntity> CREATOR = new Creator<WikiEntity>() {
        @Override
        public WikiEntity createFromParcel(Parcel in) {
            return new WikiEntity(in);
        }

        @Override
        public WikiEntity[] newArray(int size) {
            return new WikiEntity[size];
        }
    };

    @Override
    public String toString() {
        String desc = "Wiki " +
                "title " + title +
                " description " + description;

        return desc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
    }
}
