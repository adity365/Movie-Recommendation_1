package cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Movie;

/**
 * Custom LRU Cache implemented with:
 *   - HashMap<Integer, Node>  → O(1) key lookup
 *   - Doubly Linked List      → O(1) insertion / removal at any position
 *
 * Layout of the doubly linked list:
 *
 *   [HEAD (dummy)] <-> [MRU node] <-> ... <-> [LRU node] <-> [TAIL (dummy)]
 *
 * HEAD.next  → Most Recently Used
 * TAIL.prev  → Least Recently Used
 *
 * Sentinel head/tail nodes eliminate null-pointer edge cases at list ends.
 *
 * All public operations: O(1) time, O(capacity) space.
 */
public class LRUCache {

    private final int capacity;
    private final Map<Integer, Node> map;      // key → Node
    private final Node head;                   // dummy MRU sentinel
    private final Node tail;                   // dummy LRU sentinel

    // --- Cache Statistics ---
    private int hitCount;
    private int missCount;

    public LRUCache(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Cache capacity must be > 0");
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.hitCount = 0;
        this.missCount = 0;

        // Initialise sentinel nodes; they never hold real data
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;
    }

    // ----------------------------------------------------------------
    // Public API
    // ----------------------------------------------------------------

    /**
     * Retrieve recommendations for userId.
     * Moves accessed node to MRU position (front of list).
     * Returns null on cache miss.
     */
    public List<Movie> get(int key) {
        if (!map.containsKey(key)) {
            missCount++;
            return null;
        }
        hitCount++;
        Node node = map.get(key);
        moveToFront(node);          // mark as most recently used
        return node.value;
    }

    /**
     * Insert or update a cache entry.
     * If key exists → update value and move to MRU.
     * If cache is full → evict LRU (node before tail) before inserting.
     */
    public void put(int key, List<Movie> value) {
        if (map.containsKey(key)) {
            // Update existing node
            Node node = map.get(key);
            node.value = value;
            moveToFront(node);
            return;
        }

        if (map.size() == capacity) {
            evictLRU();
        }

        Node newNode = new Node(key, value);
        map.put(key, newNode);
        insertAtFront(newNode);
    }

    /**
     * Explicitly remove an entry from the cache.
     */
    public void remove(int key) {
        if (!map.containsKey(key)) return;
        Node node = map.get(key);
        removeNode(node);
        map.remove(key);
    }

    // ----------------------------------------------------------------
    // Display / Statistics
    // ----------------------------------------------------------------

    /** Prints cache state from MRU → LRU. */
    public void displayCache() {
        System.out.println();
        System.out.println("  ┌─────────────────────────────┐");
        System.out.println("  │  CACHE STATE (MRU → LRU)    │");
        System.out.println("  ├─────────────────────────────┤");
        System.out.printf( "  │  Capacity : %d / %d%s│%n",
                map.size(), capacity,
                " ".repeat(Math.max(0, 16 - String.valueOf(map.size()).length()
                        - String.valueOf(capacity).length())));
        System.out.println("  ├─────────────────────────────┤");

        if (map.isEmpty()) {
            System.out.println("  │  (empty)                    │");
        } else {
            System.out.println("  │  ▲ Most Recently Used        │");
            Node curr = head.next;
            int rank = 1;
            while (curr != tail) {
                System.out.printf("  │  %d. User %-20s│%n", rank++, curr.key);
                curr = curr.next;
            }
            System.out.println("  │  ▼ Least Recently Used       │");
        }
        System.out.println("  └─────────────────────────────┘");
        System.out.println();
    }

    public void printStats() {
        int total = hitCount + missCount;
        double hitRatio = total == 0 ? 0.0 : (hitCount * 100.0 / total);
        System.out.println();
        System.out.println("  ╔═══════════════════════════════╗");
        System.out.println("  ║       CACHE STATISTICS        ║");
        System.out.println("  ╠═══════════════════════════════╣");
        System.out.printf( "  ║  Total Requests : %-11d║%n", total);
        System.out.printf( "  ║  Cache Hits     : %-11d║%n", hitCount);
        System.out.printf( "  ║  Cache Misses   : %-11d║%n", missCount);
        System.out.printf( "  ║  Hit Ratio      : %-9.1f %% ║%n", hitRatio);
        System.out.println("  ╚═══════════════════════════════╝");
        System.out.println();
    }

    public int getHitCount()  { return hitCount;  }
    public int getMissCount() { return missCount; }
    public int size()         { return map.size(); }
    public int getCapacity()  { return capacity;  }

    // ----------------------------------------------------------------
    // Private helpers  (all O(1))
    // ----------------------------------------------------------------

    /** Remove node from its current list position (does NOT update map). */
    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    /** Insert node immediately after head (MRU position). */
    private void insertAtFront(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    /** Move existing node to MRU position. */
    private void moveToFront(Node node) {
        removeNode(node);
        insertAtFront(node);
    }

    /**
     * Evict the Least Recently Used node (node before tail).
     * Prints an eviction notice.
     */
    private void evictLRU() {
        Node lru = tail.prev;
        if (lru == head) return;          // cache is empty (shouldn't happen)
        System.out.println("  ⚠️  [EVICTION] Cache full. Evicting User " + lru.key
                + " (Least Recently Used)");
        removeNode(lru);
        map.remove(lru.key);
    }
}
