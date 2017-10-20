package pl.android.puzzledepartment.managers;

import java.util.ArrayList;
import java.util.List;

import pl.android.puzzledepartment.objects.Camera;
import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.rooms.Room;

/**
 * Created by Maciek Ruszczyk on 2017-10-20.
 */

public class CollisionManager {
    private List<Entity> entities;

    public CollisionManager() {
        entities = new ArrayList<>();
    }

    public boolean checkCollision(Camera camera) {
        for(Entity e: entities)
             if(isCollision(e, camera))
                 return true;
        return false;
    }

    private static boolean isCollision(Entity e, Camera camera) {
        final float scaleX = e.getScale().x/2;
        final float scaleY = e.getScale().y/2;
        final float scaleZ = e.getScale().z/2;

        final float possibleCamX = camera.getPossibleX();
        final float possibleCamY = camera.getPossibleY();
        final float possibleCamZ = camera.getPossibleZ();

    float tmpY1 = (e.getPos().y-scaleY);
        float tmpY2 = (e.getPos().y+scaleY);


        if(possibleCamX >= (e.getPos().x-scaleX) && possibleCamX <= (e.getPos().x+scaleX) &&
                possibleCamY >= (e.getPos().y-scaleY) && possibleCamY <= (e.getPos().y+scaleY) &&
                possibleCamZ >= (e.getPos().z-scaleZ) && possibleCamZ <= (e.getPos().z+scaleZ))
            return true;
        else return false;
    }

    public void add(Entity entity) {
        entities.add(entity);
    }
    public void add(Room room) {
        for(Entity e:room.getEntities())
            add(e);
    }
}
