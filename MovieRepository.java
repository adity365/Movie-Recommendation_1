package repository;

import model.Movie;
import java.util.*;
import java.util.stream.Collectors;

/**
 * In-memory movie repository acting as the data access layer.
 *
 * Design Decision: Separated from the service layer to follow the
 * Repository Pattern (SRP). Swap this for a DB-backed implementation
 * later without touching business logic.
 *
 * Stores movies indexed by genre for fast genre-based lookups.
 */
public class MovieRepository {

    // Primary store: movieId → Movie
    private final Map<Integer, Movie> movieStore = new HashMap<>();

    // Secondary index: genre → List<Movie> for O(1) genre lookup
    private final Map<String, List<Movie>> genreIndex = new HashMap<>();

    public void addMovie(Movie movie) {
        movieStore.put(movie.getMovieId(), movie);
        genreIndex.computeIfAbsent(movie.getGenre(), k -> new ArrayList<>())
                  .add(movie);
    }

    public Optional<Movie> findById(int movieId) {
        return Optional.ofNullable(movieStore.get(movieId));
    }

    /** Returns movies matching any of the supplied genres, sorted by rating desc. */
    public List<Movie> findByGenres(List<String> genres) {
        return genres.stream()
                .flatMap(genre -> genreIndex.getOrDefault(genre, Collections.emptyList()).stream())
                .distinct()
                .sorted(Comparator.comparingDouble(Movie::getRating).reversed())
                .collect(Collectors.toList());
    }

    public List<Movie> getAllMovies() {
        return new ArrayList<>(movieStore.values());
    }

    public int count() {
        return movieStore.size();
    }
}
