import cache.LRUCache;
import model.User;
import repository.MovieRepository;
import service.RecommendationEngine;
import service.RecommendationService;
import util.DataLoader;

import java.util.List;

/**
 * ╔══════════════════════════════════════════════════════════╗
 * ║      Movie Recommendation System with LRU Cache          ║
 * ║      Low-Level Design (LLD) Demonstration                ║
 * ╚══════════════════════════════════════════════════════════╝
 *
 * Demonstrates:
 *  1. Cache MISS  → generate + store recommendations
 *  2. Cache HIT   → instant return from cache
 *  3. Cache eviction (LRU) when capacity is exceeded
 *  4. Manual cache invalidation
 *  5. Cache statistics
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        header("MOVIE RECOMMENDATION SYSTEM — LLD DEMO");

        // ── Setup ──────────────────────────────────────────────────
        MovieRepository movieRepo = new MovieRepository();
        DataLoader.loadMovies(movieRepo);
        System.out.println("  📦 Movie catalog loaded: " + movieRepo.count() + " movies\n");

        List<User> users = DataLoader.createUsers();
        System.out.println("  👤 Users registered: " + users.size());
        users.forEach(u -> System.out.println("     • " + u));
        System.out.println();

        RecommendationEngine engine = new RecommendationEngine(movieRepo);

        // LRU Cache with capacity 3 → lets us demonstrate eviction with 5 users
        LRUCache lruCache = new LRUCache(3);
        RecommendationService service = new RecommendationService(lruCache, engine);

        User alice   = users.get(0);   // 101
        User bob     = users.get(1);   // 102
        User charlie = users.get(2);   // 103
        User diana   = users.get(3);   // 104
        User eve     = users.get(4);   // 105

        // ══════════════════════════════════════════════════════════
        // SCENARIO 1 — First-time requests → all CACHE MISS
        // ══════════════════════════════════════════════════════════
        section("SCENARIO 1 — First-time requests (expect CACHE MISS × 3)");

        request(service, alice);
        divider();

        request(service, bob);
        divider();

        request(service, charlie);
        divider();

        service.displayCacheState();   // Cache: Charlie → Bob → Alice (MRU → LRU)

        // ══════════════════════════════════════════════════════════
        // SCENARIO 2 — Repeat requests → CACHE HIT
        // ══════════════════════════════════════════════════════════
        section("SCENARIO 2 — Repeat requests (expect CACHE HIT)");

        request(service, alice);       // Alice moves to MRU
        divider();

        request(service, bob);         // Bob moves to MRU
        divider();

        service.displayCacheState();   // Cache: Bob → Alice → Charlie (MRU → LRU)

        // ══════════════════════════════════════════════════════════
        // SCENARIO 3 — Eviction: add Diana when cache is at capacity
        //   LRU at this point = Charlie → gets evicted
        // ══════════════════════════════════════════════════════════
        section("SCENARIO 3 — Cache full, Diana joins (expect Charlie EVICTED)");

        request(service, diana);       // Triggers eviction of Charlie (LRU)
        divider();

        service.displayCacheState();   // Cache: Diana → Bob → Alice

        // ══════════════════════════════════════════════════════════
        // SCENARIO 4 — Charlie re-requests after eviction → MISS again
        // ══════════════════════════════════════════════════════════
        section("SCENARIO 4 — Charlie re-requests after eviction (expect CACHE MISS)");

        request(service, charlie);     // Evicts Alice (now LRU after Diana, Bob promoted)
        divider();

        service.displayCacheState();

        // ══════════════════════════════════════════════════════════
        // SCENARIO 5 — Eve joins, manual invalidation of Bob
        // ══════════════════════════════════════════════════════════
        section("SCENARIO 5 — Eve joins + manual invalidation of Bob");

        request(service, eve);         // Evicts LRU
        divider();

        System.out.println("  🔧 Bob updated his genre preferences. Invalidating his cache...");
        service.invalidateCache(bob.getUserId());
        divider();

        service.displayCacheState();

        // Bob re-requests after invalidation → MISS → fresh computation
        request(service, bob);
        divider();

        service.displayCacheState();

        // ══════════════════════════════════════════════════════════
        // FINAL — Print aggregate cache statistics
        // ══════════════════════════════════════════════════════════
        section("FINAL — Cache Statistics");
        service.printCacheStats();

        header("DEMO COMPLETE");
    }

    // ----------------------------------------------------------------
    // Helper print utilities
    // ----------------------------------------------------------------

    private static void request(RecommendationService svc, User user) {
        System.out.println();
        svc.getRecommendations(user);
        System.out.println();
    }

    private static void header(String title) {
        String line = "═".repeat(58);
        System.out.println("\n╔" + line + "╗");
        System.out.printf( "║  %-56s║%n", title);
        System.out.println("╚" + line + "╝\n");
    }

    private static void section(String title) {
        System.out.println("  ─── " + title + " ───");
    }

    private static void divider() {
        System.out.println("  " + "─".repeat(54));
    }
}
