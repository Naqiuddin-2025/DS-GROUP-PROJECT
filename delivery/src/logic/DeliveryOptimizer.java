package logic;

import model.Rider;
import model.Location;

import java.util.*;

/**
 * =============================================================
 *  DeliveryOptimizer.java  |  logic package
 *
 *  This class implements TWO modules for Member 4:
 *
 *  ┌─────────────────────────────────────────────────────────┐
 *  │  MODULE 1 — Delivery Assignment (15 marks)              │
 *  │  Data structure : Priority Queue (Min Heap)             │
 *  │  Key method     : assignBestRider()                     │
 *  │  Complexity     : O(log n) vs Linear Search O(n)        │
 *  └─────────────────────────────────────────────────────────┘
 *  ┌─────────────────────────────────────────────────────────┐
 *  │  MODULE 2 — Route Optimization (20 marks)               │
 *  │  Data structure : Weighted Graph (adjacency list)       │
 *  │  Algorithm      : Dijkstra's Shortest Path              │
 *  │  Complexity     : O((V + E) log V)                      │
 *  └─────────────────────────────────────────────────────────┘
 *
 *  OOP CONCEPTS USED:
 *    - Encapsulation : private fields, public methods only
 *    - Abstraction   : caller just uses addRider() / djikstra(),
 *                      internal heap and graph details hidden
 *    - Inner class   : Edge (graph edge, only needed here)
 * =============================================================
 */
public class DeliveryOptimizer {

    // ==========================================================
    //  INNER CLASS — Edge
    //  Represents a one-way road between two locations.
    //  Stored inside the adjacency list of the graph.
    // ==========================================================
    private static class Edge {
        String destination;   // location ID this road leads to
        double weightKm;      // distance in km

        Edge(String destination, double weightKm) {
            this.destination = destination;
            this.weightKm    = weightKm;
        }
    }

    // ==========================================================
    //  FIELDS
    // ==========================================================

    // --- MODULE 1: Min Heap -----------------------------------
    // PriorityQueue uses Rider.compareTo() which orders by
    // estimatedDeliveryTime ascending → Min Heap behaviour.
    // The rider with the LOWEST time is always at the root.
    private PriorityQueue<Rider> riderHeap;

    // --- MODULE 2: Graph (Adjacency List) --------------------
    // Key   = locationId (String)
    // Value = list of outgoing Edge objects
    //
    // WHY HashMap over 2-D array (adjacency matrix)?
    //   - Real maps are SPARSE: each junction connects to only
    //     a few roads, not every other location.
    //   - HashMap uses O(V + E) memory vs O(V²) for a matrix.
    //   - graph.get("RestaurantA") is O(1) average.
    private HashMap<String, List<Edge>>     graph;
    private HashMap<String, Location>       locationMap;  // id → Location object

    // ==========================================================
    //  CONSTRUCTOR
    // ==========================================================
    public DeliveryOptimizer() {
        this.riderHeap   = new PriorityQueue<>();   // natural order = Rider.compareTo()
        this.graph       = new HashMap<>();
        this.locationMap = new HashMap<>();
    }

    // ==========================================================
    //  MODULE 1 — DELIVERY ASSIGNMENT (Priority Queue / Min Heap)
    // ==========================================================

    /**
     * Add a rider to the Min Heap.
     *
     * Internally: heap.offer() places the new element at the end
     * of the internal array, then "bubbles up" (heapify-up) until
     * the heap property is restored.
     *
     * Time Complexity: O(log n)
     *   — log n swaps needed in a heap of n elements
     */
    public void addRider(Rider rider) {
        if (!rider.isAvailable()) {
            System.out.println("  [Heap] " + rider.getName() + " is not available — skipped.");
            return;
        }
        riderHeap.offer(rider);
        System.out.println("  [Heap] Added  → " + rider);
    }

    /**
     * Assign the BEST available rider to the next order.
     *
     * heap.poll() removes the ROOT of the heap (minimum element).
     * After removal, the last element is moved to the root and
     * "sinks down" (heapify-down) to restore heap order.
     *
     * Time Complexity: O(log n)
     *
     * WHY THIS IS BETTER THAN LINEAR SEARCH:
     *   Linear search scans every element → O(n)
     *   Min Heap always has the minimum at the root → O(log n) to remove it
     *
     *   n = 10    → Heap: ~3 ops   Linear: 10 ops
     *   n = 100   → Heap: ~7 ops   Linear: 100 ops
     *   n = 1000  → Heap: ~10 ops  Linear: 1000 ops
     *
     * @param orderId  The order this rider is being assigned to
     * @return         The assigned Rider, or null if no riders available
     */
    public Rider assignBestRider(String orderId) {
        if (riderHeap.isEmpty()) {
            System.out.println("  [Heap] No riders available!");
            return null;
        }
        Rider best = riderHeap.poll();   // O(log n): remove root + heapify-down
        best.setAvailable(false);
        System.out.println("  [Heap] Assigned → " + best + " to Order " + orderId);
        return best;
    }

