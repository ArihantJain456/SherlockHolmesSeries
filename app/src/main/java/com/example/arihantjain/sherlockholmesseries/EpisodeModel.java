package com.example.arihantjain.sherlockholmesseries;

import java.io.Serializable;

/**
 * Created by Arihant Jain on 12/22/2016.
 */

public class EpisodeModel implements Serializable{
    private String Title;
    private String Episode;
    private String duration;
    private String ratings;
    private String image;
    public static String video_Id;
    public static int season = 0;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public void setVideo_Id(String video_Id) {
        this.video_Id = video_Id;
    }

    private String summary;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getEpisode() {
        return Episode;
    }

    public void setEpisode(String episode) {
        Episode = episode;
    }
}
