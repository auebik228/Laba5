package ticket;

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