    /**
     * Preview the best rider WITHOUT removing from the heap.
     *
     * Time Complexity: O(1) — just read the root pointer, no restructuring.
     *
     * @return Rider at heap root, or null if empty
     */
    public Rider peekBestRider() {
        return riderHeap.peek();
    }

    /**
     * Return a rider to the pool after completing a delivery.
     * Re-inserts them into the heap.
     *
     * Time Complexity: O(log n)
     */
    public void returnRider(Rider rider) {
        rider.setAvailable(true);
        riderHeap.offer(rider);
        System.out.println("  [Heap] Returned → " + rider.getName() + " back to heap.");
    }

    /**
     * Display all riders currently in the heap, sorted by priority.
     *
     * NOTE: iterating a PriorityQueue does NOT guarantee sorted order —
     *       we copy to a list and sort for display purposes only.
     */
    public void displayHeap() {
        if (riderHeap.isEmpty()) {
            System.out.println("    (heap is empty — no available riders)");
            return;
        }
        List<Rider> sorted = new ArrayList<>(riderHeap);
        Collections.sort(sorted);    // uses Rider.compareTo() → sort by time
        int rank = 1;
        for (Rider r : sorted) {
            String tag = (rank == 1) ? "  <-- heap root (fastest, assigned first)" : "";
            System.out.println("    " + rank + ". " + r + tag);
            rank++;
        }
    }

    /**
     * LINEAR SEARCH comparison method.
     *
     * Scans a plain List from index 0 to end, checking every rider.
     * Finds the one with the lowest delivery time.
     *
     * Time Complexity: O(n) — must visit every element.
     *
     * This method EXISTS to demonstrate WHY the Min Heap is better.
     * Both methods find the same best rider, but Min Heap is faster.
     *
     * @param riderList  A plain ArrayList of riders (no heap ordering)
     * @return           The best rider found, or null if list is empty
     */
    public Rider linearSearchBestRider(List<Rider> riderList) {
        Rider best = null;
        int   comparisons = 0;

        System.out.println("  [Linear] Scanning every rider one by one...");
        for (Rider r : riderList) {
            comparisons++;
            System.out.printf("    Step %2d : Check %-12s (%.1f min) — ",
                              comparisons, r.getName(), r.getEstimatedDeliveryTime());

            if (r.isAvailable()) {
                if (best == null ||
                    r.getEstimatedDeliveryTime() < best.getEstimatedDeliveryTime()) {
                    best = r;
                    System.out.print("NEW BEST");
                } else {
                    System.out.print("not better");
                }
            } else {
                System.out.print("not available");
            }
            System.out.println();
        }
        System.out.println("  [Linear] Total comparisons : " + comparisons
                           + "  →  O(n)");
        System.out.println("  [Linear] Best found        : " + best);
        return best;
    }

    /** How many riders are currently in the heap. */
    public int getHeapSize() { return riderHeap.size(); }

    // ==========================================================
    //  MODULE 2 — ROUTE OPTIMIZATION (Graph + Dijkstra)
    // ==========================================================

    /**
     * Add a Location node to the graph.
     *
     * - Stores the Location object in locationMap (id → Location)
     * - Creates an empty adjacency list for it in graph
     *
     * Time Complexity: O(1) average (HashMap.put)
     *
     * @param location  A Location object to add
     */
    public void addLocation(Location location) {
        String id = location.getLocationId();
        locationMap.putIfAbsent(id, location);
        graph.putIfAbsent(id, new ArrayList<>());
        System.out.println("  [Graph] Location added : " + id
                           + " (" + location.getDescription() + ")");
    }

