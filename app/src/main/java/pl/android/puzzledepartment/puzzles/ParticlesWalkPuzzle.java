package pl.android.puzzledepartment.puzzles;

import android.graphics.Color;
import android.util.Log;

import java.util.Random;

import pl.android.puzzledepartment.objects.Camera;
import pl.android.puzzledepartment.objects.particles.ParticleShooter;
import pl.android.puzzledepartment.objects.particles.ParticleSystem;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

/**
 * Created by Maciek Ruszczyk on 2017-11-25.
 */

public class ParticlesWalkPuzzle {

    private Point pos;
    private ParticleSystem particleSystem;
    private ParticleShooter particleShooter;

    private final static float MAX_CAMERA_SPEED = 0.05f;
    private final static int COLOR_NUMBERS = 3;
    private int colors[];
    private final Random random;
    private boolean calm = false;

    public ParticlesWalkPuzzle(Point pos, int particleTexture) {
        this.pos = pos;
        particleSystem = new ParticleSystem(10000, particleTexture);
        random = new Random();
        colors = new int[COLOR_NUMBERS];
        colors[0] = Color.rgb(255, 50, 5);
        colors[1] = Color.rgb(50, 205, 10);
        colors[2] = Color.rgb(10, 50, 255);
        particleShooter = new ParticleShooter(new Point(pos.x, pos.y, pos.z), new Vector3f(0f, 0.5f, 0f), colors[0], 360f, 1.5f);
    }

    public void update(float elapsedTime, Camera camera) {
        particleShooter.changeColor(colors[random.nextInt(COLOR_NUMBERS)]);
        //Log.v("DELTAX", new Float(Math.abs(camera.getDeltaX() + camera.getDeltaZ())).toString());
        if (Math.abs(camera.getDeltaX() + camera.getDeltaZ()) > MAX_CAMERA_SPEED)
            particleShooter.increaseSpeedMultiplier();
        else
            particleShooter.decreaseSpeedMultiplier();

        calm = particleShooter.areParticleCalm();
        particleShooter.addParticles(particleSystem, elapsedTime, 5);
    }

    public ParticleSystem getParticleSystem() {
        return particleSystem;
    }
}
