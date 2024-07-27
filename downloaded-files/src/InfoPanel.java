package src;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

/**
 * Info Panel displays shapes as SVG elements
 *
 * @author: Pranay Tiru
 */
public class InfoPanel extends JPanel implements Observer {
    private JTextArea textArea;
    private String beginningStr = "<svg xmlns=\"https://www.w3.org/2000/svg\" width=\"800\" height = \"600\">";
    private String endStr = "</svg>";

    public InfoPanel() {
        setLayout(new BorderLayout());
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        add(new JScrollPane(textArea), BorderLayout.CENTER);
        setPreferredSize(new Dimension(200, 600));
    }

    @Override
    public void update(Stack<DrawAction> currentStack) {
        StringBuilder sb = new StringBuilder();
        sb.append(beginningStr).append("\n");
        for (DrawAction action : currentStack) {
            sb.append(action.toString()).append("\n");
        }
        sb.append(endStr);
        textArea.setText(sb.toString());
    }
}