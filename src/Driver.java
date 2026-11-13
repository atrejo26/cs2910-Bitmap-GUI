import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Driver {
    public static void main(String[] args) {
        Pixel Pixel;
        Pixel = new Pixel(25, 20, 255);
        System.out.println("Pixel Test:\n" + Pixel.toString());

        Icon icon;
        icon = new Icon(5, 5);
        System.out.println("Icon Test 1:\n" + icon.toString());

        icon.setPixel(1, 2, Pixel);
        System.out.println("Icon Test 2:\n" + icon.toString());

        // ---- Test 1: default constructor ----
        System.out.println("Test 1: Default constructor (should be 0,0,0)");
        Pixel p1 = new Pixel();
        System.out.println(p1.toString());       // expect rgb(0, 0, 0)
        System.out.println(p1.toStringHex());     // expect #000000
        System.out.println();

        // ---- Test 2: custom constructor, normal values ----
        System.out.println("Test 2: Custom constructor (25, 20, 255)");
        Pixel p2 = new Pixel(25, 20, 255);
        System.out.println(p2.toString());       // expect rgb(25, 20, 255)
        System.out.println(p2.toStringHex());     // expect #1914FF
        System.out.println();

        // ---- Test 3: boundary values (min) ----
        System.out.println("Test 3: All zeros");
        Pixel p3 = new Pixel(0, 0, 0);
        System.out.println(p3.toString());       // expect rgb(0, 0, 0)
        System.out.println(p3.toStringHex());     // expect #000000
        System.out.println();

        // ---- Test 4: boundary values (max) ----
        System.out.println("Test 4: All 255s (white)");
        Pixel p4 = new Pixel(255, 255, 255);
        System.out.println(p4.toString());       // expect rgb(255, 255, 255)
        System.out.println(p4.toStringHex());     // expect #FFFFFF
        System.out.println();

        // ---- Test 5: out-of-bounds values via constructor ----
        System.out.println("Test 5: Out-of-bounds constructor (-5, 300, 999)");
        Pixel p5 = new Pixel(-5, 300, 999);
        System.out.println(p5.toString());       // expect rgb(0, 0, 0) if invalid -> 0
        System.out.println(p5.toStringHex());     // expect #000000
        System.out.println();

        // ---- Test 6: individual setters, normal values ----
        System.out.println("Test 6: Setters with valid values");
        Pixel p6 = new Pixel();
        p6.setRed(100);
        p6.setGreen(150);
        p6.setBlue(200);
        System.out.println(p6.toString());       // expect rgb(100, 150, 200)
        System.out.println(p6.toStringHex());     // expect #6496C8
        System.out.println();

        // ---- Test 7: setters with out-of-bounds values ----
        System.out.println("Test 7: Setters with invalid values");
        Pixel p7 = new Pixel(50, 60, 70); // start with valid values
        p7.setRed(-1);     // invalid -> should reset to 0
        p7.setGreen(256);  // invalid -> should reset to 0
        p7.setBlue(1000);  // invalid -> should reset to 0
        System.out.println(p7.toString());       // expect rgb(0, 0, 0)
        System.out.println(p7.toStringHex());     // expect #000000
        System.out.println();

        // ---- Test 8: verify setting one channel doesn't affect others ----
        System.out.println("Test 8: Channel independence");
        Pixel p8 = new Pixel(10, 20, 30);
        p8.setGreen(99); // only green should change
        System.out.println(p8.toString());       // expect rgb(10, 99, 30)
        System.out.println();

        // ---- Test 9: re-setting a channel after it was already set ----
        System.out.println("Test 9: Overwrite a channel value");
        Pixel p9 = new Pixel(10, 10, 10);
        p9.setRed(200);
        p9.setRed(50); // overwrite again
        System.out.println(p9.toString());       // expect rgb(50, 10, 10)
        System.out.println();

        // ---- Test 10: single-digit hex padding ----
        System.out.println("Test 10: Hex zero-padding (5, 0, 9)");
        Pixel p10 = new Pixel(5, 0, 9);
        System.out.println(p10.toStringHex());    // expect #050009
        System.out.println();

        System.out.println("All tests completed.");

    
        //  Launch the GUI 
        SwingUtilities.invokeLater(() -> {
            int rows = askPositiveInt("How many rows?");
            int cols = askPositiveInt("How many columns?");
            BitmapGUI editor = new BitmapGUI(rows, cols);
            editor.setVisible(true);        // windows start hidden
        });

    }
    private static int askPositiveInt(String message) {
        int retVal = 0;
        while (retVal <= 0) {
            String input = JOptionPane.showInputDialog(null, message, "Icon size",
                    JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                System.exit(0);             // user pressed Cancel
            }
            try {
                int n = Integer.parseInt(input.trim());
                if (n > 0) {
                    retVal = n;
                } else {
                    JOptionPane.showMessageDialog(null, "The number must be greater than 0.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "That's not a whole number.");
            }
        }
        return retVal;
    }
}