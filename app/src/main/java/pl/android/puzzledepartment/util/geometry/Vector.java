package pl.android.puzzledepartment.util.geometry;

/**
 * Created by Maciek on 2017-10-06.
 */

public class Vector {
    public final float x, y, z;

    public Vector(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public float length(){
        return (float) Math.sqrt(x*x + y*y + z*z);
    }
    public Vector crossProduct(Vector other){
        return new Vector(
                (y * other.z) - (z * other.y),
                (z * other.x) - (x * other.z),
                (x * other.y) - (y * other.x));
    }
    public float dotProduct(Vector vector){
        return x * vector.x + y * vector.y + z * vector.z;
    }
    public Vector scale(float f){
        return new Vector(x*f, y*f, z*f);
    }

    public Vector normalize(){
        final float len = this.length();
        return new Vector(x/len, y/len, z/len);
    }
}
