package pl.android.puzzledepartment.objects;

import pl.android.puzzledepartment.programs.ShaderProgram;
import pl.android.puzzledepartment.util.geometry.Point;

/**
 * Created by Maciek on 2017-10-08.
 */

public abstract class Entity {
    protected Point pos;

    protected Entity(Point pos) {
        this.pos = pos;
    }

    abstract public void draw();
    abstract public void bindData(ShaderProgram shaderProgram);

    public Point getPos() {
        return pos;
    }
}
