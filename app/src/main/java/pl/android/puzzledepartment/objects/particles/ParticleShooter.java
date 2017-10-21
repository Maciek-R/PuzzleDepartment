package pl.android.puzzledepartment.objects.particles;

import java.util.Random;

import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

import static android.opengl.Matrix.multiplyMV;
import static android.opengl.Matrix.setRotateEulerM;

/**
 * Created by Maciek Ruszczyk on 2017-10-21.
 */

public class ParticleShooter {
    private Point pos;
    private final int color;

    private final float angleVariance;
    private final float speedVariance;

    private float[] directionVector = new float[4];
    private float[] resultVec = new float[4];
    private float[] rotationMatrix = new float[16];
    private final Random random = new Random();

    public ParticleShooter(Point pos, Vector3f direction, int color, float angleVariance, float speedVariance) {
        this.pos = pos;
        this.color = color;

        this.angleVariance = angleVariance;
        this.speedVariance = speedVariance;

        directionVector[0] = direction.x;
        directionVector[1] = direction.y;
        directionVector[2] = direction.z;
    }

    public void addParticles(ParticleSystem particleSystem, float currentTime, int count) {

        for(int i=0; i<count; ++i) {
            setRotateEulerM(rotationMatrix, 0, (random.nextFloat() - 0.5f) * angleVariance, (random.nextFloat() - 0.5f) * angleVariance, (random.nextFloat() - 0.5f) * angleVariance);
            multiplyMV(resultVec, 0, rotationMatrix, 0, directionVector, 0);
            float speed = random.nextFloat() * speedVariance + 1f;

            Vector3f particleDirection = new Vector3f(resultVec[0] * speed,
                                                    resultVec[1] * speed,
                                                    resultVec[2] * speed);

            particleSystem.addParticle(pos, color, particleDirection, currentTime);
        }
    }
}
