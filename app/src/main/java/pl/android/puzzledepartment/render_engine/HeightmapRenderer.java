package pl.android.puzzledepartment.render_engine;

import pl.android.puzzledepartment.objects.HeightMap;
import pl.android.puzzledepartment.programs.HeightmapShaderProgram;

import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;

/**
 * Created by Maciek Ruszczyk on 2017-10-08.
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
