package pl.android.puzzledepartment.objects;

import java.util.ArrayList;
import java.util.List;

import pl.android.puzzledepartment.programs.ShaderProgram;
import pl.android.puzzledepartment.util.geometry.Point;

/**
 * Created by Maciek on 2017-10-08.
 */

public abstract class Entity {
    protected Point pos;
    protected List<ObjectBuilder.DrawCommand> drawList;

    protected Entity(Point pos) {
        this.pos = pos;
    }

    abstract public void bindData(ShaderProgram shaderProgram);
    public void draw() {
        for(ObjectBuilder.DrawCommand d:drawList)
            d.draw();
    }

    public Point getPos() {
        return pos;
    }
}