    /**
     * Add a DIRECTED road (one-way edge) from → to.
     *
     * Both locations must already exist in the graph.
     *
     * Time Complexity: O(1) — HashMap.get() + ArrayList.add()
     *
     * @param fromId   Source location ID
     * @param toId     Destination location ID
     * @param km       Distance of this road in kilometres
     */
    public void addDirectedRoad(String fromId, String toId, double km) {
        if (!graph.containsKey(fromId) || !graph.containsKey(toId)) {
            System.out.println("  [Graph] ERROR: location not found — "
                               + fromId + " or " + toId);
            return;
        }
        graph.get(fromId).add(new Edge(toId, km));
        System.out.printf("  [Graph] Road added  : %-14s → %-14s (%.1f km)%n",
                          fromId, toId, km);
    }

    /**
     * Add an UNDIRECTED road (two-way) between two locations.
     * Calls addDirectedRoad() twice, once in each direction.
     *
     * Time Complexity: O(1)
     */
    public void addUndirectedRoad(String aId, String bId, double km) {
        addDirectedRoad(aId, bId, km);
        addDirectedRoad(bId, aId, km);
    }

    /**
     * Print the full graph as an adjacency list.
     * Useful for reports and demos.
     */
    public void printGraph() {
        System.out.println();
        System.out.println("  Adjacency List representation:");
        System.out.println("  ─────────────────────────────────────────────────");
        for (Map.Entry<String, List<Edge>> entry : graph.entrySet()) {
            String     from  = entry.getKey();
            List<Edge> edges = entry.getValue();
            StringBuilder sb = new StringBuilder();
            sb.append("  ").append(String.format("%-14s", from)).append("→  ");
            if (edges.isEmpty()) {
                sb.append("(no outgoing roads)");
            } else {
                for (int i = 0; i < edges.size(); i++) {
                    Edge e = edges.get(i);
                    sb.append(e.destination)
                      .append(" (").append(e.weightKm).append(" km)");
                    if (i < edges.size() - 1) sb.append(",  ");
                }
            }
            System.out.println(sb);
        }
        System.out.println("  ─────────────────────────────────────────────────");
    }

