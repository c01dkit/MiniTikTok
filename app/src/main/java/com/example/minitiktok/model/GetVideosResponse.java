package com.example.minitiktok.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GetVideosResponse {

    @SerializedName("success") public boolean success;
    @SerializedName("feeds") public List<Video> videos;
    @SerializedName("error") public String error;

    public List<Video> getVideos() {
        return videos;
    }
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "success=" + success +
                ", error=" + error +
                '}';
    }
}
