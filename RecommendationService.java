package service;

import cache.LRUCache;
import model.Movie;
import model.User;

import java.util.List;

/**
 * Service layer orchestrating cache + recommendation engine.
 *
 * Flow:
 *   1. Check LRU Cache → if HIT, return instantly.
 *   2. On MISS, delegate to RecommendationEngine (expensive).
 *   3. Store result in cache for future requests.
 *
 * Design Decision: This class owns the cache and engine references.
 * Neither depends on the other — loose coupling via this orchestrator.
 * Follows Dependency Injection; both collaborators are injected.
 */
public class RecommendationService {

    private final LRUCache cache;
    private final RecommendationEngine engine;

    public RecommendationService(LRUCache cache, RecommendationEngine engine) {
        this.cache = cache;
        this.engine = engine;
    }

    /**
     * Main entry point for getting movie recommendations.
     * Implements cache-aside (lazy loading) pattern.
     */
    public List<Movie> getRecommendations(User user) {
        int userId = user.getUserId();

        System.out.println("  📥 Requesting recommendations for: "
                + user.getName() + " (User " + userId + ")");

        // --- Cache Lookup ---
        List<Movie> cached = cache.get(userId);

        if (cached != null) {
            System.out.println("  ✅ [CACHE HIT]  Returning cached recommendations for User " + userId);
            printRecommendations(cached);
            return cached;
        }

        // --- Cache Miss: Generate + Store ---
        System.out.println("  ❌ [CACHE MISS] No cached entry for User " + userId);
        List<Movie> fresh = engine.generateRecommendations(user);
        cache.put(userId, fresh);
        System.out.println("  💾 Recommendations cached for User " + userId + ".");
        printRecommendations(fresh);
        return fresh;
    }

    /** Evict a specific user from the cache (e.g., when preferences change). */
    public void invalidateCache(int userId) {
        cache.remove(userId);
        System.out.println("  🗑️  Cache invalidated for User " + userId);
    }

    public void displayCacheState() {
        cache.displayCache();
    }

    public void printCacheStats() {
        cache.printStats();
    }

    // ----------------------------------------------------------------
    // Private helpers
    // ----------------------------------------------------------------

    private void printRecommendations(List<Movie> movies) {
        System.out.println("  🎬 Recommended Movies:");
        movies.forEach(m -> System.out.printf(
                "     • %-30s [%s] ★ %.1f%n", m.getTitle(), m.getGenre(), m.getRating()));
    }
}
