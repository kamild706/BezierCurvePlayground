public class Point {

    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(double x, double y) {
        this.x = (int) x;
        this.y = (int) y;
    }

    public boolean intersects(int px, int py) {
        double dx = Math.pow(px - x, 2);
        double dy = Math.pow(py - y, 2);
        double distance = Math.sqrt(dx + dy);
        return distance < 25;
    }
}
