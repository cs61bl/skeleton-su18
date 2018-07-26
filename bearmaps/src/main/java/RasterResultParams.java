import java.util.Arrays;

/**
 * The RasterResultParams contains the fields computed by Rasterer.getMapRaster. The MapServer will
 * render the image based on the values for the renderGrid and bounding box specified in this class.
 *
 * This class uses a Builder pattern to manage its arguments and cannot be instantiated directly.
 * Use the Builder class, RasterResultParams.Builder, to create new RasterResultParams objects.
 *
 * @author Kevin Lin
 */
public class RasterResultParams {
    /** The grid of images to display. */
    public final String[][] renderGrid;
    /** The bounding upper-left, lower-right longitudes and latitudes of the final image. */
    public final double rasterUlLon, rasterUlLat, rasterLrLon, rasterLrLat;
    /** The depth of the chosen images. */
    public final int depth;
    /** True if the query was successful. */
    public final boolean querySuccess;

    /**
     * Factory method which returns a new RasterResultParams instance representing a failed query.
     * @return A RasterResultParams instance with querySuccess set to false.
     */
    public static RasterResultParams queryFailed() {
        return new RasterResultParams();
    }

    /**
     * Constructor with private access to prevent direct instantiation of a RasterResultParams
     * instance. Use the Builder instead.
     */
    private RasterResultParams() {
        this(null, 0.0, 0.0, 0.0, 0.0, 0, false);
    }

    private RasterResultParams(
            String[][] renderGrid,
            double rasterUlLon, double rasterUlLat, double rasterLrLon, double rasterLrLat,
            int depth, boolean querySuccess) {
        this.renderGrid = renderGrid;
        this.rasterUlLon = rasterUlLon;
        this.rasterUlLat = rasterUlLat;
        this.rasterLrLon = rasterLrLon;
        this.rasterLrLat = rasterLrLat;
        this.depth = depth;
        this.querySuccess = querySuccess;
    }

    @Override
    public String toString() {
        return "RasterResultParams{"
                + "rasterUlLon=" + rasterUlLon
                + ", rasterUlLat=" + rasterUlLat
                + ", rasterLrLon=" + rasterLrLon
                + ", rasterLrLat=" + rasterLrLat
                + ", depth=" + depth
                + ", querySuccess=" + querySuccess
                + ", renderGrid=" + Arrays.deepToString(renderGrid)
                + '}';
    }

    /**
     * The RasterResultParams.Builder is used to construct new RasterResultParams instances.
     * Instantiate a new Builder, set the required fields, and then construct the
     * RasterResultParams by invoking the create method.
     *
     * <pre>RasterResultParams resultParams = new RasterResultParams.Builder()
     *         .setRenderGrid(grid)
     *         .setRasterUlLon(ullon)
     *         ...
     *         .create();</pre>
     *
     * @author Kevin Lin
     */
    public static class Builder {
        /** The grid of images to display. */
        private String[][] renderGrid;
        /** The bounding upper-left, lower-right longitudes and latitudes of the final image. */
        private double rasterUlLon, rasterUlLat, rasterLrLon, rasterLrLat;
        /** The depth of the chosen images. */
        private int depth;
        /** True if the query was successful. */
        private boolean querySuccess;

        /**
         * Creates a RasterResultParams.Builder instance that can be used to build a
         * RasterResultParams objects with various fields pre-set.
         */
        public Builder() {
        }

        /**
         * Creates a RasterResultParams.Builder instance from a RasterResultParams instance with the
         * same values as the previously built RasterResultParams.
         * @param rrp RasterResultParams to copy
         */
        public Builder(RasterResultParams rrp) {
            this.renderGrid = rrp.renderGrid;
            this.rasterUlLon = rrp.rasterUlLon;
            this.rasterUlLat = rrp.rasterUlLat;
            this.rasterLrLon = rrp.rasterLrLon;
            this.rasterLrLat = rrp.rasterLrLat;
            this.depth = rrp.depth;
            this.querySuccess = rrp.querySuccess;
        }

