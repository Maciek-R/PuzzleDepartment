package pl.android.puzzledepartment.managers;


import java.util.ArrayList;
import java.util.List;

import pl.android.puzzledepartment.objects.Camera;
import pl.android.puzzledepartment.objects.Collisionable;
import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.objects.Key;
import pl.android.puzzledepartment.objects.particles.ParticleCollideShooter;
import pl.android.puzzledepartment.puzzles.AbstractPuzzle;
import pl.android.puzzledepartment.puzzles.ChessPuzzle;
import pl.android.puzzledepartment.puzzles.ParticlesOrderPuzzle;
import pl.android.puzzledepartment.puzzles.TeleportPuzzle;
import pl.android.puzzledepartment.objects.complex_objects.Room;

/**
 * Created by Maciek Ruszczyk on 2017-10-20.
 */

public class CollisionManager {
    private GameManager gameManager;

    private List<Entity> entities;
    private CollisionDescription collisionDescription;

    private List<Entity> keys;

    private TeleportPuzzle teleportPuzzle;
    private List<Entity> teleports;

    private ParticlesOrderPuzzle particlesOrderPuzzle;
    private ChessPuzzle chessPuzzle;

    public CollisionManager() {
        entities = new ArrayList<>();
        keys = new ArrayList<>();
        collisionDescription = new CollisionDescription();

        teleports = new ArrayList<>();
    }

    public void addObserver(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public CollisionDescription checkCollision(Camera camera) {
        collisionDescription.isCollision = false;
        collisionDescription.isCollisionOverEntity = false;
        collisionDescription.isCollisionSideEntity = false;
        for (Entity e : entities)
            checkCollision(e, camera);

        if(collisionDescription.isCollision())
            return collisionDescription;

        checkChessCollision(camera);
            return collisionDescription;
    }

    private boolean checkCollision(Collisionable c, Camera camera) {

        final float scaleY = c.getScale().y/2;

        if (collide(c, camera)) {

            collisionDescription.isCollision = true;
            if (camera.getPosY() >= (c.getPos().y + scaleY)) {
                collisionDescription.collisionPosY = c.getPos().y + scaleY;
                collisionDescription.isCollisionOverEntity = true;
            } else {
                collisionDescription.isCollisionSideEntity = true;
            }
            return true;
        } else
            return false;
    }

    private boolean collide(Collisionable e, Camera camera) {
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
        if(entity instanceof Key)
            keys.add(entity);
        else
            entities.add(entity);
    }
    public void add(Room room) {
        for(Entity e:room.getEntities())
            add(e);
    }
    public void add(List<? extends AbstractPuzzle> puzzles) {
        for(AbstractPuzzle puzzle:puzzles)
            add(puzzle);
    }

    private void add (AbstractPuzzle puzzle) {
        if (puzzle instanceof TeleportPuzzle) {
            addTeleportPuzzle((TeleportPuzzle) puzzle);

        } else if (puzzle instanceof ParticlesOrderPuzzle) {
            addParticlesOrderPuzzle((ParticlesOrderPuzzle) puzzle);

        } else if (puzzle instanceof ChessPuzzle) {
            addChessPuzzle((ChessPuzzle) puzzle);
        }
    }

    private void addTeleportPuzzle(TeleportPuzzle teleportPuzzle) {
        this.teleportPuzzle = teleportPuzzle;
        for(Room r:teleportPuzzle.getRooms())
            add(r);
        for(Entity e:teleportPuzzle.getTeleports())
            addTeleport(e);
    }
    private void addParticlesOrderPuzzle(ParticlesOrderPuzzle particlesOrderPuzzle) {
        this.particlesOrderPuzzle = particlesOrderPuzzle;
    }
    private void addChessPuzzle(ChessPuzzle chessPuzzle) {
        this.chessPuzzle = chessPuzzle;
    }
    private void addTeleport(Entity entity) {
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

    public void checkParticlesCollision(Camera camera) {
        for (ParticleCollideShooter particleCollideShooter : particlesOrderPuzzle.getParticleShooters()) {
            if (checkCollision(particleCollideShooter, camera)) {
                particlesOrderPuzzle.checkIfChoseCorrectParticle(particleCollideShooter);
                return;
            }
        }
    }

    private boolean checkChessCollision(Camera camera) {
        for (Entity e : chessPuzzle.getSelectedEntities()) {
            if (checkCollision(e, camera)) {

                if(e.equals(chessPuzzle.getNextCube()))
                    chessPuzzle.selectNextCube();
                return true;
            }
        }
        return false;
    }

    public void checkWithKeyCollision(Camera camera) {
        for (Entity k : keys)
            if (checkCollision(k, camera)) {
                gameManager.onCollisionNotify(k);
                keys.remove(k);
                return;
            }
    }
}
