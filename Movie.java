package model;

/**
 * Represents a Movie entity.
 *
 * Design Decision: rating stored as double to allow fine-grained
 * scoring (e.g., 8.4). Immutable movieId ensures stable identity.
 */
public class Movie {

    private final int movieId;
    private String title;
    private String genre;
    private double rating;

    public Movie(int movieId, String title, String genre, double rating) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.rating = rating;
    }

    public int getMovieId() { return movieId; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public double getRating() { return rating; }

    public void setTitle(String title) { this.title = title; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setRating(double rating) { this.rating = rating; }

    @Override
    public String toString() {
        return String.format("Movie{id=%d, title='%s', genre='%s', rating=%.1f}",
                movieId, title, genre, rating);
    }
}
