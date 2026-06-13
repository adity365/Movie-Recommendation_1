package cache;

import java.util.List;
import model.Movie;

/**
 * Doubly Linked List Node for the LRU Cache.
 *
 * Design Decision: Generic approach could be used, but we keep it
 * typed (Integer key, List<Movie> value) for clarity and interview
 * readability. Each node stores both key and value so that on eviction
 * we can remove the entry from the HashMap in O(1) without a reverse lookup.
 *
 *   [prev] <-- [Node] --> [next]
 */
public class Node {

    int key;                   // userId
    List<Movie> value;         // cached recommendations

    Node prev;
    Node next;

    public Node(int key, List<Movie> value) {
        this.key = key;
        this.value = value;
    }

    /** Sentinel / dummy node constructor (no data). */
    public Node() {
        this(-1, null);
    }
}
