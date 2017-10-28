package pl.android.puzzledepartment.objects;

import pl.android.puzzledepartment.action.Actionable;
import pl.android.puzzledepartment.programs.ShaderProgram;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

/**
 * Created by Maciek Ruszczyk on 2017-10-28.
 */

public class DragonStatue extends Entity implements Actionable{

    private Cube cube;

    public DragonStatue(Point pos) {
        super(pos);
        this.cube = new Cube(pos);
    }

    @Override
    public void bindData(ShaderProgram shaderProgram) {
        cube.bindData(shaderProgram);
    }

    @Override
    public void draw() {
        cube.draw();
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
