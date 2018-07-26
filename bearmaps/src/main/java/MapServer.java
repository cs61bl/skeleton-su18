import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static spark.Spark.*;

/**
 * This MapServer class is the entry point for running the JavaSpark web server for the BearMaps
 * application project, receiving API calls, handling the API call processing, and generating
 * requested images and routes. You should not need to modify this file, though you're welcome to
 * make changes as you see fit.
 *
 * @author Alan Yao, Josh Hug, Kevin Lin
 */
public class MapServer {
    /**
     * The root upper left/lower right longitudes and latitudes represent the bounding box of the
     * root tile, as the images are scraped.
     *
     * Longitude refers to the x-axis (vertical lines).
     * Latitude refers to the y-axis (horizontal lines).
     */
    public static final double ROOT_ULLAT = 37.892195547244356, ROOT_ULLON = -122.2998046875,
            ROOT_LRLAT = 37.82280243352756,  ROOT_LRLON = -122.2119140625;
    /** Each tile is 256x256 pixels. */
    public static final int TILE_SIZE = 256;
    /** The root longitude distance per pixel. */
    public static final double ROOT_LONDPP = (ROOT_LRLON - ROOT_ULLON) / TILE_SIZE;
    /** The difference between the left-most and right-most longitudes of the map. */
    public static final double ROOT_LON_DELTA = Math.abs(ROOT_ULLON - ROOT_LRLON);
    /** The difference between the upper-most and lower-most latitudes of the map. */
    public static final double ROOT_LAT_DELTA = Math.abs(ROOT_ULLAT - ROOT_LRLAT);

    /** The <code>GraphDB</code>responsible for managing map data. */
    private static GraphDB graph;
    /** The <code>Rasterer</code> responsible for computing the shortest path and directions. */
    private static Rasterer rasterer;
    /**
     * The most recently-requested shortest-paths route. The <code>renderImage</code> method redraws
     * this route every time a new rastering result is requested from the browser.
     */
    private static List<Long> route;
    /** The configured Gson Java serializer. */
    private static Gson gson;

