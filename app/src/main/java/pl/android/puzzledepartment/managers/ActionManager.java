package pl.android.puzzledepartment.managers;

import java.util.ArrayList;
import java.util.List;

import pl.android.puzzledepartment.action.Actionable;
import pl.android.puzzledepartment.objects.Camera;

/**
 * Created by Maciek Ruszczyk on 2017-10-28.
 */

public class ActionManager {

    private List<Actionable> actionables;
    private Actionable activeActionable;

    public ActionManager() {
        actionables = new ArrayList<Actionable>();
        activeActionable = null;
    }

    public void add(List<? extends Actionable> actionables) {
        for(Actionable a:actionables)
            add(a);
    }

    public void add(Actionable actionable) {
        actionables.add(actionable);
    }

    public boolean isNearAnyActionableObject(Camera camera) {
        for (Actionable a : actionables)
            if (!a.isInAction() && extendedCollide(a, camera)) {
                activeActionable = a;
                return true;
            }

        activeActionable = null;
        return false;
    }

    private boolean extendedCollide(Actionable a, Camera camera) {
        final float scaleX = a.getScale().x;
        final float scaleY = a.getScale().y;
        final float scaleZ = a.getScale().z;

        final float entityLeftPosX = a.getPosition().x - scaleX;
        final float entityRightPosX = a.getPosition().x + scaleX;
        final float entityBottomPosY = a.getPosition().y - scaleY;
        final float entityTopPosY = a.getPosition().y + scaleY;
        final float entityLeftPosZ = a.getPosition().z - scaleZ;
        final float entityRightPosZ = a.getPosition().z + scaleZ;

        final float possibleCamLeftX = camera.getPossibleX() - camera.WIDTH/2;
        final float possibleCamRightX = camera.getPossibleX() + camera.WIDTH/2;
        final float possibleCamBottomY = camera.getPossibleY();
        final float possibleCamTopY = camera.getPossibleY() + camera.WIDTH;
        final float possibleCamLeftZ = camera.getPossibleZ() - camera.WIDTH/2;
        final float possibleCamRightZ = camera.getPossibleZ() + camera.WIDTH/2;

        if(possibleCamLeftX > entityRightPosX || possibleCamRightX < entityLeftPosX ||
                possibleCamBottomY > entityTopPosY || possibleCamTopY < entityBottomPosY ||
                possibleCamLeftZ > entityRightPosZ || possibleCamRightZ < entityLeftPosZ)
            return false;
        else
            return true;
    }

    public void activate() {
        if(activeActionable != null)
            activeActionable.action();
    }

    public void moveInActionObjects() {
        for (Actionable a : actionables) {
            if(a.isInAction())
                a.updateAction();
        }
    }
}
