package pl.android.puzzledepartment.util.geometry;

/**
 * Created by Maciek on 2017-10-06.
 */

public class Point {
    public final float x, y, z;

    public Point(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point translate(Vector vector){
        return new Point(x + vector.x, y + vector.y, z + vector.z);
    }
}
