package service;

import model.Movie;
import model.User;
import repository.MovieRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Core recommendation engine.
 *
 * Design Decision: Isolated behind its own class so the algorithm
 * can evolve (collaborative filtering, ML scoring, etc.) without
 * touching the caching or service layers — Open/Closed Principle.
 *
 * The Thread.sleep() simulates real-world latency (DB queries,
 * ML model inference, external API calls). This is exactly what
 * makes caching valuable.
 */
public class RecommendationEngine {

    private static final int SIMULATION_DELAY_MS = 1500; // 1.5 s artificial delay
    private static final int MAX_RECOMMENDATIONS  = 5;

    private final MovieRepository movieRepository;

    public RecommendationEngine(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * Generates recommendations for the given user.
     * Expensive: simulates heavy computation with a delay.
     */
    public List<Movie> generateRecommendations(User user) {
        System.out.println("  🔄 Generating recommendations for " + user.getName()
                + " (User " + user.getUserId() + ")...");
        System.out.println("  ⏳ [Simulating heavy computation — " + SIMULATION_DELAY_MS + " ms]");

        simulateHeavyComputation();

        List<Movie> recommendations;

        if (user.getPreferredGenres() != null && !user.getPreferredGenres().isEmpty()) {
            // Genre-aware recommendation: fetch matching movies, top-rated first
            recommendations = movieRepository.findByGenres(user.getPreferredGenres())
                    .stream()
                    .limit(MAX_RECOMMENDATIONS)
                    .collect(Collectors.toList());
        } else {
            // Fallback: return globally top-rated movies
            recommendations = movieRepository.getAllMovies()
                    .stream()
                    .sorted((a, b) -> Double.compare(b.getRating(), a.getRating()))
                    .limit(MAX_RECOMMENDATIONS)
                    .collect(Collectors.toList());
        }

        System.out.println("  ✅ Recommendations generated (" + recommendations.size() + " movies).");
        return recommendations;
    }

    private void simulateHeavyComputation() {
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Recommendation computation interrupted.");
        }
    }
}
