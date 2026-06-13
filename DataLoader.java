package util;

import model.Movie;
import model.User;
import repository.MovieRepository;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class for seeding in-memory data.
 *
 * Design Decision: Kept separate from Main.java to keep the entry
 * point readable. In production this would be replaced by DB seed
 * scripts or a migration tool (e.g., Flyway).
 */
public class DataLoader {

    private DataLoader() { /* static utility — no instances */ }

    /** Populates a MovieRepository with sample movies spanning multiple genres. */
    public static void loadMovies(MovieRepository repo) {
        repo.addMovie(new Movie(1,  "Inception",                   "Sci-Fi",  8.8));
        repo.addMovie(new Movie(2,  "Interstellar",                "Sci-Fi",  8.6));
        repo.addMovie(new Movie(3,  "The Matrix",                  "Sci-Fi",  8.7));
        repo.addMovie(new Movie(4,  "Arrival",                     "Sci-Fi",  7.9));
        repo.addMovie(new Movie(5,  "Dune: Part Two",              "Sci-Fi",  8.5));

        repo.addMovie(new Movie(6,  "The Dark Knight",             "Action",  9.0));
        repo.addMovie(new Movie(7,  "Mad Max: Fury Road",          "Action",  8.1));
        repo.addMovie(new Movie(8,  "John Wick",                   "Action",  7.4));
        repo.addMovie(new Movie(9,  "Top Gun: Maverick",           "Action",  8.3));
        repo.addMovie(new Movie(10, "Mission: Impossible",         "Action",  7.1));

        repo.addMovie(new Movie(11, "The Shawshank Redemption",    "Drama",   9.3));
        repo.addMovie(new Movie(12, "Forrest Gump",                "Drama",   8.8));
        repo.addMovie(new Movie(13, "Schindler's List",            "Drama",   9.0));
        repo.addMovie(new Movie(14, "The Godfather",               "Drama",   9.2));
        repo.addMovie(new Movie(15, "A Beautiful Mind",            "Drama",   8.2));

        repo.addMovie(new Movie(16, "The Conjuring",               "Horror",  7.5));
        repo.addMovie(new Movie(17, "Hereditary",                  "Horror",  7.3));
        repo.addMovie(new Movie(18, "Get Out",                     "Horror",  7.7));
        repo.addMovie(new Movie(19, "A Quiet Place",               "Horror",  7.5));
        repo.addMovie(new Movie(20, "It",                          "Horror",  7.3));

        repo.addMovie(new Movie(21, "The Intouchables",            "Comedy",  8.5));
        repo.addMovie(new Movie(22, "Superbad",                    "Comedy",  7.6));
        repo.addMovie(new Movie(23, "The Grand Budapest Hotel",    "Comedy",  8.1));
        repo.addMovie(new Movie(24, "Game Night",                  "Comedy",  7.0));
        repo.addMovie(new Movie(25, "Knives Out",                  "Comedy",  7.9));
    }

    /** Creates and returns a list of sample users with genre preferences. */
    public static List<User> createUsers() {
        return Arrays.asList(
            new User(101, "Alice",   "alice@email.com",   Arrays.asList("Sci-Fi", "Action")),
            new User(102, "Bob",     "bob@email.com",     Arrays.asList("Drama",  "Comedy")),
            new User(103, "Charlie", "charlie@email.com", Arrays.asList("Horror", "Action")),
            new User(104, "Diana",   "diana@email.com",   Arrays.asList("Sci-Fi", "Drama")),
            new User(105, "Eve",     "eve@email.com",     Arrays.asList("Comedy", "Horror"))
        );
    }
}
