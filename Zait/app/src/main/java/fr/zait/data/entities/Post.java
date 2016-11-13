package fr.zait.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {
    public String subreddit;
    public String title;
    public String author;
    public String text;
    public int points;
    public int numComments;
    public String permalink;
    public String url;
    public String domain;
    public String id;
    public long createdUtc;
    public String thumbnail;
    public boolean hasBeenSeen;

    public Post() {
    }

    public Post(Parcel in) {
        String[] data = new String[13];

        in.readStringArray(data);

        subreddit = data[0];
        title = data[1];
        author = data[2];
        points = Integer.valueOf(data[3]);
        numComments = Integer.valueOf(data[4]);
        permalink = data[5];
        url = data[6];
        domain = data[7];
        id = data[8];
        createdUtc = Long.valueOf(data[9]);
        thumbnail = data[10];
        hasBeenSeen = Boolean.getBoolean(data[11]);
        text = data[12];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{subreddit, title, author, String.valueOf(points), String.valueOf(numComments), permalink, url, domain, id, String.valueOf(createdUtc), thumbnail, String.valueOf(hasBeenSeen), text});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

}
