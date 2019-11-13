import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

class PointsDetailsEditor extends AbstractSubject<List<Point>> {

    private static final String ACTION_CTRL_ENTER = "ACTION_CTRL_ENTER";
    private JTextArea mTextArea;

    private JButton createApplyButton() {
        JButton button = new JButton("Apply");
        button.addActionListener((event) -> onApply());
        return button;
    }

    private void createTextArea() {
        JTextArea area = new JTextArea();
        area.setPreferredSize(new Dimension(250, 500));

        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onApply();
            }
        };

        area.getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK),
                ACTION_CTRL_ENTER);
        area.getActionMap().put(ACTION_CTRL_ENTER, action);

        mTextArea = area;
    }

    Component getComponent() {
        JPanel panel = new JPanel();
        createTextArea();
        panel.add(mTextArea);
        panel.add(createApplyButton());
        return panel;
    }

    private void onApply() {
        List<Point> points = textAreaToPointsList();
        if (points != null) {
            dispatchingValue(points);
        }
    }

    void setPointsSource(Subject<List<Point>> source) {
        source.observe(points -> {
            StringBuilder sb = new StringBuilder();
            String separator = " ".repeat(10);
            points.forEach(point -> {
                String coords = String.format("%d%s%d\n", point.x, separator, point.y);
                sb.append(coords);
            });
            mTextArea.setText(sb.toString());
        });
    }

    private List<Point> textAreaToPointsList() {
        List<Point> points = new ArrayList<>();
        String input = mTextArea.getText();
        String[] lines = input.split("\n");

        for (String line : lines) {
            String[] coords = line.split("\\s+");
            if (coords.length != 2) return null;
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            Point point = new Point(x, y);
            points.add(point);
        }

        return points;
    }
}
