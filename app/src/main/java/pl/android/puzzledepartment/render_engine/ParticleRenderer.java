package pl.android.puzzledepartment.render_engine;

import pl.android.puzzledepartment.objects.particles.ParticleSystem;
import pl.android.puzzledepartment.programs.ParticleShaderProgram;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDepthMask;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;

/**
 * Created by Maciek Ruszczyk on 2017-11-19.
 */

public class ParticleRenderer {
    private final ParticleShaderProgram particleShaderProgram;

    public ParticleRenderer(ParticleShaderProgram particleShaderProgram) {
        this.particleShaderProgram = particleShaderProgram;
        particleShaderProgram.useProgram();
        particleShaderProgram.loadTextureUnits();
        particleShaderProgram.stopProgram();
    }

    public void render(ParticleSystem particleSystem, float[] viewProjectionMatrix, float currentTime) {
        glEnable(GL_BLEND);
        glDepthMask(false);
        glBlendFunc(GL_ONE, GL_ONE);
        particleShaderProgram.useProgram();
        particleShaderProgram.loadMatrix(viewProjectionMatrix);
        particleShaderProgram.loadTime(currentTime);
        particleShaderProgram.bindTextures(particleSystem);
        particleSystem.bindData(particleShaderProgram);
        particleSystem.draw();
        particleShaderProgram.stopProgram();
        glDepthMask(true);
        glDisable(GL_BLEND);
    }
}
