package pl.android.puzzledepartment.managers;


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
        collisionDescription.isCollision = false;
        collisionDescription.isCollisionOverEntity = false;
        collisionDescription.isCollisionSideEntity = false;
        for (Entity e : entities)
            checkCollision(e, camera);

        return collisionDescription;
    }

    private boolean checkCollision(Entity e, Camera camera) {

        final float scaleY = e.getScale().y/2;

        if (collide(e, camera)) {

            collisionDescription.isCollision = true;
            if (camera.getPosY() >= (e.getPos().y + scaleY)) {
                collisionDescription.collisionPosY = e.getPos().y + scaleY;
                collisionDescription.isCollisionOverEntity = true;
            } else {
                collisionDescription.isCollisionSideEntity = true;
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
        boolean isCollision;
        boolean isCollisionOverEntity;
        boolean isCollisionSideEntity;
        float collisionPosY;

        public boolean isCollisionOverEntity() {
            return isCollisionOverEntity;
        }

        public boolean isCollisionSideEntity() {
            return isCollisionSideEntity;
        }

        public boolean isCollision() {
            return isCollision;
        }
        public float getCollisionPosY() {
            return collisionPosY;
        }
    }

    public boolean checkTeleportCollision(Camera camera) {
        for (Entity t : teleports)
            if (checkCollision(t, camera)) {
                boolean x = teleportPuzzle.checkCorrectTeleport(t);
                camera.goTo(teleportPuzzle.getPositionOnCurrentFloor());
                return true;
            }
        return false;
    }
}
