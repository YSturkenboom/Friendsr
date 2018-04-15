package com.ananasco.friendsr;

import java.io.Serializable;

/**
 * Created by yuri on 10-4-18.
 */

public class Friend implements Serializable {
    private String name, bio;
    private int drawableId, distance;
    private float rating;
    private Boolean liked, superLiked;

    public Friend(String name, String bio, int drawableId) {
        this.name = name;
        this.bio = bio;
        this.drawableId = drawableId;

        // defaults to false
        this.liked = Boolean.FALSE;
        this.superLiked = Boolean.FALSE;

        // distance is instantiated randomly the first time the Firend is created
        this.distance = (int) Math.round(20 * Math.random());
    }

    public Boolean getLiked() {
        return liked;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public Boolean getSuperLiked() {
        return superLiked;
    }

    public void setSuperLiked(Boolean superLiked) {
        this.superLiked = superLiked;
    }

    public int getDistance() {
        return distance;
    }
}