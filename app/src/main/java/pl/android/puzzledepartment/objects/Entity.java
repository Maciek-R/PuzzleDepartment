package pl.android.puzzledepartment.objects;

import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;

import pl.android.puzzledepartment.programs.ShaderProgram;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

/**
 * Created by Maciek Ruszczyk on 2017-10-08.
 */

public abstract class Entity {
    protected Point pos;
    protected float verAngle;
    protected Vector3f scale;
    protected List<ObjectBuilder.DrawCommand> drawList;

    protected Entity(Point pos) {
        this(pos, 0f, new Vector3f(1f, 1f, 1f));
    }
    protected Entity(Point pos, float angle) {
        this(pos, angle, new Vector3f(1f, 1f, 1f));
    }
    protected Entity(Point pos, Vector3f scale) {
        this(pos, 0f, scale);
    }
    protected Entity(Point pos, float angle, Vector3f scale) {
        this.pos = pos;
        this.verAngle = angle;
        this.scale = scale;
        drawList = new ArrayList<>();
    }
    public void move() {

        long time = SystemClock.currentThreadTimeMillis();

        float x = 3*(float) Math.sin((float)time / 1000);
        float z = 3*(float) Math.cos((float)time / 1000) - 3;

        this.pos = new Point(x, pos.y, z);
    }
    public void move2() {

        long time = SystemClock.currentThreadTimeMillis();
        float x = 3*(float) Math.sin((float)time / 500);
        this.pos = new Point(x, pos.y, pos.z);
    }
    public void rotate(float angle) {
        this.verAngle+=angle;
    }
    abstract public void bindData(ShaderProgram shaderProgram);
    abstract public void draw();

    public Point getPos() {
        return pos;
    }
    public float getRotation() { return verAngle; }
    public Vector3f getScale() { return scale; }
}