    /**
     * Code responsible for initializing variables. These objects must be instantiated
     * independently of the main method for the tests to function properly.
     */
    public static void initialize() {
        graph = new GraphDB(OSM_DB_PATH);
        rasterer = new Rasterer();
        route = Collections.emptyList();
        gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    /**
     * Launch the <code>mapServer</code>, register server routes, and listen on the default port.
     * @param args Ignored
     */
    public static void main(String[] args) {
        initialize();
        staticFileLocation("/page");
        /* Allow for all origin requests since this is not an authenticated server. */
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "*");
            response.header("Access-Control-Allow-Headers", "*");
        });

        /* Define the raster endpoint for HTTP GET requests. */
        get("/raster", (req, res) -> {
            RasterResultParams resultParams = null;
            try {
                RasterRequestParams params = RasterRequestParams.from(req.queryMap().toMap());
                resultParams = rasterer.getMapRaster(params);
            } catch (IllegalArgumentException e) {
                halt(HALT_RESPONSE, e.getMessage());
            }
            try {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                BufferedImage img = renderImage(resultParams);
                ImageIO.write(img, "png", os);
                return gson.toJson(
                        new RenderedRasterResultParams(
                                resultParams, img.getWidth(), img.getHeight(),
                                Base64.getEncoder().encodeToString(os.toByteArray())
                        ));
            } catch (IOException e) {
                e.printStackTrace();
            }
            /* Return the best-effort resultParams when unable to render image. */
            return gson.toJson(resultParams);
        });

        /* Define the routing endpoint for HTTP GET requests. */
        get("/route", (req, res) -> {
            RouteRequestParams  params = null;
            try {
                params = RouteRequestParams.from(req.queryMap().toMap());
            } catch (IllegalArgumentException e) {
                halt(HALT_RESPONSE, e.getMessage());
            }
            route = Router.shortestPath(graph,
                    params.startLon, params.startLat, params.endLon, params.endLat);
            String directions = getDirectionsText(Router.routeDirections(graph, route));
            RouteResultParams routeParams = new RouteResultParams(!route.isEmpty(), directions);
            return gson.toJson(routeParams);
        });

        /* Define the API endpoint for clearing the current route. */
        get("/clear_route", (req, res) -> {
            route = Collections.emptyList();
            return true;
        });

        /* Define the API endpoint for search */
        get("/search", (req, res) -> {
            String term = req.queryParams("term");
            /* Search for actual location data. */
            if (req.queryParams().contains("full")) {
                List<LocationParams> data = graph.getLocations(term);
                return gson.toJson(data);
            } else {
                /* Search for prefix matching strings. */
                List<String> matches = graph.getLocationsByPrefix(term);
                return gson.toJson(matches);
            }
        });

        /* Define map application redirect */
        get("/", (request, response) -> {
            response.redirect("/map.html", 301);
            return true;
        });
    }

    /**
     * Return the image defined by the <code>resultParams</code>.
     * @param resultParams <code>RasterResultParams</code> from <code>Rasterer.getMapRaster</code>
     * @return The final, rastered image including any shortest-paths routes.
     */
    private static BufferedImage renderImage(RasterResultParams resultParams) {
        String[][] renderGrid = resultParams.renderGrid;
        int numVertTiles = renderGrid.length;
        int numHorizTiles = renderGrid[0].length;

        BufferedImage img = new BufferedImage(numHorizTiles * MapServer.TILE_SIZE,
                numVertTiles * MapServer.TILE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics graphic = img.getGraphics();
        int x = 0, y = 0;
        for (int r = 0; r < numVertTiles; r += 1) {
            for (int c = 0; c < numHorizTiles; c += 1) {
                graphic.drawImage(readImage(IMG_ROOT + renderGrid[r][c]), x, y, null);
                x += MapServer.TILE_SIZE;
                if (x >= img.getWidth()) {
                    x = 0;
                    y += MapServer.TILE_SIZE;
                }
            }
        }
        /* If there is a route, draw it. */
        if (route != null && !route.isEmpty()) {
            double ullon = resultParams.rasterUlLon;
            double ullat = resultParams.rasterUlLat;
            double lrlon = resultParams.rasterLrLon;
            double lrlat = resultParams.rasterLrLat;
            double wdpp = (lrlon - ullon) / img.getWidth();
            double hdpp = (ullat - lrlat) / img.getHeight();
            Graphics2D g2d = (Graphics2D) graphic;
            g2d.setColor(MapServer.ROUTE_STROKE_COLOR);
            g2d.setStroke(new BasicStroke(MapServer.ROUTE_STROKE_WIDTH_PX,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            route.stream().reduce((v, w) -> {
                g2d.drawLine((int) ((graph.lon(v) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(v)) * (1 / hdpp)),
                        (int) ((graph.lon(w) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(w)) * (1 / hdpp)));
                return w;
            });
        }
        return img;
    }

    /**
     * Returns the image found at the given <code>imgPath</code>.
     * @param imgPath <code>String</code> path to the image.
     * @return The <code>BufferedImage</code> at the specified <code>imgPath</code>.
     */
    private static BufferedImage readImage(String imgPath) {
        BufferedImage tileImg = null;
        File in = new File(imgPath);
        try {
            tileImg = ImageIO.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tileImg;
    }

    /**
     * Returns the HTML-friendly <code>Stirng</code> representation of the route.
     * @param directions The <code>List</code> of <code>NavigationDirections</code>.
     * @return An HTML-encoded <code>String</code> of the list of navigation directions.
     */
    private static String getDirectionsText(List<Router.NavigationDirection> directions) {
        StringBuilder sb = new StringBuilder();
        int step = 1;
        for (Router.NavigationDirection d: directions) {
            sb.append(String.format("%d. %s <br>", step, d));
            step += 1;
        }
        return sb.toString();
    }

    /** HTTP failed response. */
    private static final int HALT_RESPONSE = 403;
    /** Route stroke information: typically roads are not more than 5px wide. */
    private static final float ROUTE_STROKE_WIDTH_PX = 5.0f;
    /** Route stroke information: semi-transparent cyan. */
    private static final Color ROUTE_STROKE_COLOR = new Color(108, 181, 230, 200);
    /** The directory where tile images can be found. */
    private static final String IMG_ROOT = "../library-su18/bearmaps/img/";
    /**
     * The OSM XML file path. Downloaded from <a href="http://download.bbbike.org/osm/">here</a>
     * using custom region selection.
     */
    private static final String OSM_DB_PATH = "../library-su18/bearmaps/berkeley-2018.osm.xml";

    /** An adapter class for extending RasterResultParams with the final image. */
    private static class RenderedRasterResultParams {
        private final String[][] renderGrid;
        private final double rasterUlLon, rasterUlLat, rasterLrLon, rasterLrLat;
        private final int depth;
        private final boolean querySuccess;
        private final int rasterWidth, rasterHeight;
        private final String b64EncodedImageData;

        private RenderedRasterResultParams(
                RasterResultParams params, int rasterWidth, int rasterHeight, String encodedImage) {
            this.renderGrid = params.renderGrid;
            this.rasterUlLon = params.rasterUlLon;
            this.rasterUlLat = params.rasterUlLat;
            this.rasterLrLon = params.rasterLrLon;
            this.rasterLrLat = params.rasterLrLat;
            this.depth = params.depth;
            this.querySuccess = params.querySuccess;
            this.rasterWidth = rasterWidth;
            this.rasterHeight = rasterHeight;
            this.b64EncodedImageData = encodedImage;
        }
    }
}
