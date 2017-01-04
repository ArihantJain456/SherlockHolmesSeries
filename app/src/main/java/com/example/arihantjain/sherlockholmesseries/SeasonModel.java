package com.example.arihantjain.sherlockholmesseries;

import java.io.Serializable;

/**
 * Created by Arihant Jain on 12/25/2016.
 */

public class SeasonModel implements Serializable{
    private String seasonDate;
    private int seasonNo;
    private String seasonImage;

    public String getSeasonDate() {
        return seasonDate;
    }

    public String getSeasonImage() {
        return seasonImage;
    }

    public void setSeasonImage(String seasonImage) {
        this.seasonImage = seasonImage;
    }

    public void setSeasonDate(String seasonDate) {
        this.seasonDate = seasonDate;
    }

    public int getSeasonNo() {
        return seasonNo;
    }

    public void setSeasonNo(int seasonNo) {
        this.seasonNo = seasonNo;
    }
}
