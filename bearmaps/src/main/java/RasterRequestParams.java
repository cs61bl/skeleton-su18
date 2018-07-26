import java.util.Map;

/**
 * The RasterRequestParams contains the fields received from the browser during a rastering request.
 *
 * This class can either be instantiated through a Spark request query map, or through the Builder
 * class for mocking browser requests during unit testing of Rasterer.getMapRaster.
 *
 * @author Kevin Lin
 */
public class RasterRequestParams {
    /** The requested upper-left latitude. */
    public final double ullat;
    /** The requested upper-left longitude. */
    public final double ullon;
    /** The requested lower-right latitude. */
    public final double lrlat;
    /** The requested lower-right longitude. */
    public final double lrlon;
    /** The width (in pixels) of the browser viewport. */
    public final double w;
    /** The height (in pixels) of the browser viewport. */
    public final double h;

    /**
     * Validate and return a parameter map of the required request parameters.
     * Requires that all input parameters are doubles.
     * @param req HTTP Request
     * @return A populated map of input parameter to it's numerical value.
     */
    public static RasterRequestParams from(Map<String, String[]> req) {
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
     * Constructor with private access to prevent direct instantiation of a RasterRequestParams
     * instance. Use the factory method, from, instead.
     */
    private RasterRequestParams() {
        this(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }

    private RasterRequestParams(
            double ullat, double ullon, double lrlat, double lrlon, double w, double h) {
        this.ullat = ullat;
        this.ullon = ullon;
        this.lrlat = lrlat;
        this.lrlon = lrlon;
        this.w = w;
        this.h = h;
    }

    public static class Builder {
        private double ullat, ullon, lrlat, lrlon, w, h;

        /**
         * Creates a RasterRequestParams.Builder instance that can be used to build
         * a RasterRequestParams with various fields pre-set.
         */
        public Builder() {
        }

        public Builder setUllat(double value) {
            this.ullat = value;
            return this;
        }

        public Builder setUllon(double value) {
            this.ullon = value;
            return this;
        }

        public Builder setLrlat(double value) {
            this.lrlat = value;
            return this;
        }

        public Builder setLrlon(double value) {
            this.lrlon = value;
            return this;
        }

        public Builder setW(double width) {
            this.w = width;
            return this;
        }

        public Builder setH(double height) {
            this.h = height;
            return this;
        }

        public RasterRequestParams create() {
            return new RasterRequestParams(ullat, ullon, lrlat, lrlon, w, h);
        }

        private Builder set(String field, double value) {
            switch (field) {
                case "ullat":
                    this.ullat = value;
                    break;
                case "ullon":
                    this.ullon = value;
                    break;
                case "lrlat":
                    this.lrlat = value;
                    break;
                case "lrlon":
                    this.lrlon = value;
                    break;
                case "w":
                    this.w = value;
                    break;
                case "h":
                    this.h = value;
                    break;
                default:
                    String msg = String.format("%s is not a valid field for %f.%n", field, value);
                    throw new IllegalArgumentException(msg);
            }
            return this;
        }
    }

    /** Raster requests to the server must have the following keys in the params map. */
    private static final String[] REQUIRED_PARAMS = {"ullat", "ullon", "lrlat", "lrlon", "w", "h"};
}
