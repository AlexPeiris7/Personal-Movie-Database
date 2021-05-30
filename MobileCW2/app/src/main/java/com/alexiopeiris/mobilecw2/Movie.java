package com.alexiopeiris.mobilecw2;

public class Movie {

    private String title;
    private int year;
    private String director;
    private String actors;
    private int rating;
    private String review;
    private String favourite;

    public Movie(String title, int year, String director, String actors, int rating, String review, String favourite) {
        this.title = title;
        this.year = year;
        this.director = director;
        this.actors = actors;
        this.rating = rating;
        this.review = review;
        this.favourite = favourite;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getFavourite() { return favourite; }

    public void setFavourite(String favourite) { this.favourite = favourite; }

}
