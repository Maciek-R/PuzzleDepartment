package pl.android.puzzledepartment.render_engine;

import java.util.List;

import pl.android.puzzledepartment.objects.Camera;
import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.programs.ShaderProgram;
import pl.android.puzzledepartment.util.MatrixHelper;

import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Created by Maciek on 2017-10-08.
 */

public class EntityRenderer {
    private final ShaderProgram shaderProgram;
    private final float[] modelMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];

    public EntityRenderer(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    public void render(Entity entity, final float[] viewProjectionMatrix) {
        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, entity.getPos().x, entity.getPos().y, entity.getPos().z);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
        shaderProgram.useProgram();
        shaderProgram.setUniforms(modelViewProjectionMatrix);
        entity.bindData(shaderProgram);
        entity.draw();
    }
}
