package pl.android.puzzledepartment.managers;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import pl.android.puzzledepartment.objects.Camera;
import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.puzzles.TeleportPuzzle;
import pl.android.puzzledepartment.rooms.Room;

/**
 * Created by Maciek Ruszczyk on 2017-10-20.
 */

public class CollisionManager {
    private List<Entity> entities;
    private CollisionDescription collisionDescription;

    private TeleportPuzzle teleportPuzzle;
    private List<Entity> teleports;

    public CollisionManager() {
        entities = new ArrayList<>();
        collisionDescription = new CollisionDescription();

        teleports = new ArrayList<>();
    }

    public CollisionDescription checkCollision(Camera camera) {
        for (Entity e : entities)
             if(isCollision(e, camera))
                 return collisionDescription;
        return collisionDescription;
    }

    private boolean isCollision(Entity e, Camera camera) {
        final float scaleX = e.getScale().x/2;
        final float scaleY = e.getScale().y/2;
        final float scaleZ = e.getScale().z/2;

        final float possibleCamX = camera.getPossibleX();
        final float possibleCamY = camera.getPosY();//-1.5f;
        final float possibleCamZ = camera.getPossibleZ();

        if (possibleCamX >= (e.getPos().x - scaleX) && possibleCamX <= (e.getPos().x + scaleX) &&
                possibleCamY >= (e.getPos().y - scaleY) && possibleCamY <= (e.getPos().y + scaleY) &&
                possibleCamZ >= (e.getPos().z - scaleZ) && possibleCamZ <= (e.getPos().z + scaleZ)) {

            collisionDescription.setIsCollision(true);
            collisionDescription.setCollisionPosY(e.getPos().y + scaleY);
            return true;
        } else {
            collisionDescription.setIsCollision(false);
            return false;
        }
    }
    public void add(Entity entity) {
        entities.add(entity);
    }
    public void add(Room room) {
        for(Entity e:room.getEntities())
            add(e);
    }
    public void add(TeleportPuzzle teleportPuzzle) {
        this.teleportPuzzle = teleportPuzzle;
        for(Room r:teleportPuzzle.getRooms())
            add(r);
        for(Entity e:teleportPuzzle.getTeleports())
            addTeleport(e);
    }
    public void addTeleport(Entity entity) {
        teleports.add(entity);
    }

    public static class CollisionDescription {
        private boolean isCollision;
        private float collisionPosY;

        public boolean isCollision() {
            return isCollision;
        }
        public float getCollisionPosY() {
            return collisionPosY;
        }
        public void setIsCollision(boolean isCollision) {
            this.isCollision = isCollision;
        }
        public void setCollisionPosY(float collisionPosY) {
            this.collisionPosY = collisionPosY;
        }
    }

    public boolean checkTeleportCollision(Camera camera) {
        for (Entity t : teleports)
            if (isCollision(t, camera)) {
                boolean x = teleportPuzzle.checkCorrectTeleport(t);
                camera.goTo(teleportPuzzle.getPositionOnCurrentFloor());
                return true;
            }
        return false;
    }
}
