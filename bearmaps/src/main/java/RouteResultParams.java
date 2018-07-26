/**
 * The RouteResultParams contains the fields computed by Router.shortestPath and
 * Router.routeDirections (stored as an HTML-friendly String) for serialization by Gson.
 *
 * The fields in this class cannot be accessed as it is only used to serialize results in MapServer.
 *
 * @author Kevin Lin
 */
class RouteResultParams {
    /** Whether or not the route was successfully computed. */
    private final boolean routingSuccess;
    /** The HTML-friendly String representation of the navigation directions. */
    private final String directions;

    /** Private constructor to prevent direct instantiation of a RouteResultParams instance. */
    private RouteResultParams() {
        this(false, "");
    }

    /**
     * Constructs a RouteResultParams instance and sets the routingSuccess and distance fields.
     * @param routingSuccess The routingSuccess field.
     * @param directions The directions field.
     */
    RouteResultParams(boolean routingSuccess, String directions) {
        this.routingSuccess = routingSuccess;
        this.directions = directions;
    }
}
