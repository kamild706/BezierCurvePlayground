import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Path2D;
import java.util.List;

public class DrawingArea extends JComponent {

    private static final int OVAL_WIDTH = 8;
    private static final int OVAL_HEIGHT = 8;
    private Main main;
    private Image image;
    private Graphics2D graphics2D;
    public static final int DRAWING = 0;
    public int mode = DRAWING;
    private  Point draggedPoint = null;
    private Integer prevX = null;
    private Integer prevY = null;

    public DrawingArea(Main main) {
        this.main = main;
        setDoubleBuffered(false);
        initializeMouseListener();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null) {
            image = createImage(getSize().width, getSize().height);
            graphics2D = (Graphics2D) image.getGraphics();
            graphics2D.setStroke(new BasicStroke(4));
            clear();
        }
        g.drawImage(image, 0, 0, null);
    }

    private void initializeMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                onMousePressedWhenMoving(e);

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                onMouseReleasedWhenDrawing(e);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (draggedPoint == null) return;

                int currX = e.getX();
                int currY = e.getY();
                int dx = currX - prevX;
                int dy = currY - prevY;
                prevX = currX;
                prevY = currY;

                draggedPoint.x = draggedPoint.x + dx;
                draggedPoint.y = draggedPoint.y + dy;
                redraw();
                main.refreshInputArea();
            }
        });
    }

    private void onMousePressedWhenMoving(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        for (Point point : main.points) {
            if (point.intersects(x, y)) {
                draggedPoint = point;
                prevX = x;
                prevY = y;
                return;
            }
        }
        draggedPoint = null;
    }

    private void onMouseReleasedWhenDrawing(MouseEvent event) {
        if (draggedPoint == null) {
            int x = event.getX();
            int y = event.getY();
            Point point = new Point(x, y);
            main.addPoint(point);
        }
        draggedPoint = null;
    }

    public void redraw() {
        clear();

        main.points.forEach(p -> {
            graphics2D.drawOval(p.x, p.y, OVAL_WIDTH, OVAL_HEIGHT);
        });

        BezierCurve bezierCurve = new BezierCurve(main.points);
        List<Point> curve = bezierCurve.curve();
        if (curve != null) {
            Path2D path = pointsToPath(curve);
            graphics2D.draw(path);
        }
        repaint();

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

    private void clear() {
        graphics2D.setPaint(Color.white);
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        graphics2D.setPaint(Color.black);
        repaint();
    }
}
