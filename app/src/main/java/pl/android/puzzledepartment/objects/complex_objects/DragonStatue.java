package pl.android.puzzledepartment.objects.complex_objects;

import pl.android.puzzledepartment.action.Actionable;
import pl.android.puzzledepartment.objects.Cube;
import pl.android.puzzledepartment.objects.Dragon;
import pl.android.puzzledepartment.objects.EntityModel;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

/**
 * Created by Maciek Ruszczyk on 2017-10-28.
 */

public class DragonStatue implements Actionable{

    public enum Direction{LEFT, BACKWARD, RIGHT, FORWARD};

    private Direction direction = Direction.FORWARD;
    private Point pos;
    private Vector3f scale;

    private Cube cube;
    private Dragon dragon;

    private boolean isInAction = false;
    private float targetRotation;

    public DragonStatue(Point pos, EntityModel dragonModel) {
        this.pos = pos;
        this.scale = new Vector3f(1f, 1f, 1f);
        this.cube = new Cube(pos, scale);
        this.dragon = new Dragon(new Point(pos.x, pos.y+0.5f, pos.z), dragonModel, new Vector3f(0.5f, 0.5f, 0.5f));
    }

    public Cube getCube(){
        return cube;
    }
    public Dragon getDragon(){
        return dragon;
    }

    @Override
    public void action() {
        isInAction = true;
        targetRotation = cube.getVerRotation() + 90f;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void updateAction() {
        cube.rotate(30f);
        dragon.rotate(30f);
        if(cube.getVerRotation() > targetRotation){
            cube.setVerRotation(targetRotation);
            dragon.setVerRotation(targetRotation);
            isInAction = false;
            setNextDirection();
        }
    }

    private void setNextDirection() {
        switch(direction){
            case RIGHT: direction = Direction.BACKWARD; break;
            case FORWARD: direction = Direction.RIGHT; break;
            case LEFT: direction = Direction.FORWARD; break;
            case BACKWARD: direction = Direction.LEFT; break;
        }
    }

    @Override
    public Point getPosition() {
        return pos;
    }

    @Override
    public Vector3f getScale() {
        return scale;
    }

    @Override
    public boolean isInAction() {
        return isInAction;
    }
}
