package pl.android.puzzledepartment.render_engine;

import pl.android.puzzledepartment.objects.Skybox;
import pl.android.puzzledepartment.programs.ShaderProgram;
import pl.android.puzzledepartment.programs.SkyboxShaderProgram;

/**
 * Created by Maciek Ruszczyk on 2017-10-28.
 */

public class SkyboxRenderer {

    private final SkyboxShaderProgram skyboxShaderProgram;

    public SkyboxRenderer(SkyboxShaderProgram skyboxShaderProgram) {
        this.skyboxShaderProgram = skyboxShaderProgram;
    }


    public void render(Skybox skybox, float[] viewProjectionMatrix) {
        skyboxShaderProgram.useProgram();
        skyboxShaderProgram.setUniforms(viewProjectionMatrix, skybox.getTextureId());
        skybox.bindData(skyboxShaderProgram);
        skybox.draw();
    }
}
