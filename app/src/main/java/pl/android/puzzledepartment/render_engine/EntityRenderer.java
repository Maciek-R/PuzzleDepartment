package pl.android.puzzledepartment.render_engine;


import pl.android.puzzledepartment.objects.Camera;
import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.objects.Light;
import pl.android.puzzledepartment.objects.particles.ParticleShooter;
import pl.android.puzzledepartment.objects.particles.ParticleSystem;
import pl.android.puzzledepartment.programs.ShaderProgram;

import static android.opengl.GLES20.GL_BACK;
import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_CULL_FACE;
import static android.opengl.GLES20.GL_FRONT;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glCullFace;
import static android.opengl.GLES20.glDepthMask;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;
import static android.opengl.Matrix.invertM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;
import static android.opengl.Matrix.transposeM;

/**
 * Created by Maciek Ruszczyk on 2017-10-08.
 */

public class EntityRenderer {
    private final ShaderProgram shaderProgram;
    private final float[] modelMatrix = new float[16];
    private final float[] modelViewMatrix = new float[16];
    private final float[] invertedModelMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];
    private final float[] tempMatrix = new float[16];

    public EntityRenderer(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    public void render(Entity entity, final float[] viewProjectionMatrix) {
        prepareMatrix(entity, viewProjectionMatrix);
        shaderProgram.useProgram();
        shaderProgram.setUniforms(modelViewProjectionMatrix);
        bindDataAndDraw(entity);
    }

    public void render(Entity entity, final float[] viewProjectionMatrix, float r, float g, float b) {
        prepareMatrix(entity, viewProjectionMatrix);
        shaderProgram.useProgram();
        shaderProgram.setUniforms(modelViewProjectionMatrix, r, g, b);
        bindDataAndDraw(entity);
    }

    private void prepareMatrix(Entity entity,  final float[] viewProjectionMatrix) {
        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, entity.getPos().x, entity.getPos().y, entity.getPos().z);
        rotateM(modelMatrix, 0, entity.getRotation(), 0f, 1f, 0f);
        scaleM(modelMatrix, 0, entity.getScale().x, entity.getScale().y, entity.getScale().z);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }

    private void prepareModelMatrix(Entity entity) {
        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, entity.getPos().x, entity.getPos().y, entity.getPos().z);
        rotateM(modelMatrix, 0, entity.getRotation(), 0f, 1f, 0f);
        scaleM(modelMatrix, 0, entity.getScale().x, entity.getScale().y, entity.getScale().z);
    }

    private void bindDataAndDraw(Entity entity) {
        entity.bindData(shaderProgram);
        entity.draw();
    }

    /*public void renderNormalColoured(Entity entity, float[] viewMatrix, float[] projectionMatrix, Light light) {
        prepareModelMatrix(entity);
        multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        invertM(tempMatrix, 0, modelMatrix, 0);
        transposeM(invertedModelMatrix, 0, tempMatrix, 0);
        multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);
        shaderProgram.useProgram();
        shaderProgram.setUniforms(modelMatrix, invertedModelMatrix, modelViewProjectionMatrix, light);
        bindDataAndDraw(entity, shaderProgram);
    }

    public void renderNormalUnColoured(Entity entity, float[] viewMatrix, float[] projectionMatrix, Light light, float r, float g, float b) {
        prepareModelMatrix(entity);
        multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        invertM(tempMatrix, 0, modelMatrix, 0);
        transposeM(invertedModelMatrix, 0, tempMatrix, 0);
        multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);
        shaderProgram.useProgram();
        shaderProgram.setUniforms(modelMatrix, invertedModelMatrix, modelViewProjectionMatrix, light, r, g, b);
        bindDataAndDraw(entity, shaderProgram);
    }*/

    public void renderParticles(ParticleSystem particleSystem, ParticleShooter particleShooter, final float[] viewProjectionMatrix, float currentTime) {
        glEnable(GL_BLEND);
        glDepthMask(false);
        glBlendFunc(GL_ONE, GL_ONE);
        shaderProgram.useProgram();
        shaderProgram.setUniforms(viewProjectionMatrix, currentTime, particleSystem.getTexture());
        particleSystem.bindData(shaderProgram);
        particleSystem.draw();
        glDepthMask(true);
        glDisable(GL_BLEND);
    }

    public void renderNormalSpecularUnColoured(Entity entity, float[] viewMatrix, float[] projectionMatrix, Light light, float r, float g, float b, Camera camera, float damper, float reflectivity) {
        prepareMatricesForNormalVectors(entity, viewMatrix, projectionMatrix);
        shaderProgram.useProgram();
        shaderProgram.setUniforms(modelMatrix, invertedModelMatrix, modelViewProjectionMatrix, light, r, g, b, camera, damper, reflectivity);
        bindDataAndDraw(entity);
    }
    public void renderNormalSpecularColoured(Entity entity, float[] viewMatrix, float[] projectionMatrix, Light light, Camera camera, float damper, float reflectivity) {
        prepareMatricesForNormalVectors(entity, viewMatrix, projectionMatrix);
        shaderProgram.useProgram();
        shaderProgram.setUniforms(modelMatrix, invertedModelMatrix, modelViewProjectionMatrix, light, camera, damper, reflectivity);
        bindDataAndDraw(entity);
    }

    private void prepareMatricesForNormalVectors(Entity entity, float[] viewMatrix, float[] projectionMatrix) {
        prepareModelMatrix(entity);
        multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        invertM(tempMatrix, 0, modelMatrix, 0);
        transposeM(invertedModelMatrix, 0, tempMatrix, 0);
        multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);
    }
}
