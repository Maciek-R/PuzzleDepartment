package pl.android.puzzledepartment.render_engine;

import android.content.Context;

import java.util.List;

import pl.android.puzzledepartment.objects.Camera;
import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.objects.HeightMap;
import pl.android.puzzledepartment.objects.Light;
import pl.android.puzzledepartment.objects.ShaderCube;
import pl.android.puzzledepartment.programs.ColorShaderProgram;
import pl.android.puzzledepartment.programs.HeightmapShaderProgram;
import pl.android.puzzledepartment.programs.NormalColouredShaderProgram;
import pl.android.puzzledepartment.programs.NormalUncolouredShaderProgram;
import pl.android.puzzledepartment.programs.SimpleColorShaderProgram;
import pl.android.puzzledepartment.util.MatrixHelper;

import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Created by Maciek Ruszczyk on 2017-10-08.
 */

public class MasterRenderer {
    private final EntityRenderer entityRenderer;
    private final EntityRenderer normalColouredEntityRenderer;
    private final EntityRenderer normalUnColouredEntityRenderer;
    private final EntityRenderer simpleColorRenderer;
    private final HeightmapRenderer heightmapRenderer;

    private final float[] viewMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];

    private Light light;

    public MasterRenderer(Context context, Light light) {
        entityRenderer = new EntityRenderer(new ColorShaderProgram(context));
        normalColouredEntityRenderer = new EntityRenderer(new NormalColouredShaderProgram(context));
        normalUnColouredEntityRenderer = new EntityRenderer(new NormalUncolouredShaderProgram(context));
        simpleColorRenderer = new EntityRenderer(new SimpleColorShaderProgram(context));
        heightmapRenderer = new HeightmapRenderer(new HeightmapShaderProgram(context));
        this.light = light;
    }
    public void render(List<Entity> entities) {
        for(Entity entity:entities)
            render(entity);
    }

    public void renderNormalColoured(ShaderCube shaderCube) {
        normalColouredEntityRenderer.renderNormalColoured(shaderCube, viewMatrix, projectionMatrix, light);
    }

    public void renderNormalUnColoured(Entity entity) {
        normalUnColouredEntityRenderer.renderNormalUnColoured(entity, viewMatrix, projectionMatrix, light, 1.0f, 0.0f, 0.0f);
    }

    public void renderSimpleColor(Entity entity) {
        simpleColorRenderer.render(entity, viewProjectionMatrix, 1.0f, 0.0f, 0.0f);
    }

    public void renderLight(Light light) {
        simpleColorRenderer.render(light, viewProjectionMatrix, light.getLightColor().x, light.getLightColor().y, light.getLightColor().z);
    }

    public void render(Entity entity) {
        entityRenderer.render(entity, viewProjectionMatrix);
    }

    public void render(HeightMap heightMap) {
        heightmapRenderer.render(heightMap, viewProjectionMatrix);
    }

    public void createProjectionMatrix(int width, int height) {
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float)width / (float) height, 0.5f, 50f);
    }
    public void prepareCamera(Camera camera) {
        setIdentityM(viewMatrix, 0);
        rotateM(viewMatrix, 0, camera.getRotationY(), 1f, 0f, 0f);
        rotateM(viewMatrix, 0, camera.getRotationX(), 0f, 1f, 0f);
        translateM(viewMatrix, 0, -camera.getPosX(), -camera.getPosY(), -camera.getPosZ());
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
    }
}
