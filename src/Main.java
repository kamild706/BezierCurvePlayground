import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    private static final int WIDTH = 1900;
    private static final int HEIGHT = 1000;

    private DrawingArea drawingArea;
    private JPanel controls;
    private JTextArea inputArea;

    public List<Point> points = new ArrayList<>();

    private void start() {
        JFrame frame = new JFrame("Zadanie 6");
        Container container = frame.getContentPane();
        container.setLayout(new BorderLayout());

        drawingArea = new DrawingArea(this);
        container.add(drawingArea, BorderLayout.CENTER);

        controls = new JPanel();
        BoxLayout boxLayout = new BoxLayout(controls, BoxLayout.Y_AXIS);
        controls.setLayout(boxLayout);
        controls.setPreferredSize(new Dimension(300, HEIGHT));
        controls.add(createInputArea());

        container.add(controls, BorderLayout.WEST);
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private Component createInputArea() {
        JPanel container = new JPanel();

        JTextArea area = new JTextArea();
        this.inputArea = area;
        area.setPreferredSize(new Dimension(250, 500));

        Action action = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                handleAccept(null);
            }
        };

        area.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_MASK), "myAction");
        area.getActionMap().put("myAction", action);



        container.add(area);

        JButton button = new JButton("Zastosuj");
        button.addActionListener(this::handleAccept);
        container.add(button);

        return container;
    }

    private void handleAccept(ActionEvent event) {
        List<Point> newPoints = new ArrayList<>();
        String input = inputArea.getText();
        String[] lines = input.split("\n");

        for (String line : lines) {
            String[] coords = line.split("\\s+");
            if (coords.length != 2) return;
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            Point point = new Point(x, y);
            newPoints.add(point);
        }

        points.clear();
        points.addAll(newPoints);
        drawingArea.redraw();
    }

    public static void main(String[] args) {
        new Main().start();
    }

    public void addPoint(Point point) {
        points.add(point);
        refreshInputArea();
        drawingArea.redraw();
    }

    public void refreshInputArea() {
        StringBuilder sb = new StringBuilder();
        String separator = String.join("", Collections.nCopies(10, " "));
        points.forEach(point -> {
            String coords = String.format("%d%s%d\n", point.x, separator, point.y);
            sb.append(coords);
        });
        inputArea.setText(sb.toString());
    }
}
