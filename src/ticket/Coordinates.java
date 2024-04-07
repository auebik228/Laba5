package ticket;
/**
 * The 'Coordinates' class represents a set of coordinates in a two-dimensional space.
 * It contains two private fields: 'x' and 'y', which represent the x-coordinate and y-coordinate respectively.
 * The 'x' field should have a value greater than -223.
 *
 * The class provides a constructor to initialize the coordinates with the given values of 'x' and 'y'.
 * It also provides getter and setter methods for both 'x' and 'y' fields.
 *
 * The class overrides the 'toString' method to provide a string representation of the coordinates in the format "{x=..., y=...}".
 */
public class Coordinates {
    private long x; //Значение поля должно быть больше -223
    private int y;
    public Coordinates(long x,int y){
        this.x=x;
        this.y=y;
    }

    public long getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public void setX(long x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
