package pl.android.puzzledepartment.objects;

import android.os.SystemClock;

import pl.android.puzzledepartment.managers.CollisionManager;
import pl.android.puzzledepartment.managers.TimeManager;
import pl.android.puzzledepartment.util.geometry.Vector3f;

/**
 * Created by Maciek Ruszczyk on 2017-10-06.
 */

public class Camera {

    private static final float JUMP_POWER = 20;
    private static final float GRAVITY = -50;

    private float posX;
    private float posY;
    private float posZ;

    private float rotationX;
    private float rotationY;

    private float possiblePosX;
    //private float possiblePosY;
    private float possiblePosZ;

    private boolean isInAir = false;
    private float flySpeed = 0f;

    private float deltaX = 0;
    private float deltaZ = 0;

    public Camera() {
        this(0, 0, 0);
    }

    public Camera(float x, float y, float z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
    }

    public void move() {
        this.posX = possiblePosX;
        this.posZ = possiblePosZ;
    }

    public void jump() {
        if (!isInAir) {
            flySpeed = JUMP_POWER;
            isInAir = true;
        }
    }

    public void update(HeightMap heightMap, CollisionManager collisionManager) {
        countNextPossiblePosition(heightMap);
        if(!collisionManager.checkCollision(this))
            move();

        flySpeed += GRAVITY * TimeManager.getDeltaTimeInSeconds();
        this.posY += flySpeed * TimeManager.getDeltaTimeInSeconds();

        float heightY = heightMap.getHeight(posX, posZ)+1.5f;
        if (posY < heightY) {
            isInAir = false;
            flySpeed = 0;
            posY = heightY;
        }
    }

    private void countNextPossiblePosition(HeightMap heightMap) {
        possiblePosX = posX;
        possiblePosZ = posZ;

        float translationX = -(float) (Math.sin(Math.toRadians(rotationX)) * deltaZ);
        float translationZ = (float) (Math.cos(Math.toRadians(rotationX)) * deltaZ);

        possiblePosX += translationX;
        possiblePosZ += translationZ;

        translationX = (float) (Math.sin(Math.toRadians(rotationX + 90)) * deltaX);
        translationZ = -(float) (Math.cos(Math.toRadians(rotationX + 90)) * deltaX);

        possiblePosX += translationX;
        possiblePosZ += translationZ;

       // final float height = heightMap.getHeight(possiblePosX, possiblePosZ);
       // possiblePosY = height;
    }

    public void rotateX(float angle) {
        rotationX+=angle;
    }
    public void rotateY(float angle) {
        rotationY+=angle;

        if(rotationY < -90)
            rotationY = -90;
        else if(rotationY > 90)
            rotationY = 90;
    }

    public float getPosX() {return posX;}
    public float getPosY() {return posY;}
    public float getPosZ() {return posZ;}

    public float getRotationX() {return rotationX;}
    public float getRotationY() {return rotationY;}

    public float getPossibleX() {return possiblePosX;}
  //  public float getPossibleY() {return possiblePosY;}
    public float getPossibleZ() {return possiblePosZ;}

    public void setDirection(float deltaX, float deltaZ) {
        this.deltaX = deltaX;
        this.deltaZ = deltaZ;
    }
}
