package com.example.myapp.firebasevideoplayer;

public class VideoDetailModel {
    private String title;
    private String videoUri;

    public VideoDetailModel(String title, String videoUri) {
        this.title = title;
        this.videoUri = videoUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }
}
