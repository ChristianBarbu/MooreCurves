import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

import static java.lang.Math.abs;

public class Moore extends JFrame {

    public Moore() {
        setTitle("Moore Curve");
        setSize(1000, 1000);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Draws the given moore curve (list of pairs)
     * @param mooreCurve to be drawn
     * @param g needed for the paint method
     */
    void drawMoore(List<Pair> mooreCurve, Graphics g) {
        for(int i = 0; i < mooreCurve.size() - 1; i++){
            g.drawLine(mooreCurve.get(i).x, mooreCurve.get(i).y, mooreCurve.get(i + 1).x, mooreCurve.get(i + 1).y);
        }
    }

    /**
     * swing stuff
     */
    public void paint(Graphics g) {

        // TODO: uncomment ONE of this moore list of pairs and change it down below
        // List<Pair> moore1 = applyScaleAndOffset(moore(1), 100, 100);

        List<Pair> moore2 = applyScaleAndOffset(moore(2), 200, 100);
        //List<Pair> moore3 = applyScaleAndOffset(moore(4), 50, 50);
        // List<Pair> moore5 = applyScaleAndOffset(moore(5), 25, 50);
        // List<Pair> moore6 = applyScaleAndOffset(moore(6), 10, 50);

        drawMoore(moore2, g);
    }

    /**
     * Scales and offsets the starting point of the actual drawn curve (not needed in C, as this is only for java drawing reasons)
     * @param input moore curve given as a list of pairs
     * @param scale factor
     * @param offset offset from the coordinate (0,0)
     * @return new scaled, offsetted list of pairs
     */
    public static List<Pair> applyScaleAndOffset(List<Pair> input, int scale, int offset) {
        List<Pair> result = new ArrayList<>();
        for (Pair pair : input) {
            result.add(new Pair(pair.x * scale + offset, pair.y * scale + offset));
        }
        return result;
    }

    public static void main(String[] args) {

        Moore moore = new Moore();

        System.out.println("numOfCharsToAllocateRecursive(2) = " + numOfCharsToAllocateRecursive(2));
        System.out.println("numOfCharsToAllocate(2) = " + numOfCharsToAllocate(2));

    }

    /**
     * Mod function, as java doesn't provide a correct n mod m, whereas n < 0
     * @param n dividend
     * @param m divisor
     * @return n mod m
     */
    static int mod(int n, int m) {
        return (n < 0) ? (m - (abs(n) % m) ) %m : (n % m);
    }

    /**
     * A way to allocate the length of the string recursively
     * @param n degree of moore curve
     * @return how many space to allocate for the char array
     */
    static long numOfCharsToAllocateRecursive(int n) {
        if (n == 1)
            return 9;
        return (long) (numOfCharsToAllocateRecursive(n - 1) - Math.pow(4, n - 1) + Math.pow(4, n - 1) * 11);
    }

    /**
     * A way to allocate the length of the string iteratively
     * @param n degree of moore curve
     * @return how many space to allocate for the char array
     */
    static long numOfCharsToAllocate(int n) {
        return (long) ((5*Math.pow(2, 2 * n + 1) - 13) / 3);
    }

    /**
     * Calculates the string given by the degree and the L-system
     * @param degree of moore curve
     * @return string, from which instructions can be extracted
     */
    static String mooreStr(int degree) {
        var numCoords = (int) Math.pow(4, degree);

        var axiom = "LFL+F+LFL";

        if (degree == 1)
            return axiom;

        var L = "−RF+LFL+FR−";
        var R = "+LF−RFR−FL+";

        var result = axiom;
        var temp = axiom;

        for (int i = 0; i < degree - 1; i++) {
            long tempStrLen = numOfCharsToAllocate(i + 1);
            int k = 0;
            for (int j = 0; j < tempStrLen; j++) {
                if (temp.charAt(k) == 'L') {
                    temp = result.substring(0, k) + L + result.substring(k + 1);
                    result = temp;
                    k += 11;
                } else {
                    if (temp.charAt(k) == 'R') {
                        temp = result.substring(0, k) + R + result.substring(k + 1);
                        result = temp;
                        k += 11;
                    } else {
                        k++;
                    }
                }

            }
        }
        System.out.println(result);
        return result;
    }

    /**
     * Storing data in a pair
     * (Later in C we only need to store the data in the given integer pointers)
     */
    static class Pair{
        int x;
        int y;
        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    /**
     * Calculates the actual points on the curve
     * @param degree of moore curve
     * @return list of pairs
     */
    static List<Pair> moore(int degree) {
        var instr = shortMooreStr(degree);

        // System.out.println(instr);

        // starting at point (2^n - 1, 0)
        List<Pair> result = new ArrayList<>();

        Pair start = new Pair((int)Math.pow(2, degree-1) - 1, 0);
        Pair current = new Pair(start.x, start.y);
        result.add(start);


        int dir = 0;

        // Defining directions
        /*
            0 -> North  (increment y)
            1 -> West   (decrement x)
            2 -> South  (decrement y)
            3 -> East   (increment x)
         */

        // we start looking to north
        for(int i = 0; i < instr.length(); i++) {
            char currentChar = instr.charAt(i);
            switch (currentChar) {
                case 'F' -> {
                    Pair add = moveWithRespectToDir(dir);
                    current.x += add.x;
                    current.y += add.y;
                    result.add(new Pair(current.x, current.y));
                }
                case '+' -> dir = mod(dir - 1, 4);
                default -> dir = mod(dir + 1, 4); // <- for some reason it does not accept '-' as a real char, therefore it is default here
            }
        }
        return result;
    }

    /**
     * Returns a pair, which is only used to be able to
     * @param dir specified direction (0 := NORTH, 1 := WEST, 2 := SOUTH, 3 := EAST)
     * @return the pair to be added to the 'current' location
     */
    static Pair moveWithRespectToDir(int dir) {
        Pair result = new Pair(0,0);
        switch (dir) {
            case 0 -> result.y = 1;     // NORTH
            case 1 -> result.x = -1;    // WEST
            case 2 -> result.y = -1;    // SOUTH
            case 3 -> result.x = 1;     // EAST
        }
        return result;
    }

    /**
     * A shorter variant of the L-system string
     * @param degree of moore curve
     * @return shorter version of the L-system string
     */
    static String shortMooreStr(int degree) {
        var str = mooreStr(degree);
        return str.replaceAll("[LR]", "");
    }

}
