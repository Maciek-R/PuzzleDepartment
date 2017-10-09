package pl.android.puzzledepartment.objects;

/**
 * Created by Maciek Ruszczyk on 2017-10-06.
 */

public class Camera {

    private float posX;
    private float posY;
    private float posZ;

    private float rotationX;
    private float rotationY;

    public Camera() {
        this(0, 0, 0);
    }

    public Camera(float x, float y, float z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
    }

    public void move(float transZ, float transX) {
        float translationX = -(float) (Math.sin(Math.toRadians(rotationX)) * transZ);
        float translationZ = (float) (Math.cos(Math.toRadians(rotationX)) * transZ);

        posX += translationX;
        posZ += translationZ;

        translationX = (float) (Math.sin(Math.toRadians(rotationX + 90)) * transX);
        translationZ = -(float) (Math.cos(Math.toRadians(rotationX + 90)) * transX);

        posX += translationX;
        posZ += translationZ;
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

    public void setY(HeightMap heightMap) {
        final float height = heightMap.getHeight(posX, posZ);
        this.posY = height + 1.5f;
    }
}
