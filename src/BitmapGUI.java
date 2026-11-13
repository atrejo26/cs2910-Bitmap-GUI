import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.*;

public class BitmapGUI extends JFrame {
    private int rows;
    private int cols;
    private Icon icon;
    private PixelGridPanel gridPanel;
    private ColorChooserPanel colorChooser;
    private JCheckBox advancedBox;
    private RecentColorsPanel recentColors;

    public BitmapGUI(int rows, int cols) {
        super("Bitmap Editor");
        this.rows = rows;
        this.cols = cols;
        icon = new Icon(rows, cols);

        gridPanel = new PixelGridPanel(rows, cols, this);
        colorChooser = new ColorChooserPanel();
        recentColors = new RecentColorsPanel(colorChooser);
        advancedBox = new JCheckBox("Advanced");

        setLayout(new BorderLayout());
        add(buildLeftSide(), BorderLayout.CENTER);
        add(buildRightSide(), BorderLayout.EAST);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel buildLeftSide() {
        JPanel left = new JPanel(new BorderLayout(0, 10));
        left.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        left.add(new JScrollPane(gridPanel), BorderLayout.CENTER);
        left.add(advancedBox, BorderLayout.SOUTH);
        return left;
    }

    private JPanel buildRightSide() {
        JPanel right = new JPanel(new BorderLayout());
        right.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(colorChooser);
        top.add(Box.createVerticalStrut(10));
        top.add(new JLabel("Last five colors"));
        top.add(recentColors);

        right.add(top, BorderLayout.NORTH);

        JButton saveButton = new JButton("Create Bitmap File");
        saveButton.addActionListener(e -> saveBitmap());
        right.add(saveButton, BorderLayout.SOUTH);

        return right;
    }
    // Called by PixelGridPanel when a button is clicked
    public void handleGridClick(int row, int col) {
        if (advancedBox.isSelected()) {
            // Advanced mode: open the dialog instead of coloring this pixel
            AdvancedDialog dialog = new AdvancedDialog(this, row, col);
            dialog.setVisible(true);
        } else {
            Color c = colorChooser.getSelectedColor();
            colorPixel(row, col, c);
            recentColors.addColor(c);
        }
    }

    private void colorPixel(int row, int col, Color c) {
        icon.setPixel(row, col, new Pixel(c.getRed(), c.getGreen(), c.getBlue()));
        gridPanel.setButtonColor(row, col, c);
    }
    
    private void saveBitmap() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose a folder to save in");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File folder = chooser.getSelectedFile();

            String name = JOptionPane.showInputDialog(this, "File name:");
            if (name != null && !name.trim().isEmpty()) {
                name = name.trim();
                if (!name.toLowerCase().endsWith(".bmp")) {
                    name = name + ".bmp";
                }

                File out = new File(folder, name);
                if (BitmapFile.save(icon, rows, cols, out)) {
                    JOptionPane.showMessageDialog(this, "Saved to " + out.getAbsolutePath());
                } else {
                    JOptionPane.showMessageDialog(this, "Save failed.");
                }
            }
        }
    }
    // Fills numRows down and numCols to the right, starting at (startRow, startCol).
    public void fillArea(int startRow, int startCol, int numRows, int numCols) {
        Color c = colorChooser.getSelectedColor();

        int endRow = Math.min(rows, startRow + numRows);
        int endCol = Math.min(cols, startCol + numCols);

        for (int r = startRow; r < endRow; r++) {
            for (int col = startCol; col < endCol; col++) {
                colorPixel(r, col, c);
            }
        }
        recentColors.addColor(c);
    }
    // Copies the image's pixels in, with its top-left at (startRow, startCol).
    public void importImage(int startRow, int startCol, BufferedImage img) {
        // Images use (x, y) = (col, row). The extra conditions stop at the grid's edge.
        for (int y = 0; y < img.getHeight() && startRow + y < rows; y++) {
            for (int x = 0; x < img.getWidth() && startCol + x < cols; x++) {
                Color c = new Color(img.getRGB(x, y));
                colorPixel(startRow + y, startCol + x, c);
            }
        }
    }
}