import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.*;

public class BitmapGUI extends JFrame implements EditObserver {

    private int rows;
    private int cols;
    private Icon icon;
    private PixelGridPanel gridPanel;
    private ColorChooserPanel colorChooser;
    private JCheckBox advancedBox;
    private RecentColorsPanel recentColors;

    // Observer pattern
    private IconEdit iconEdit;
    private JCheckBox detachedBox;

    // Decorator pattern
    private JCheckBox verticalBox;
    private JCheckBox horizontalBox;
    private JCheckBox randomBox;

    // Constructor for the first editor
    public BitmapGUI(int rows, int cols) {
        this(rows, cols, new IconEdit());
    }

    // Constructor for additional editors sharing the same subject
    public BitmapGUI(int rows, int cols, IconEdit iconEdit) {
        super("Bitmap Editor");

        this.rows = rows;
        this.cols = cols;
        this.iconEdit = iconEdit;

        icon = new Icon(rows, cols);

        gridPanel = new PixelGridPanel(rows, cols, this);
        colorChooser = new ColorChooserPanel();
        recentColors = new RecentColorsPanel(colorChooser);

        advancedBox = new JCheckBox("Advanced");

        // Observer controls
        detachedBox = new JCheckBox("Detached");

        // Decorator controls
        verticalBox = new JCheckBox("Vertical Invert");
        horizontalBox = new JCheckBox("Horizontal Invert");
        randomBox = new JCheckBox("Random Color");

        // Register this editor as an observer
        iconEdit.attach(this);

        setLayout(new BorderLayout());

        add(buildLeftSide(), BorderLayout.CENTER);
        add(buildRightSide(), BorderLayout.EAST);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // When this window closes, stop listening for edits
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                iconEdit.detach(BitmapGUI.this);
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel buildLeftSide() {
        JPanel left = new JPanel(new BorderLayout(0, 10));

        left.setBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        left.add(
            new JScrollPane(gridPanel),
            BorderLayout.CENTER
        );

        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));

        bottom.add(advancedBox);
        bottom.add(detachedBox);

        left.add(bottom, BorderLayout.SOUTH);

        // Detach or reattach this editor
        detachedBox.addActionListener(e -> {
            if (detachedBox.isSelected()) {
                iconEdit.detach(this);
            } else {
                iconEdit.attach(this);
            }
        });

