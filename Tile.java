import java.awt.Color; // the color type used in StdDraw
import java.awt.Font; // the font type used in StdDraw
import java.util.Random; // for random number generator

// A class used for modeling numbered tiles as in 2048
public class Tile {
    // Data fields: instance variables
    // --------------------------------------------------------------------------
    private int number; // the number on the tile
    private Color backgroundColor; // background (tile) color
    private Color foregroundColor; // foreground (number) color
    private Color boxColor; // box (boundary) color

    // Data fields: class variables
    // --------------------------------------------------------------------------
    // the value of the boundary thickness (for the boundaries around the tiles)
    private static double boundaryThickness = 0.004;
    // the font used for displaying the tile number
    private static Font font = new Font("Arial", Font.PLAIN, 14);

    // Methods
    // --------------------------------------------------------------------------
    // the default constructor that creates a tile with 2 as the number on it
    public Tile() {
        // set the random number on the tile
        int[] numbers = {2, 4};
        Random random = new Random();
        int randomNumber = random.nextInt(numbers.length);
        this.number = numbers[randomNumber];
        foregroundColor = new Color(0, 100, 200);
        boxColor = new Color(0, 100, 200);

    }

    public void numberBackGroundColor() {
        if (number == 2) {
            backgroundColor = new Color(151, 178, 199);
        } else if (number == 4) {
            backgroundColor = new Color(147, 209, 229);
        } else if (number == 8) {
            backgroundColor = new Color(85, 197, 252);
        } else if (number == 16) {
            backgroundColor = new Color(72, 187, 222);
        } else if (number == 32) {
            backgroundColor = new Color(22, 166, 210);
        } else if (number == 64) {
            backgroundColor = new Color(19, 134, 169);
        } else if (number == 128) {
            backgroundColor = new Color(135, 155, 253);
        } else if (number == 256) {
            backgroundColor = new Color(119, 127, 252);
        } else if (number == 512) {
            backgroundColor = new Color(100, 112, 252);
        } else if (number == 1024) {
            backgroundColor = new Color(65, 81, 243);
        } else if (number == 2048) {
            backgroundColor = new Color(49, 68, 250);
        }
    }

    // a method for drawing the tile
    public void draw(Point position, double... sideLength) {
        //After each operation, it redefines the colors according to their numbers.
        numberBackGroundColor();
        // the default value for the side length (sLength) is 1
        double sLength;
        if (sideLength.length == 0) // sideLength is a variable-length parameter
            sLength = 1;
        else
            sLength = sideLength[0];
        // draw the tile as a filled square
        StdDraw.setPenColor(backgroundColor);
        StdDraw.filledSquare(position.getX(), position.getY(), sLength / 2);
        // draw the bounding box around the tile as a square
        StdDraw.setPenColor(boxColor);
        StdDraw.setPenRadius(boundaryThickness);
        StdDraw.square(position.getX(), position.getY(), sLength / 2);
        StdDraw.setPenRadius(); // reset the pen radius to its default value
        // draw the number on the tile
        StdDraw.setPenColor(foregroundColor);
        StdDraw.setFont(font);
        StdDraw.text(position.getX(), position.getY(), "" + number);
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

}