        /**
         * Sets the value for this Builder's renderGrid.
         * @param grid The computed renderGrid.
         * @return This Builder instance.
         */
        public Builder setRenderGrid(String[][] grid) {
            this.renderGrid = grid;
            return this;
        }

        /**
         * Sets the value for this Builder's rasterUlLon.
         * @param ullon The computed upper-left longitude.
         * @return This Builder instance.
         */
        public Builder setRasterUlLon(double ullon) {
            this.rasterUlLon = ullon;
            return this;
        }

        /**
         * Sets the value for this Builder's rasterUlLat.
         * @param ullat The computed upper-left latitude.
         * @return This Builder instance.
         */
        public Builder setRasterUlLat(double ullat) {
            this.rasterUlLat = ullat;
            return this;
        }

        /**
         * Sets the value for this Builder's rasterLrLon.
         * @param lrlon The computed lower-right longitude.
         * @return This Builder instance.
         */
        public Builder setRasterLrLon(double lrlon) {
            this.rasterLrLon = lrlon;
            return this;
        }

        /**
         * Sets the value for this Builder's rasterLrLat.
         * @param lrlat The computed lower-right latitude.
         * @return This Builder instance.
         */
        public Builder setRasterLrLat(double lrlat) {
            this.rasterLrLat = lrlat;
            return this;
        }

        /**
         * Sets the value for this Builder's depth.
         * @param d The computed depth of the images.
         * @return This Builder instance.
         */
        public Builder setDepth(int d) {
            this.depth = d;
            return this;
        }

        /**
         * Sets the value for this Builder's querySuccess.
         * @param success Whether the query was a success or not.
         * @return This Builder instance.
         */
        public Builder setQuerySuccess(boolean success) {
            this.querySuccess = success;
            return this;
        }

        /**
         * Returns a validated RasterResultParams instance with the same values as this Builder.
         * @return A new RasterResultParams instance with the same values as this Builder.
         * @throws IllegalStateException If any of the RasterResultParams' values are invalid.
         */
        public RasterResultParams create() {
            RasterResultParams result = new RasterResultParams(
                    renderGrid,
                    rasterUlLon, rasterUlLat, rasterLrLon, rasterLrLat,
                    depth, querySuccess
            );
            String fmt = "Raster result field invalid: %s";
            if (result.renderGrid == null
                    || result.renderGrid.length == 0 || result.renderGrid[0].length == 0) {
                throw new IllegalStateException(String.format(fmt, "renderGrid"));
            }
            if (result.rasterUlLon < MapServer.ROOT_ULLON
                    || result.rasterUlLon > MapServer.ROOT_LRLON) {
                throw new IllegalStateException(String.format(fmt, "rasterUlLon"));
            }
            if (result.rasterUlLat > MapServer.ROOT_ULLAT
                    || result.rasterUlLat < MapServer.ROOT_LRLAT) {
                throw new IllegalStateException(String.format(fmt, "rasterUlLat"));
            }
            if (result.rasterLrLon < MapServer.ROOT_ULLON
                    || result.rasterLrLon > MapServer.ROOT_LRLON) {
                throw new IllegalStateException(String.format(fmt, "rasterLrLon"));
            }
            if (result.rasterLrLat > MapServer.ROOT_ULLAT
                    || result.rasterLrLat < MapServer.ROOT_LRLAT) {
                throw new IllegalStateException(String.format(fmt, "rasterLrLat"));
            }
            if (result.depth < 0 || result.depth > Rasterer.MAX_DEPTH) {
                throw new IllegalStateException(String.format(fmt, "depth"));
            }
            if (!result.querySuccess) {
                throw new IllegalStateException(String.format(fmt, "querySuccess"));
            }
            return result;
        }
    }
}