        return left;
    }

    private JPanel buildRightSide() {
        JPanel right = new JPanel(new BorderLayout());

        right.setBorder(
            BorderFactory.createEmptyBorder(10, 0, 10, 10)
        );

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        top.add(colorChooser);
        top.add(Box.createVerticalStrut(10));

        top.add(new JLabel("Last five colors"));
        top.add(recentColors);

        top.add(Box.createVerticalStrut(15));
        top.add(new JLabel("Edit Alterations"));

        top.add(verticalBox);
        top.add(horizontalBox);
        top.add(randomBox);

        right.add(top, BorderLayout.NORTH);

        // Buttons at the bottom
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));

        JButton newWindowButton = new JButton("New Window");
        newWindowButton.addActionListener(e -> {
            BitmapGUI newGUI = new BitmapGUI(rows, cols, iconEdit);
            // CHANGED: open the new window down and to the right, instead of exactly on top of this one
            newGUI.setLocation(getX() + 40, getY() + 40);
            newGUI.setVisible(true);
        });

        JButton saveButton = new JButton("Create Bitmap File");
        saveButton.addActionListener(e -> saveBitmap());

        buttons.add(newWindowButton);
        buttons.add(Box.createVerticalStrut(5));
        buttons.add(saveButton);

        right.add(buttons, BorderLayout.SOUTH);

        return right;
    }

    // Called when a grid button is clicked
    public void handleGridClick(int row, int col) {

        if (advancedBox.isSelected()) {
            AdvancedDialog dialog =
                new AdvancedDialog(this, row, col);

            dialog.setVisible(true);

        } else {
            Color c = colorChooser.getSelectedColor();

            // CHANGED: was iconEdit.setEdit(...). sendEdit (below) also handles a detached window
            sendEdit(
                new BasicBitmapEdit(row, col, c)
            );

            recentColors.addColor(c);
        }
    }

    // Feature 1: PixelGridPanel calls this while Shift and the mouse button are held down
    public void handleGridHover(int row, int col) {

        Color c = colorChooser.getSelectedColor();

        // CHANGED: was iconEdit.setEdit(...)
        sendEdit(
            new BasicBitmapEdit(row, col, c)
        );

        recentColors.addColor(c);
    }

    // CHANGED: new method. Every edit made in this window goes through here.
    // Attached: send it to IconEdit, which passes it to every attached window
    //           (this one included) through update().
    // Detached: apply it to this window only.
    private void sendEdit(BitmapEdit edit) {

        if (iconEdit.isAttached(this)) {
            iconEdit.setEdit(edit);
        } else {
            update(edit);
        }
    }

    // Feature 2 and 3:
    // Called whenever IconEdit notifies this observer.
    @Override
    public void update(BitmapEdit edit) {

        // Start with the original edit
        BitmapEdit alteredEdit = edit;

        // Apply vertical inversion if selected
        if (verticalBox.isSelected()) {
            alteredEdit = new VerticalInvertBitmapEdit(
                alteredEdit, rows, cols
            );
        }

        // Apply horizontal inversion if selected
        if (horizontalBox.isSelected()) {
            alteredEdit = new HorizontalInvertBitmapEdit(
                alteredEdit, rows, cols
            );
        }

        // Apply random color if selected
        if (randomBox.isSelected()) {
            alteredEdit = new RandomColorBitmapEdit(
                alteredEdit, rows, cols
            );
        }

        // Get the altered edit information
        int row = alteredEdit.getRow();
        int col = alteredEdit.getCol();
        Color c = alteredEdit.getColor();

        // Make sure the altered coordinates are valid
        if (row >= 0 && row < rows &&
            col >= 0 && col < cols) {

            // Update this editor without notifying IconEdit again
            colorPixel(row, col, c);

            // CHANGED: removed recentColors.addColor(c) from here, so "Last five colors"
            // only shows colors picked in this window (not other windows' or random colors)
        }
    }

    // Updates this editor's own bitmap and grid.
    // IMPORTANT: This method does NOT call iconEdit.setEdit().
    private void colorPixel(int row, int col, Color c) {

        icon.setPixel(
            row,
            col,
            new Pixel(c.getRed(), c.getGreen(), c.getBlue())
        );

        gridPanel.setButtonColor(row, col, c);
    }

    private void saveBitmap() {

        JFileChooser chooser = new JFileChooser();

        chooser.setDialogTitle("Choose a folder to save in");

        chooser.setFileSelectionMode(
            JFileChooser.DIRECTORIES_ONLY
        );

        if (chooser.showSaveDialog(this) ==
            JFileChooser.APPROVE_OPTION) {

            File folder = chooser.getSelectedFile();

            String name = JOptionPane.showInputDialog(
                this, "File name:"
            );

            if (name != null && !name.trim().isEmpty()) {

                name = name.trim();

                if (!name.toLowerCase().endsWith(".bmp")) {
                    name = name + ".bmp";
                }

                File out = new File(folder, name);

                if (BitmapFile.save(icon, rows, cols, out)) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Saved to " + out.getAbsolutePath()
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        this, "Save failed."
                    );
                }
            }
        }
    }

    // Fill an area using the Observer pattern
    public void fillArea(
        int startRow,
        int startCol,
        int numRows,
        int numCols
    ) {

        Color c = colorChooser.getSelectedColor();

        int endRow = Math.min(rows, startRow + numRows);
        int endCol = Math.min(cols, startCol + numCols);

        for (int r = startRow; r < endRow; r++) {
            for (int col = startCol; col < endCol; col++) {

                // CHANGED: was iconEdit.setEdit(...)
                sendEdit(
                    new BasicBitmapEdit(r, col, c)
                );
            }
        }

        recentColors.addColor(c);
    }

    // Import an image using the Observer pattern
    public void importImage(
        int startRow,
        int startCol,
        BufferedImage img
    ) {

        for (int y = 0;
             y < img.getHeight() && startRow + y < rows;
             y++) {

            for (int x = 0;
                 x < img.getWidth() && startCol + x < cols;
                 x++) {

                Color c = new Color(img.getRGB(x, y));

                // CHANGED: was iconEdit.setEdit(...)
                sendEdit(
                    new BasicBitmapEdit(
                        startRow + y,
                        startCol + x,
                        c
                    )
                );
            }
        }
    }
}