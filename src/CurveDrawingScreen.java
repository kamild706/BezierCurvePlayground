import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CurveDrawingScreen implements Subject<List<Point>> {

    private final Set<Observer<? super List<Point>>> mObservers = new HashSet<>();
    private final List<Point> mPoints = new ArrayList<>();
    private CurveArea mCurveArea;


    private void addPoint(Point point) {
        mPoints.add(point);
        mCurveArea.redraw();
        dispatchingValue(mPoints);
    }

    private void dispatchingValue(List<Point> points) {
        mObservers.forEach(observer -> observer.onChanged(points));
    }

    Component getComponent() {
        mCurveArea = new CurveArea();
        return mCurveArea;
    }

    @Override
    public void observe(@NotNull Observer<List<Point>> observer) {
        mObservers.add(observer);
    }

    void setPointsSource(Subject<List<Point>> source) {
        source.observe(points -> {
            mPoints.clear();
            mPoints.addAll(points);
            mCurveArea.redraw();
        });
    }

    private void substitutePoints(Point oldPoint, Point newPoint) {
        int index = mPoints.indexOf(oldPoint);
        if (index > -1) {
            mPoints.remove(index);
            mPoints.add(index, newPoint);
            mCurveArea.redraw();
            dispatchingValue(mPoints);
        }
    }

    class CurveArea extends JComponent {

        private Image mImage;
        private Graphics2D mGraphics2D;
        private Integer prevX = null;
        private Integer prevY = null;
        private Point draggedPoint = null;

        CurveArea() {
            setDoubleBuffered(false);
            initializeMouseListeners();
        }

        void clear() {
            mGraphics2D.setPaint(Color.white);
            mGraphics2D.fillRect(0, 0, getSize().width, getSize().height);
            mGraphics2D.setPaint(Color.black);
        }

        private void initializeMouseListeners() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    onMousePressed(e);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    onMouseReleased(e);
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);
                    onMouseDragged(e);
                }
            });
        }

        private void onMouseDragged(MouseEvent e) {
            if (draggedPoint == null) return;

            int currX = e.getX();
            int currY = e.getY();
            int dx = currX - prevX;
            int dy = currY - prevY;
            prevX = currX;
            prevY = currY;

            Point point = new Point(draggedPoint.x + dx, draggedPoint.y + dy);
            substitutePoints(draggedPoint, point);
            draggedPoint = point;
        }

        private void onMouseReleased(MouseEvent e) {
            if (draggedPoint == null) {
                int x = e.getX();
                int y = e.getY();
                Point point = new Point(x, y);
                addPoint(point);
            }
            draggedPoint = null;
        }

        private void onMousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            for (Point point : mPoints) {
                if (point.intersects(x, y)) {
                    draggedPoint = point;
                    prevX = x;
                    prevY = y;
                    return;
                }
            }
            draggedPoint = null;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (mImage == null) {
                mImage = createImage(getSize().width, getSize().height);
                mGraphics2D = (Graphics2D) mImage.getGraphics();
                mGraphics2D.setStroke(new BasicStroke(4));
                clear();
                repaint();
            }
            g.drawImage(mImage, 0, 0, null);
        }

        private Path2D pointsToPath(List<Point> points) {
            if (points.size() == 0) return null;

            Path2D path = new Path2D.Double();
            Point first = points.get(0);
            path.moveTo(first.x, first.y);
            for (int i = 1; i < points.size(); i++) {
                Point point = points.get(i);
                path.lineTo(point.x, point.y);
            }
            return path;
        }

        private void redraw() {
            clear();
            mPoints.forEach(point -> mGraphics2D.drawOval(point.x, point.y, 8, 8));

            BezierCurve bezierCurve = new BezierCurve(mPoints);
            List<Point> curve = bezierCurve.curve();
            if (curve != null) {
                Path2D path = pointsToPath(curve);
                mGraphics2D.draw(path);
            }
            repaint();
        }
    }
}
