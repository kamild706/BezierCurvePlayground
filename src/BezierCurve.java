import java.util.ArrayList;
import java.util.List;

class BezierCurve {

    private final List<Point> mPoints;
    private List<Long> mCoeffs;
    private final int mDegree;
    private static final double STEP = 0.01;

    BezierCurve(List<Point> points) {
        mPoints = points;
        mDegree = points.size() - 1;
    }

    List<Point> curve() {
        if (mDegree < 1) return null;

        computeBinomialCoefficients();
        List<Point> curve = new ArrayList<>((int) (1 / STEP + 1));

        for (double t = 0; t <= 1; t += STEP) {
            double x = 0;
            double y = 0;
            for (int i = 0; i <= mDegree; i++) {
                double factor = factor(t, i, mDegree);
                Point point = mPoints.get(i);
                x += factor * point.x;
                y += factor * point.y;
            }
            curve.add(new Point(x, y));
        }

        return curve;
    }

    private double factor(double t, int i, int n) {
        return mCoeffs.get(i) * Math.pow(t, i) * Math.pow(1 - t, n - i);
    }

    private void computeBinomialCoefficients() {
        mCoeffs = new ArrayList<>(mDegree + 1);
        mCoeffs.add(1L);
        for (int i = 0; i < mDegree; i++) {
            mCoeffs.add(mCoeffs.get(i) * (mDegree - i) / (i + 1));
        }
    }
}
