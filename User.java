package model;

import java.util.List;

/**
 * Represents a user in the Movie Recommendation System.
 *
 * Design Decision: Immutable userId to preserve cache key integrity.
 * Preferred genres drive the recommendation engine logic.
 */
public class User {

    private final int userId;
    private String name;
    private String email;
    private List<String> preferredGenres;

    public User(int userId, String name, String email, List<String> preferredGenres) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.preferredGenres = preferredGenres;
    }

    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<String> getPreferredGenres() { return preferredGenres; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPreferredGenres(List<String> preferredGenres) {
        this.preferredGenres = preferredGenres;
    }

    @Override
    public String toString() {
        return String.format("User{id=%d, name='%s', email='%s', genres=%s}",
                userId, name, email, preferredGenres);
    }
}
