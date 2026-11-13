import java.awt.*;
import javax.swing.*;

public class PixelGridPanel extends JPanel {
    private JButton[][] buttons;

    // editor is who we report clicks to
    public PixelGridPanel(int rows, int cols, BitmapGUI editor) {
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
                b.addActionListener(e -> editor.handleGridClick(row, col));

                buttons[i][j] = b;
                add(b);
            }
        }
    }
    
    public void setButtonColor(int row, int col, Color c) {
        buttons[row][col].setBackground(c);
    }
}