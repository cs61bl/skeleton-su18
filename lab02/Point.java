/** A point in 2D space. */
public class Point {

    public double x;
    public double y;

    /** A constructor that returns a point at the origin. */
    Point() {
        this.x = 0;
        this.y = 0;
    }

    /** A constructor that takes in the x, y coordinate of a point. */
    Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /** A constructor that creates a point with the same x and y values. */
    Point(Point other) {
        this.x = other.x;
        this.y = other.y;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}
