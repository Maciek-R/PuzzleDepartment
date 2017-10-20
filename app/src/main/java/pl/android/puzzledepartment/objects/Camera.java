package pl.android.puzzledepartment.objects;

import pl.android.puzzledepartment.util.geometry.Vector3f;

/**
 * Created by Maciek Ruszczyk on 2017-10-06.
 */

public class Camera {

    private float posX;
    private float posY;
    private float posZ;

    private float rotationX;
    private float rotationY;

    private float possiblePosX;
    private float possiblePosY;
    private float possiblePosZ;

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
        this.posY = possiblePosY + 1.5f;
        this.posZ = possiblePosZ;
    }

    public void countNextPossiblePosition(float transZ, float transX, HeightMap heightMap) {
        possiblePosX = posX;
        possiblePosZ = posZ;

        float translationX = -(float) (Math.sin(Math.toRadians(rotationX)) * transZ);
        float translationZ = (float) (Math.cos(Math.toRadians(rotationX)) * transZ);

        possiblePosX += translationX;
        possiblePosZ += translationZ;

        translationX = (float) (Math.sin(Math.toRadians(rotationX + 90)) * transX);
        translationZ = -(float) (Math.cos(Math.toRadians(rotationX + 90)) * transX);

        possiblePosX += translationX;
        possiblePosZ += translationZ;

        final float height = heightMap.getHeight(possiblePosX, possiblePosZ);
        possiblePosY = height;
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
    public float getPossibleY() {return possiblePosY;}
    public float getPossibleZ() {return possiblePosZ;}
}
