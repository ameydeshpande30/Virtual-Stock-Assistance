package com.wce.barclays.model;

public class Sample {
    public String getMovie_Name() {
        return Movie_Name;
    }

    public void setMovie_Name(String movie_Name) {
        Movie_Name = movie_Name;
    }

    public Sample(String movie_Name) {
        Movie_Name = movie_Name;
    }

    String Movie_Name;

    @Override
    public String toString() {
        return Movie_Name;
    }
}
