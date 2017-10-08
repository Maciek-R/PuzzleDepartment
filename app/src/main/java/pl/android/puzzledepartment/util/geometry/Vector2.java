package pl.android.puzzledepartment.util.geometry;

/**
 * Created by Maciek on 2017-10-07.
 */

public class Vector2 {
    public final float x, y;

    public Vector2(float x, float y){
        this.x = x;
        this.y = y;
    }
    public float length(){
        return (float) Math.sqrt(x*x + y*y);
    }
    public float dotProduct(Vector2 vector){
        return x * vector.x + y * vector.y;
    }
    public Vector2 scale(float f){
        return new Vector2(x*f, y*f);
    }

    public Vector2 normalize(){
        final float len = this.length();
        return new Vector2(x/len, y/len);
    }
}
