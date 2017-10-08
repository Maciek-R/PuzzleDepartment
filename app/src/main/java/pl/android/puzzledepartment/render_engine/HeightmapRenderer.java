package pl.android.puzzledepartment.render_engine;

import java.util.List;

import pl.android.puzzledepartment.objects.Camera;
import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.objects.HeightMap;
import pl.android.puzzledepartment.programs.HeightmapShaderProgram;
import pl.android.puzzledepartment.programs.ShaderProgram;
import pl.android.puzzledepartment.util.MatrixHelper;

import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Created by Maciek on 2017-10-08.
 */

public class HeightmapRenderer {
    private final HeightmapShaderProgram heightmapShaderProgram;
    private final float[] modelMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];

    public HeightmapRenderer(HeightmapShaderProgram heightmapShaderProgram) {
        this.heightmapShaderProgram = heightmapShaderProgram;
    }

    public void render(HeightMap heightMap, final float[] viewProjectionMatrix) {
        setIdentityM(modelMatrix, 0);
        scaleM(modelMatrix, 0, heightMap.getScale().x, heightMap.getScale().y, heightMap.getScale().z);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
        heightmapShaderProgram.useProgram();
        heightmapShaderProgram.setUniforms(modelViewProjectionMatrix);
        heightMap.bindData(heightmapShaderProgram);
        heightMap.draw();
    }
}
