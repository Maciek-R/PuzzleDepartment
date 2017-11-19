package pl.android.puzzledepartment.objects.complex_objects;

import pl.android.puzzledepartment.action.Actionable;
import pl.android.puzzledepartment.objects.Cube;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

/**
 * Created by Maciek Ruszczyk on 2017-10-28.
 */

public class DragonStatue implements Actionable{
    private Point pos;
    private Vector3f scale;

    private Cube cube;

    public DragonStatue(Point pos) {
        this.pos = pos;
        this.scale = new Vector3f(1.0f, 1.0f, 1.0f);
        this.cube = new Cube(pos);
    }

    public Cube getCube(){
        return cube;
    }

    @Override
    public void action() {

    }

    @Override
    public Point getPosition() {
        return pos;
    }

    @Override
    public Vector3f getScale() {
        return scale;
    }
}
