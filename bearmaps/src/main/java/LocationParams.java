/** The LocationParams contains a serialization-ready representation of a location in Berkeley. */
class LocationParams {
    /** The coordinates of this location. */
    final double lat, lon;
    /** The name of this location. */
    final String name;
    /** The ID of this location. */
    final long id;

    /** Private constructor to prevent direct instantiation of a LocationParams instance. */
    private LocationParams() {
        this(0.0, 0.0, null, 0L);
    }

    LocationParams(double lat, double lon, String name, long id) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.id = id;
    }
}
