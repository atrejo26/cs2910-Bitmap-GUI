import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AdvancedDialog extends JDialog {
    private BitmapGUI gui;
    private int startRow;
    private int startCol;
    private JTextField rowsField;
    private JTextField colsField;
    private BufferedImage selectedImage;
    private JLabel previewLabel;
    private JButton importOkButton;

    public AdvancedDialog(BitmapGUI gui, int startRow, int startCol) {
        super(gui, "Advanced", true);
        this.gui = gui;
        this.startRow = startRow;
        this.startCol = startCol;

        setLayout(new BorderLayout());

        JLabel startLabel = new JLabel("Starting at row " + startRow + ", column " + startCol);
        startLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(startLabel, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Fill", buildFillTab());
        tabs.addTab("Import", buildImportTab());
        add(tabs, BorderLayout.CENTER);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(gui);
    }

    private JPanel buildFillTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        rowsField = new JTextField(5);
        colsField = new JTextField(5);
        JPanel form = new JPanel(new GridLayout(2, 2, 5, 5));
        form.add(new JLabel("Rows:"));
        form.add(rowsField);
        form.add(new JLabel("Cols:"));
        form.add(colsField);
        panel.add(form, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> applyFill());
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonRow.add(okButton);
        panel.add(buttonRow, BorderLayout.SOUTH);

        return panel;
    }
    // Import tab: "Import a file" button, preview box, OK button, in one row
    private JPanel buildImportTab() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton chooseButton = new JButton("Import a file");
        chooseButton.addActionListener(e -> chooseFile());

        previewLabel = new JLabel("No file selected", SwingConstants.CENTER);
        previewLabel.setPreferredSize(new Dimension(200, 200));
        previewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        importOkButton = new JButton("OK");
        importOkButton.setEnabled(false);
        importOkButton.addActionListener(e -> applyImport());

        panel.add(chooseButton);
        panel.add(previewLabel);
        panel.add(importOkButton);

        return panel;
    }
    //Runs when the Fill tab's OK button is pressed
    private void applyFill() {
        int numRows = readPositiveInt(rowsField);
        int numCols = readPositiveInt(colsField);

        if (numRows > 0 && numCols > 0) {
            gui.fillArea(startRow, startCol, numRows, numCols);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Rows and columns must be positive whole numbers.");
        }
    }
    // Reads a text box as a positive int. Returns -1 if it's blank, not a number, or <= 0.
    private int readPositiveInt(JTextField field) {
        int retVal = -1;
        try {
            int n = Integer.parseInt(field.getText().trim());
            if (n > 0) {
                retVal = n;
            }
        } catch (NumberFormatException e) {
            retVal = -1;
        }
        return retVal;
    }
    // Opens a file chooser, loads the chosen bitmap, and shows it in the preview
    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Bitmap images (*.bmp)", "bmp"));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            BufferedImage img = BitmapFile.load(chooser.getSelectedFile());

            if (img == null) {
                JOptionPane.showMessageDialog(this, "That file isn't a readable bitmap.");
            } else {
                selectedImage = img;
                previewLabel.setText("");
                previewLabel.setIcon(makePreview(img));
                importOkButton.setEnabled(true);
            }
        }
    }
    // Scales an image to fit inside the 200x200 preview box
    private ImageIcon makePreview(BufferedImage img) {
        double scale = Math.min(200.0 / img.getWidth(), 200.0 / img.getHeight());
        int w = Math.max(1, (int) (img.getWidth() * scale));
        int h = Math.max(1, (int) (img.getHeight() * scale));

        Image scaled = img.getScaledInstance(w, h, Image.SCALE_REPLICATE);
        ImageIcon retVal = new ImageIcon(scaled);
        return retVal;
    }
    // Runs when the Import tab's OK button is pressed
    private void applyImport() {
        gui.importImage(startRow, startCol, selectedImage);
        dispose();
    }
}