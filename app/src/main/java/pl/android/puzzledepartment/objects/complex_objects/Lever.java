package pl.android.puzzledepartment.objects.complex_objects;

import pl.android.puzzledepartment.action.Actionable;
import pl.android.puzzledepartment.objects.Cube;
import pl.android.puzzledepartment.objects.SimpleColorShaderCube;
import pl.android.puzzledepartment.util.geometry.Point;

/**
 * Created by Maciek Ruszczyk on 2017-11-26.
 */

public class Lever extends Cube implements Actionable{

    private SimpleColorShaderCube simpleColorShaderCube = null;

    public Lever(Point pos, SimpleColorShaderCube simpleColorShaderCube) {
        super(pos);
        this.simpleColorShaderCube = simpleColorShaderCube;
    }

    @Override
    public void action() {
        this.singleHorRotate(90f);
        simpleColorShaderCube.setNextColor();
    }

    @Override
    public Point getPosition() {
        return pos;
    }

    @Override
    public boolean isInAction() {
        return false;
    }

    @Override
    public void updateAction() {

    }
}
