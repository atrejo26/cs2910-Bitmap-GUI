import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class RecentColorsPanel extends JPanel {
    private static final int MAX_COLORS = 5;
    private ArrayList<Color> colors;
    private JButton[] swatches;
    private ColorChooserPanel chooser;

    public RecentColorsPanel(ColorChooserPanel chooser) {
        this.chooser = chooser;
        colors = new ArrayList<Color>();
        swatches = new JButton[MAX_COLORS];

        setLayout(new GridLayout(1, MAX_COLORS, 4, 0));

        for (int i = 0; i < MAX_COLORS; i++) {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(26, 26));
            b.setOpaque(true);
            b.setBorderPainted(false);
            b.setEnabled(false);

            final int index = i;
            b.addActionListener(e -> this.chooser.setSelectedColor(colors.get(index)));

            swatches[i] = b;
            add(b);
        }
    }

    // Adds a color to the front of the list, with no duplicates and at most 5
    public void addColor(Color c) {
        colors.remove(c);
        colors.add(0, c);
        if (colors.size() > MAX_COLORS) {
            colors.remove(MAX_COLORS);
        }

        for (int i = 0; i < colors.size(); i++) {
            swatches[i].setBackground(colors.get(i));
            swatches[i].setEnabled(true);
        }
    }
}