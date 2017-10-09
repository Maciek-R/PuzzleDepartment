package pl.android.puzzledepartment.render_engine;

import android.content.Context;

import java.util.List;

import pl.android.puzzledepartment.objects.Camera;
import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.objects.HeightMap;
import pl.android.puzzledepartment.programs.ColorShaderProgram;
import pl.android.puzzledepartment.programs.HeightmapShaderProgram;
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
    private final HeightmapRenderer heightmapRenderer;

    private final float[] viewMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];

    public MasterRenderer(Context context) {
        entityRenderer = new EntityRenderer(new ColorShaderProgram(context));
        heightmapRenderer = new HeightmapRenderer(new HeightmapShaderProgram(context));
    }
    public void render(List<Entity> entities) {
        for(Entity entity:entities)
            render(entity);
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
