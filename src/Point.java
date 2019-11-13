class Point {

    final int x;
    final int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Point(double x, double y) {
        this.x = (int) x;
        this.y = (int) y;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public int hashCode() {
        return x * 31 + y * 7;
    }

    boolean intersects(int px, int py) {
        double dx = Math.pow(px - x, 2);
        double dy = Math.pow(py - y, 2);
        double distance = Math.sqrt(dx + dy);
        return distance < 25;
    }

    Point movedBy(int dx, int dy) {
        return new Point(this.x + dx, this.y + dy);
    }
}
