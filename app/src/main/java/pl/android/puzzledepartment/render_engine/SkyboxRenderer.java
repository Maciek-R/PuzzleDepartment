package pl.android.puzzledepartment.render_engine;

import pl.android.puzzledepartment.objects.Skybox;
import pl.android.puzzledepartment.programs.SkyboxShaderProgram;

/**
 * Created by Maciek Ruszczyk on 2017-10-28.
 */

public class SkyboxRenderer {

    private final SkyboxShaderProgram skyboxShaderProgram;

    public SkyboxRenderer(SkyboxShaderProgram skyboxShaderProgram) {
        this.skyboxShaderProgram = skyboxShaderProgram;
        skyboxShaderProgram.useProgram();
        skyboxShaderProgram.loadTextureUnits();
        skyboxShaderProgram.stopProgram();
    }

    public void render(Skybox skybox, float[] viewProjectionMatrix) {
        skyboxShaderProgram.useProgram();
        skyboxShaderProgram.loadMatrix(viewProjectionMatrix);
        skyboxShaderProgram.bindTextures(skybox);
        skybox.bindData(skyboxShaderProgram);
        skybox.draw();
    }
}
