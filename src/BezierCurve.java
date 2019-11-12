import java.util.ArrayList;
import java.util.List;

public class BezierCurve {

    private final List<Point> mPoints;
    private final int degree;
    private final double STEP = 0.005;
    private List<Integer> coeffs;

    public BezierCurve(List<Point> points) {
        this.mPoints = points;
        degree = points.size() - 1;
    }

    public List<Point> curve() {
        if (degree < 1) return null;

        computeBinomialCoefficients();
        List<Point> curve = new ArrayList<>((int) (1 / STEP + 1));

        for (double t = 0; t <= 1; t += STEP) {
            double x = 0;
            double y = 0;
            for (int i = 0; i <= degree; i++) {
                double factor = factor(t, i, degree);
                Point point = mPoints.get(i);
                x += factor * point.x;
                y += factor * point.y;
            }
            curve.add(new Point(x, y));
        }

        return curve;
    }

    private double factor(double t, int i, int n) {
        return coeffs.get(i) * Math.pow(t, i) * Math.pow(1 - t, n - i);
    }

    private void computeBinomialCoefficients() {
        coeffs = new ArrayList<>(degree + 1);
        coeffs.add(1);
        for (int i = 0; i < degree; i++) {
            coeffs.add(coeffs.get(i) * (degree - i) / (i + 1));
        }
    }
}
