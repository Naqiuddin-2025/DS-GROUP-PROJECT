package model;

/**
 * =============================================================
 *  Rider.java  |  model package
 *  PURPOSE : Stores all data about one delivery rider.
 *            Implements Comparable<Rider> so Java's PriorityQueue
 *            (Min Heap) can rank riders automatically.
 *
 *  WHY Comparable<Rider>?
 *    Java's PriorityQueue needs to know which Rider is "smaller".
 *    We define "smaller" = faster estimated delivery time.
 *    The rider with the LOWEST time sits at the heap root.
 *
 *  OOP CONCEPTS USED:
 *    - Encapsulation : all fields are private, accessed via
 *                      getters and setters only
 *    - Interface     : implements Comparable<Rider>
 * =============================================================
 */
public class Rider implements Comparable<Rider> {

    // ── private fields (Encapsulation) ──────────────────────
    private String  riderId;
    private String  name;
    private double  estimatedDeliveryTime;  // minutes  <-- MIN HEAP KEY
    private double  distanceKm;             // km from restaurant
    private boolean isAvailable;

    // ── Constructor ─────────────────────────────────────────
    public Rider(String riderId, String name,
                 double estimatedDeliveryTime, double distanceKm) {
        this.riderId               = riderId;
        this.name                  = name;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.distanceKm            = distanceKm;
        this.isAvailable           = true;   // riders start available
    }

    // ── Getters ─────────────────────────────────────────────
    public String  getRiderId()               { return riderId; }
    public String  getName()                  { return name; }
    public double  getEstimatedDeliveryTime() { return estimatedDeliveryTime; }
    public double  getDistanceKm()            { return distanceKm; }
    public boolean isAvailable()              { return isAvailable; }

    // ── Setters ─────────────────────────────────────────────
    public void setAvailable(boolean available)         { this.isAvailable           = available; }
    public void setEstimatedDeliveryTime(double minutes){ this.estimatedDeliveryTime = minutes;   }

    // ── compareTo  (REQUIRED by PriorityQueue / Min Heap) ───
    /**
     * Defines the ordering rule for the Min Heap.
     *
     * Returns NEGATIVE  → this rider comes BEFORE other (lower time = higher priority)
     * Returns ZERO      → equal priority
     * Returns POSITIVE  → this rider comes AFTER other
     *
     * Example:
     *   Rider A = 5 min,  Rider B = 12 min
     *   A.compareTo(B) = Double.compare(5, 12) = negative
     *   → A goes to heap root (assigned first)
     */
    @Override
    public int compareTo(Rider other) {
        return Double.compare(this.estimatedDeliveryTime,
                              other.estimatedDeliveryTime);
    }

    // ── toString ────────────────────────────────────────────
    @Override
    public String toString() {
        return String.format("Rider[%-4s | %-12s | %5.1f min | %4.1f km | %s]",
                riderId, name, estimatedDeliveryTime, distanceKm,
                isAvailable ? "Available" : "Busy");
    }
}