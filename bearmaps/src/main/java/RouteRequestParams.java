import java.util.Map;

/**
 * The RouteRequestParams contains the fields received from the browser in the shortest-route
 * request. This class will primarily be used by the MapServer to unpack data received from the
 * web browser before sending calling the shortestPath method.
 *
 * This class can either be instantiated through a Spark request query map, or through the Builder
 * class for mocking browser requests during unit testing of Router.shortestPath.
 *
 * @author Kevin Lin
 */
public class RouteRequestParams {
    /** The start point latitude. */
    public final double startLat;
    /** The start point longitude. */
    public final double startLon;
    /** The end point latitude. */
    public final double endLat;
    /** The end point longitude. */
    public final double endLon;

    /**
     * Validate and return a parameter map of the required request parameters.
     * Requires that all input parameters are doubles.
     * @param req The queryParams map from a Spark HTTP Request.
     * @return A populated map of input parameter to it's numerical value.
     */
    public static RouteRequestParams from(Map<String, String[]> req) {
        Builder builder = new Builder();
        for (String param : REQUIRED_PARAMS) {
            if (!req.containsKey(param)) {
                String msg = String.format("Request failed: %s not found.", param);
                throw new IllegalArgumentException(msg);
            } else {
                try {
                    builder.set(param, Double.parseDouble(req.get(param)[0]));
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    e.printStackTrace();
                    String msg = String.format("Incorrect parameters: unable to parse %s.", param);
                    throw new IllegalArgumentException(msg);
                }
            }
        }
        return builder.create();
    }

    /**
     * Constructor with private access to prevent direct instantiation of a RasterResultParams
     * instance. Use the the factory method, from, instead.
     */
    private RouteRequestParams() {
        this(0.0, 0.0, 0.0, 0.0);
    }

    private RouteRequestParams(double startLat, double startLon, double endLat, double endLon) {
        this.startLat = startLat;
        this.startLon = startLon;
        this.endLat = endLat;
        this.endLon = endLon;
    }

    public static class Builder {
        private double startLat, startLon, endLat, endLon;

        /**
         * Creates a RouteRequestParams.Builder instance that can be used to build
         * a RouteRequestParams with various fields pre-set.
         */
        public Builder() {
        }

        public RouteRequestParams create() {
            return new RouteRequestParams(startLat, startLon, endLat, endLon);
        }

        public Builder setStartLat(double value) {
            this.startLat = value;
            return this;
        }

        public Builder setStartLon(double value) {
            this.startLon = value;
            return this;
        }

        public Builder setEndLat(double value) {
            this.endLat = value;
            return this;
        }

        public Builder setEndLon(double value) {
            this.endLon = value;
            return this;
        }

        private Builder set(String field, double value) {
            switch (field) {
                case "start_lat":
                    this.startLat = value;
                    break;
                case "start_lon":
                    this.startLon = value;
                    break;
                case "end_lat":
                    this.endLat = value;
                    break;
                case "end_lon":
                    this.endLon = value;
                    break;
                default:
                    String msg = String.format("%s is not a valid field for %f.%n", field, value);
                    throw new IllegalArgumentException(msg);
            }
            return this;
        }
    }

    /** Route requests to the server must have the following keys in the params map. */
    private static final String[] REQUIRED_PARAMS = {
        "start_lat", "start_lon", "end_lat", "end_lon"
    };
}
