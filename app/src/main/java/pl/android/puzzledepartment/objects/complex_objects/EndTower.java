package pl.android.puzzledepartment.objects.complex_objects;

import android.graphics.Color;

import pl.android.puzzledepartment.action.Actionable;
import pl.android.puzzledepartment.objects.Door;
import pl.android.puzzledepartment.objects.EntityModel;
import pl.android.puzzledepartment.objects.Tower;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;


/**
 * Created by Maciek Ruszczyk on 2017-12-17.
 */

public class EndTower implements Actionable{
    private Point pos;
    private Vector3f scale;

    private Tower tower;
    private Door door;

    private boolean isInAction = false;
    private float targetRotation;
    private boolean isDoorOpen = false;

    public EndTower(Point pos, EntityModel towerModel, EntityModel doorModel) {
        this.pos = pos;
        this.scale = new Vector3f(1f, 1f, 1f);
        this.tower = new Tower(pos, Color.GREEN, towerModel);
        this.door = new Door(new Point(pos.x+2.41f, pos.y, pos.z+0.62f), Color.RED, doorModel);
    }

    public Tower getTower(){
        return tower;
    }
    public Door getDoor(){
        return door;
    }

    @Override
    public void action() {
        isInAction = true;
        targetRotation = door.getVerRotation() + 90f;
    }

    @Override
    public void updateAction() {
        if(isDoorOpen)
            return;
        door.rotate(30f);
        if(door.getVerRotation() > targetRotation) {
            door.setVerRotation(targetRotation);
            isDoorOpen = true;
        }
    }

    @Override
    public Point getPosition() {
        return door.getPos();
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
