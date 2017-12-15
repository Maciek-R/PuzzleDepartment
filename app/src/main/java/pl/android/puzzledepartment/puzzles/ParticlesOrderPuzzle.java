package pl.android.puzzledepartment.puzzles;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.android.puzzledepartment.objects.particles.ParticleCollideShooter;
import pl.android.puzzledepartment.objects.particles.ParticleSystem;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

/**
 * Created by Maciek Ruszczyk on 2017-11-24.
 */

public class ParticlesOrderPuzzle extends AbstractPuzzle{
    private static final int PARTICLES_SHOOTERS_COUNT = 5;

    private Point pos;
    private ParticleSystem particleSystem;
    private ParticleCollideShooter particleShooters[];

    private List<ParticleCollideShooter> particleShootersOrderLevel;
    private List<ParticleCollideShooter> particleShootersOrderLevelAlreadyPicked;
    private int currentLevel = 0;

    public ParticlesOrderPuzzle(Point pos, int particleTexture) {
        this.pos = pos;
        particleSystem = new ParticleSystem(10000, particleTexture);
        particleShooters = new ParticleCollideShooter[PARTICLES_SHOOTERS_COUNT];
        for(int i=0; i<PARTICLES_SHOOTERS_COUNT; ++i) {
            float angleInRadians = ((float)i / (float)PARTICLES_SHOOTERS_COUNT) * ((float) Math.PI * 2f);
            float transX = (float)Math.cos(angleInRadians) * 3f;
            float transZ = (float)Math.sin(angleInRadians) * 3f;
            particleShooters[i] = new ParticleCollideShooter(new Point(pos.x + transX, pos.y, pos.z + transZ), new Vector3f(0f, 0.5f, 0f), Color.rgb(255, 50, 5), 360f, 0.5f);
        }
        randomParticlesOrderToPick();
    }

    private void randomParticlesOrderToPick() {
        particleShootersOrderLevelAlreadyPicked = new ArrayList<ParticleCollideShooter>();
        particleShootersOrderLevel = new ArrayList<ParticleCollideShooter>(PARTICLES_SHOOTERS_COUNT);
        for(int i=0; i<PARTICLES_SHOOTERS_COUNT; ++i)
            particleShootersOrderLevel.add(particleShooters[i]);

        Collections.shuffle(particleShootersOrderLevel);
    }

    public void checkIfChoseCorrectParticle(ParticleCollideShooter particleCollideShooter) {
        if(currentLevel>=PARTICLES_SHOOTERS_COUNT){
            return;
        }

        if (particleCollideShooter == particleShootersOrderLevel.get(currentLevel)) {
            particleCollideShooter.changeColorToGreen();
            particleShootersOrderLevelAlreadyPicked.add(particleCollideShooter);
            if(++currentLevel == PARTICLES_SHOOTERS_COUNT)
                isCompleted = true;
        }
        else if(!isAlreadyPicked(particleCollideShooter))
            reset();
    }

    private boolean isAlreadyPicked(ParticleCollideShooter particleCollideShooter) {
        return particleShootersOrderLevelAlreadyPicked.contains(particleCollideShooter);
    }

    private void reset() {
        for (ParticleCollideShooter particleCollideShooter : particleShooters) {
            particleCollideShooter.changeColorToRed();
        }
        particleShootersOrderLevelAlreadyPicked.clear();
        currentLevel = 0;
    }

    @Override
    public void update(float elapsedTime) {
        for(int i=0; i<PARTICLES_SHOOTERS_COUNT; ++i) {
            particleShooters[i].addParticles(particleSystem, elapsedTime, 5);
        }
    }

    public ParticleSystem getParticleSystem() {
        return particleSystem;
    }

    public ParticleCollideShooter[] getParticleShooters() {
        return particleShooters;
    }

    @Override
    public Point getKeySpawnPosition() {
        return new Point(pos.x, pos.y, pos.z);
    }
    @Override
    public int getKeyColor() {
        return Color.YELLOW;
    }
}
