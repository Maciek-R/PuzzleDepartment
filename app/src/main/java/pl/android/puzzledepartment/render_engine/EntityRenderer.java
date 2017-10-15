package pl.android.puzzledepartment.render_engine;

import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.objects.Light;
import pl.android.puzzledepartment.objects.ShaderCube;
import pl.android.puzzledepartment.programs.ShaderProgram;

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
        bindDataAndDraw(entity, shaderProgram);
    }

    public void render(Entity entity, final float[] viewProjectionMatrix, float r, float g, float b) {
        prepareMatrix(entity, viewProjectionMatrix);
        shaderProgram.useProgram();
        shaderProgram.setUniforms(modelViewProjectionMatrix, r, g, b);
        bindDataAndDraw(entity, shaderProgram);
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

    private void bindDataAndDraw(Entity entity, ShaderProgram shaderProgram) {
        entity.bindData(shaderProgram);
        entity.draw();
    }

    public void renderNormalColoured(ShaderCube shaderCube, float[] viewMatrix, float[] projectionMatrix, Light light) {
        prepareModelMatrix(shaderCube);
        multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        invertM(tempMatrix, 0, modelMatrix, 0);
        transposeM(invertedModelMatrix, 0, tempMatrix, 0);
        multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);
        shaderProgram.useProgram();
        shaderProgram.setUniforms(invertedModelMatrix, modelViewProjectionMatrix, light);
        bindDataAndDraw(shaderCube, shaderProgram);
    }

    public void renderNormalUnColoured(Entity entity, float[] viewMatrix, float[] projectionMatrix, Light light, float r, float g, float b) {
        prepareModelMatrix(entity);
        multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        invertM(tempMatrix, 0, modelMatrix, 0);
        transposeM(invertedModelMatrix, 0, tempMatrix, 0);
        multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);
        shaderProgram.useProgram();
        shaderProgram.setUniforms(invertedModelMatrix, modelViewProjectionMatrix, light, r, g, b);
        bindDataAndDraw(entity, shaderProgram);
    }
}
