import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeListener;

public class ColorChooserPanel extends JPanel {
    private JSlider redSlider;
    private JSlider greenSlider;
    private JSlider blueSlider;
    private JPanel preview;

    public ColorChooserPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        redSlider = makeSlider();
        greenSlider = makeSlider();
        blueSlider = makeSlider();

        preview = new JPanel();
        preview.setPreferredSize(new Dimension(150, 50));
        preview.setMaximumSize(new Dimension(150, 50));
        preview.setBackground(Color.BLACK);
        preview.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        ChangeListener update = e -> preview.setBackground(getSelectedColor());
        redSlider.addChangeListener(update);
        greenSlider.addChangeListener(update);
        blueSlider.addChangeListener(update);

        add(new JLabel("Red"));
        add(redSlider);
        add(new JLabel("Green"));
        add(greenSlider);
        add(new JLabel("Blue"));
        add(blueSlider);
        add(new JLabel("Preview"));
        add(preview);
    }

    // Builds one slider: range 0-255, starting at 0, with tick marks and numbers
    private JSlider makeSlider() {
        JSlider retVal = new JSlider(0, 255, 0);
        retVal.setMajorTickSpacing(85); // labels at 0, 85, 170, 255
        retVal.setPaintTicks(true);
        retVal.setPaintLabels(true);
        return retVal;
    }

    // Combines the three slider values into one Color
    public Color getSelectedColor() {
        Color retVal = new Color(redSlider.getValue(), greenSlider.getValue(),
                blueSlider.getValue());
        return retVal;
    }

    // Moves the sliders to match a color moving them fires the ChangeListener so the preview updates automatically
    public void setSelectedColor(Color c) {
        redSlider.setValue(c.getRed());
        greenSlider.setValue(c.getGreen());
        blueSlider.setValue(c.getBlue());
    }
}