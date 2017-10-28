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
        collisionDescription.setIsCollision(false);
        collisionDescription.isCollisionOverEntity = false;
        collisionDescription.isCollisionSideEntity = false;
        for (Entity e : entities)
            if (isCollision(e, camera)) {
                //if (collisionDescription.isOverEntity())
               //     continue;
               // else
                //    return collisionDescription;
            }

        if(collisionDescription.isCollision())
            return collisionDescription;

       // collisionDescription.setIsCollision(false);
        return collisionDescription;
    }

    private boolean isCollision(Entity e, Camera camera) {

        final float scaleY = e.getScale().y/2;

        if (collide(e, camera)) {

            collisionDescription.setIsCollision(true);
            if (camera.getPosY() >= (e.getPos().y + scaleY)) {
                collisionDescription.setCollisionPosY(e.getPos().y + scaleY);
                collisionDescription.isCollisionOverEntity = true;
            } else {
                collisionDescription.isCollisionSideEntity = true;
               // collisionDescription.setIsOverEntity(false);
            }

            return true;
        } else
            return false;
    }

    private boolean collide(Entity e, Camera camera) {
        final float scaleX = e.getScale().x/2;
        final float scaleY = e.getScale().y/2;
        final float scaleZ = e.getScale().z/2;

        final float entityLeftPosX = e.getPos().x - scaleX;
        final float entityRightPosX = e.getPos().x + scaleX;
        final float entityBottomPosY = e.getPos().y - scaleY;
        final float entityTopPosY = e.getPos().y + scaleY;
        final float entityLeftPosZ = e.getPos().z - scaleZ;
        final float entityRightPosZ = e.getPos().z + scaleZ;

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

        //possibleCamX >= (e.getPos().x - scaleX) && possibleCamX <= (e.getPos().x + scaleX) &&
        //        possibleCamY >= (e.getPos().y - scaleY) && possibleCamY <= (e.getPos().y + scaleY) &&
        //        possibleCamZ >= (e.getPos().z - scaleZ) && possibleCamZ <= (e.getPos().z + scaleZ))

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
        public boolean isCollisionOverEntity;
        public boolean isCollisionSideEntity;
        private float collisionPosY;

        public boolean isCollision() {
            return isCollision;
        }
      //  public boolean isOverEntity() {return overEntity; }
        public float getCollisionPosY() {
            return collisionPosY;
        }
        //public void setIsOverEntity(boolean overEntity) {this.overEntity = overEntity; }
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
