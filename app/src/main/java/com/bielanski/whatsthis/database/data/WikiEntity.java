package com.bielanski.whatsthis.database.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Arrays;

@Entity(tableName = "wiki", indices = {@Index(value = {"title"})})
public class WikiEntity implements Parcelable{
    @PrimaryKey
    @NonNull
    private String title;
    private String description;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    public WikiEntity(String title, String description, byte[] image) {
        this.title = title;
        this.description = description;
        this.image = image;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {

        System.arraycopy(image, 0, this.image, 0, image.length);
        //Arrays.copyOf(image, image.length);
        //this.image = image;
    }
    protected WikiEntity(Parcel in) {
        title = in.readString();
        description = in.readString();
        image = in.createByteArray();
        //in.readByteArray(image);
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
        parcel.writeByteArray(image);
    }
}
