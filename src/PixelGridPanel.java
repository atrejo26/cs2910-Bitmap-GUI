import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class PixelGridPanel extends JPanel {

    private JButton[][] buttons;

    public PixelGridPanel(
        int rows,
        int cols,
        BitmapGUI editor
    ) {

        setLayout(new GridLayout(rows, cols));

        buttons = new JButton[rows][cols];

        for (int i = 0; i < rows; i++) {

            for (int j = 0; j < cols; j++) {

                JButton b = new JButton();

                b.setPreferredSize(new Dimension(30, 30));
                b.setBackground(Color.BLACK);
                b.setOpaque(true);
                b.setBorderPainted(false);

                final int row = i;
                final int col = j;

                // Normal click
                b.addActionListener(e ->
                    editor.handleGridClick(row, col)
                );

                // Shift + mouse button held = draw a line
                b.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseEntered(MouseEvent e) {

                        boolean mouseDown = (e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0;

                        if (e.isShiftDown() && mouseDown) {
                            editor.handleGridHover(row, col);
                        }
                    }

                    // Colors the pixel where the drag starts
                    @Override
                    public void mousePressed(MouseEvent e) {

                        if (e.isShiftDown()) {
                            editor.handleGridHover(row, col);
                        }
                    }
                });

                buttons[i][j] = b;

                add(b);
            }
        }
    }

    public void setButtonColor(
        int row,
        int col,
        Color c
    ) {
        buttons[row][col].setBackground(c);
    }

    
}