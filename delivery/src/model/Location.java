package model;

/**
 * =============================================================
 *  Location.java  |  model package
 *  PURPOSE : Represents one node (vertex) in the delivery map graph.
 *
 *  ROLE IN DIJKSTRA:
 *    - Every city junction, restaurant, and customer address
 *      is stored as a Location object.
 *    - During Dijkstra, each Location tracks:
 *        shortestDist  → best known distance from the source
 *        visited       → whether it has been fully processed
 *
 *  GRAPH VISUALISATION:
 *
 *   [Restaurant] ──5km── [JunctionA] ──3km── [Customer]
 *        └──────────9km──── [Mall] ───4km── [JunctionB]
 *                                               │
 *                                             1km
 *                                               │
 *                                          [Customer]
 *
 *  Each box = one Location object.
 *
 *  OOP CONCEPTS USED:
 *    - Encapsulation : private fields, public getters/setters
 * =============================================================
 */
public class Location {

    // ── private fields ───────────────────────────────────────
    private String  locationId;    // unique key  e.g. "RestaurantA"
    private String  description;   // display name e.g. "McD Sunway"

    // these two fields are WRITTEN by DeliveryOptimizer during Dijkstra
    private double  shortestDist;  // best distance found so far from source
    private boolean visited;       // true once this node is fully processed

    // ── Constructor ─────────────────────────────────────────
    /**
     * @param locationId  Short unique ID used as the graph's HashMap key
     * @param description Human-readable name shown in output
     */
    public Location(String locationId, String description) {
        this.locationId   = locationId;
        this.description  = description;
        this.shortestDist = Double.MAX_VALUE; // "infinity" — not yet reached
        this.visited      = false;
    }

    // ── Getters ─────────────────────────────────────────────
    public String  getLocationId()  { return locationId; }
    public String  getDescription() { return description; }
    public double  getShortestDist(){ return shortestDist; }
    public boolean isVisited()      { return visited; }

    // ── Setters (used by DeliveryOptimizer during Dijkstra) ─
    public void setShortestDist(double dist) { this.shortestDist = dist; }
    public void setVisited(boolean visited)  { this.visited      = visited; }

    /** Reset this location back to its initial "unseen" state. */
    public void reset() {
        this.shortestDist = Double.MAX_VALUE;
        this.visited      = false;
    }

    // ── toString ────────────────────────────────────────────
    @Override
    public String toString() {
        String d = (shortestDist == Double.MAX_VALUE)
                   ? "inf"
                   : String.format("%.1f km", shortestDist);
        return String.format("Location[%-12s | %-20s | dist=%-10s | %s]",
                locationId, description, d,
                visited ? "visited" : "unvisited");
    }
}