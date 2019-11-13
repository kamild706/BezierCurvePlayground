import javax.swing.*;
import java.awt.*;

public class App {

    private static final int WIDTH = 1900;
    private static final int HEIGHT = 1000;

    private void start() {
        JFrame frame = new JFrame("Bezier Curve demo");

        Container container = frame.getContentPane();
        container.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(leftPanel, BoxLayout.Y_AXIS);
        leftPanel.setLayout(boxLayout);
        leftPanel.setPreferredSize(new Dimension(300, HEIGHT));

        PointsDetailsEditor pointsDetailsEditor = new PointsDetailsEditor();
        leftPanel.add(pointsDetailsEditor.getComponent());
        container.add(leftPanel, BorderLayout.WEST);

        CurveDrawingScreen curveDrawingScreen = new CurveDrawingScreen();
        container.add(curveDrawingScreen.getComponent(), BorderLayout.CENTER);

        pointsDetailsEditor.setPointsSource(curveDrawingScreen);
        curveDrawingScreen.setPointsSource(pointsDetailsEditor);

        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new App().start();
    }
}
