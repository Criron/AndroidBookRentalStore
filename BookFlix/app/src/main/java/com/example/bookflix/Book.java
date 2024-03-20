package com.example.bookflix;

public class Book {

    public Book() {
    }

    /*
    * constructor
    * */
    public Book(String authors, float average_rating, String categories, String description, long isbn13, int num_pages, int published_year, int ratings_count, String thumbnail, String title) {
        this.authors = authors;
        this.average_rating = average_rating;
        this.categories = categories;
        this.description = description;
        this.isbn13 = isbn13;
        this.num_pages = num_pages;
        this.published_year = published_year;
        this.ratings_count = ratings_count;
        this.thumbnail = thumbnail;
        this.title = title;
    }

    String authors;
    float average_rating;
    String categories;
    String description;
    long isbn13;
    int num_pages;
    int published_year;
    int ratings_count;
    String thumbnail;
    String title;

    public float getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(float average_rating) {
        this.average_rating = average_rating;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(long isbn13) {
        this.isbn13 = isbn13;
    }

    public int getNum_pages() {
        return num_pages;
    }

    public void setNum_pages(int num_pages) {
        this.num_pages = num_pages;
    }

    public int getPublished_year() {
        return published_year;
    }

    public void setPublished_year(int published_year) {
        this.published_year = published_year;
    }

    public int getRatings_count() {
        return ratings_count;
    }

    public void setRatings_count(int ratings_count) {
        this.ratings_count = ratings_count;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