    /**
     * =============================================================
     *  DIJKSTRA'S SHORTEST PATH ALGORITHM
     * =============================================================
     *
     *  HOW IT WORKS — plain English:
     *  ─────────────────────────────
     *  1. Set the distance of the START location to 0.
     *     Set ALL other locations to infinity (not yet reached).
     *
     *  2. Put the START location into a Min Heap (priority queue).
     *
     *  3. Loop while the heap is not empty:
     *     a. POLL the location with the smallest known distance.
     *     b. Mark it as visited (we won't revisit it).
     *     c. For each NEIGHBOUR connected by a road:
     *           newDist = distanceToCurrent + roadLength
     *           If newDist is LESS than neighbour's current best:
     *              → UPDATE neighbour's distance
     *              → RECORD that we came from current location
     *              → ADD neighbour to the heap
     *
     *  4. When the destination is polled from the heap, we're done.
     *
     *  5. Trace back through "prev" map to print the full path.
     *
     *  WHY USE A MIN HEAP INSIDE DIJKSTRA?
     *    Every time we need the "closest unvisited location", the
     *    heap gives it in O(log V) instead of O(V) for linear scan.
     *    This is what makes Dijkstra efficient.
     *
     *  TIME COMPLEXITY : O( (V + E) log V )
     *    V     = number of locations (vertices)
     *    E     = number of roads (edges)
     *    log V = cost of each heap operation
     *
     *  SPACE COMPLEXITY: O(V)
     *    Stores dist[], prev[], and visited set — each size V.
     *
     *  WHY DIJKSTRA OVER BFS?
     *    BFS finds the shortest path in terms of NUMBER of hops.
     *    It treats all roads as equal length.
     *    Dijkstra handles WEIGHTED roads — some are longer than others.
     *    For delivery routing, roads have real distances → need Dijkstra.
     *
     * @param startId  Location ID of the restaurant
     * @param endId    Location ID of the customer's address
     * @return         Shortest distance in km, or -1 if unreachable
     */
    public double dijkstra(String startId, String endId) {

        // ── Validate input ─────────────────────────────────────
        if (!graph.containsKey(startId)) {
            System.out.println("  ERROR: Start location not found: " + startId);
            return -1;
        }
        if (!graph.containsKey(endId)) {
            System.out.println("  ERROR: End location not found: " + endId);
            return -1;
        }

        // ── STEP 1: Reset all Location objects ────────────────
        for (Location loc : locationMap.values()) {
            loc.reset();
        }

        // ── STEP 2: Initialise distance and predecessor maps ──
        Map<String, Double> dist = new HashMap<>();   // best known dist from start
        Map<String, String> prev = new HashMap<>();   // which node did we come from

        for (String id : graph.keySet()) {
            dist.put(id, Double.MAX_VALUE);   // infinity
            prev.put(id, null);               // no predecessor yet
        }
        dist.put(startId, 0.0);               // distance from start to itself = 0
        locationMap.get(startId).setShortestDist(0.0);

        // ── STEP 3: Min Heap — entries are [distance, locationId] ─
        // We use a PriorityQueue that orders by the distance value.
        // int[0] stored as double = distance
        // The locationId is tracked separately via a parallel index list.
        //
        // Simpler beginner-friendly approach:
        // store String[] {locationId} and look up dist[] for comparison.
        PriorityQueue<String> heap = new PriorityQueue<>(
            (a, b) -> Double.compare(
                dist.getOrDefault(a, Double.MAX_VALUE),
                dist.getOrDefault(b, Double.MAX_VALUE)
            )
        );
        heap.offer(startId);

        System.out.println("  Start : " + startId + "  (dist = 0)");
        System.out.println("  All other locations : dist = infinity");
        System.out.println();

        // ── STEP 4: Main loop ──────────────────────────────────
        int stepNum = 0;
        while (!heap.isEmpty()) {

            String currId = heap.poll();   // location with smallest known dist

            // Skip if already visited (stale heap entry)
            if (locationMap.get(currId).isVisited()) continue;
            locationMap.get(currId).setVisited(true);

            double currDist = dist.get(currId);
            stepNum++;
            System.out.printf("  Step %d : Poll '%-14s' | dist = %.1f km%n",
                              stepNum, currId, currDist);

            // Early exit once we process the destination
            if (currId.equals(endId)) {
                System.out.println("           Destination reached — stopping.");
                break;
            }

            // ── STEP 5: Relax all edges from currId ───────────
            for (Edge edge : graph.get(currId)) {

                if (locationMap.get(edge.destination).isVisited()) continue;

                double newDist = currDist + edge.weightKm;
                double oldDist = dist.get(edge.destination);

                if (newDist < oldDist) {
                    // Found a shorter path — update
                    dist.put(edge.destination, newDist);
                    prev.put(edge.destination, currId);
                    locationMap.get(edge.destination).setShortestDist(newDist);
                    heap.offer(edge.destination);   // re-add with new priority

                    System.out.printf("           Relax  %-14s → %-14s : %.1f + %.1f = %.1f km  (improved from %s)%n",
                            currId, edge.destination,
                            currDist, edge.weightKm, newDist,
                            oldDist == Double.MAX_VALUE ? "inf" : String.format("%.1f", oldDist));
                }
            }
        }

        // ── STEP 6: Read result ────────────────────────────────
        double finalDist = dist.get(endId);
        if (finalDist == Double.MAX_VALUE) {
            System.out.println("\n  Result : NO path from " + startId + " to " + endId);
            return -1;
        }

        // ── STEP 7: Reconstruct the path ──────────────────────
        List<String> path = new ArrayList<>();
        String cursor = endId;
        while (cursor != null) {
            path.add(0, cursor);
            cursor = prev.get(cursor);
        }

        System.out.println();
        System.out.println("  ── Shortest Path Result ─────────────────────────");
        System.out.println("  Route    : " + String.join("  →  ", path));
        System.out.printf ("  Distance : %.1f km%n", finalDist);
        System.out.println("  ─────────────────────────────────────────────────");

        return finalDist;
    }

    /**
     * Print a summary of ALL final distances from a source.
     * Call this after dijkstra() to show the complete distance table.
     */
    public void printDistanceTable(String sourceId) {
        System.out.println("\n  ── Distance Table from '" + sourceId + "' ──────────");
        System.out.printf("  %-18s  %-12s  %-8s%n", "Location", "Description", "Distance");
        System.out.println("  ─────────────────────────────────────────────────");
        for (Map.Entry<String, Location> entry : locationMap.entrySet()) {
            Location loc = entry.getValue();
            String distStr = (loc.getShortestDist() == Double.MAX_VALUE)
                             ? "unreachable"
                             : String.format("%.1f km", loc.getShortestDist());
            System.out.printf("  %-18s  %-12s  %-8s%n",
                    loc.getLocationId(), loc.getDescription(), distStr);
        }
        System.out.println("  ─────────────────────────────────────────────────");
    }
}