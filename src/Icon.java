import java.util.ArrayList;

public class Icon {
    private ArrayList<ArrayList<Pixel>> grid;

    int r = 0;
    int c = 0;

    public Icon() {
        this(5, 5);
        r = 5;
        c = 5;
    }

    public Icon(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Error: rows and cols must be positive");
        } else {
            r = rows;
            c = cols;
            grid = new ArrayList<ArrayList<Pixel>>();

            for (int i = 0; i < rows; i++) {
                ArrayList<Pixel> row = new ArrayList<Pixel>();

                for (int j = 0; j < cols; j++)
                {
                    //creat new black pixel by default
                    Pixel blackPixel = new Pixel(0, 0, 0);
                    row.add(blackPixel);
                }
                grid.add(row);
            }
        }
    }

    private boolean isValid(int row, int col) {
        boolean retVal = true;
        if (row < 0 || row >= grid.size()) {
            retVal = false;
        }
        if (col < 0 || col >= grid.get(row).size()) {
            retVal = false;
        }
        return retVal;
    }

    public Pixel getPixel(int row, int col) {
        if (isValid(row, col)) {
            return grid.get(row).get(col);
        } else {
            System.out.println("Error: out of bounds");
            return null;
        }
    }
    public void setPixel(int row, int col, Pixel newPixel) {
        if (isValid(row, col)) {
            grid.get(row).set(col, newPixel);
        } else {
            System.out.println("Error: out of bounds");
        }
    }
    
    public String toString() {
        String retString = "";
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                retString = retString + getPixel(i, j) + " ";
            }
            if (i < r-1) {
                retString = retString + "\n";
            }
        }
        return retString;
    }

}
