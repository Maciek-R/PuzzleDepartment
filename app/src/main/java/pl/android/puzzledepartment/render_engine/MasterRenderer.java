package pl.android.puzzledepartment.render_engine;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import pl.android.puzzledepartment.gui.GuiEntity;
import pl.android.puzzledepartment.gui.GuiRenderer;
import pl.android.puzzledepartment.objects.Camera;
import pl.android.puzzledepartment.objects.Dragon;
import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.objects.HeightMap;
import pl.android.puzzledepartment.objects.Light;
import pl.android.puzzledepartment.objects.Skybox;
import pl.android.puzzledepartment.objects.particles.ParticleShooter;
import pl.android.puzzledepartment.objects.particles.ParticleSystem;
import pl.android.puzzledepartment.programs.ColorShaderProgram;
import pl.android.puzzledepartment.programs.GuiShaderProgram;
import pl.android.puzzledepartment.programs.HeightmapShaderProgram;
import pl.android.puzzledepartment.programs.NormalColouredShaderProgram;
import pl.android.puzzledepartment.programs.NormalUncolouredShaderProgram;
import pl.android.puzzledepartment.programs.ParticleShaderProgram;
import pl.android.puzzledepartment.programs.SimpleColorShaderProgram;
import pl.android.puzzledepartment.programs.SkyboxShaderProgram;
import pl.android.puzzledepartment.puzzles.TeleportPuzzle;
import pl.android.puzzledepartment.rooms.Room;
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
    private final EntityRenderer particleRenderer;
    private final SkyboxRenderer skyboxRenderer;
    private final GuiRenderer guiRenderer;

    private final float[] viewMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];

    private final float[] tmp = new float[16];
    private final float[] out = new float[16];


    private List<Dragon> entities;

    private Light light;

    public MasterRenderer(Context context, Light light) {
        entityRenderer = new EntityRenderer(new ColorShaderProgram(context));
        normalColouredEntityRenderer = new EntityRenderer(new NormalColouredShaderProgram(context));
        normalUnColouredEntityRenderer = new EntityRenderer(new NormalUncolouredShaderProgram(context));
        simpleColorRenderer = new EntityRenderer(new SimpleColorShaderProgram(context));
        heightmapRenderer = new HeightmapRenderer(new HeightmapShaderProgram(context));
        particleRenderer = new EntityRenderer(new ParticleShaderProgram(context));
        guiRenderer = new GuiRenderer(new GuiShaderProgram(context));
        skyboxRenderer = new SkyboxRenderer(new SkyboxShaderProgram(context));

        this.light = light;
        entities = new ArrayList<Dragon>();
    }
   /* public void renderNormalColoured(Entity entity) {
        normalColouredEntityRenderer.renderNormalColoured(entity, viewMatrix, projectionMatrix, light);
    }

    public void renderNormalUnColoured(Entity entity) {
        normalUnColouredEntityRenderer.renderNormalUnColoured(entity, viewMatrix, projectionMatrix, light, 1.0f, 0.0f, 0.0f);
    }*/

    public void renderSimpleColor(Entity entity) {
        simpleColorRenderer.render(entity, viewProjectionMatrix, 1.0f, 0.0f, 0.0f);
    }

    public void renderLight(Light light) {
        simpleColorRenderer.render(light, viewProjectionMatrix, light.getLightColor().x, light.getLightColor().y, light.getLightColor().z);
    }

    public void render(List<Entity> entities) {
        for(Entity entity:entities)
            render(entity);
    }

    public void render(Entity entity) {
        entityRenderer.render(entity, viewProjectionMatrix);
    }

    public void render(HeightMap heightMap) {
        heightmapRenderer.render(heightMap, viewMatrix, projectionMatrix);
    }

    public void createProjectionMatrix(int width, int height) {
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float)width / (float) height, 0.1f, 500f);
    }
    public void prepareCamera(Camera camera) {
        setIdentityM(viewMatrix, 0);
        rotateM(viewMatrix, 0, camera.getRotationY(), 1f, 0f, 0f);
        rotateM(viewMatrix, 0, camera.getRotationX(), 0f, 1f, 0f);
        translateM(viewMatrix, 0, -camera.getPosX(), -camera.getLookPosY(), -camera.getPosZ());
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
    }

    public void render(Room room) {
        render(room.getEntities());
    }
    public void render(TeleportPuzzle teleportPuzzle) {
        render(teleportPuzzle.getTeleports());
        for(Room r:teleportPuzzle.getRooms())
            render(r);
    }

    public void renderParticles(ParticleSystem particleSystem, ParticleShooter particleShooter, float currentTime) {
        particleRenderer.renderParticles(particleSystem, particleShooter, viewProjectionMatrix, currentTime);
    }

    public void renderGuis(List<GuiEntity> guiEntities) {
        guiRenderer.render(guiEntities);
    }

    public void renderSkybox(Skybox skybox, Camera camera) {
        setIdentityM(viewMatrix, 0);
        skybox.rotate();
        rotateM(viewMatrix, 0, camera.getRotationY(), 1f, 0f, 0f);
        rotateM(viewMatrix, 0, camera.getRotationX(), 0f, 1f, 0f);
        rotateM(viewMatrix, 0, skybox.getRotation(), 0f, 1f, 0f);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        skyboxRenderer.render(skybox, viewProjectionMatrix);
    }

    public void renderWithNormals(Entity entity, Camera camera) {
        switch(entity.getType())
        {
            case UNCOLOURED:
                normalUnColouredEntityRenderer.renderNormalSpecularUnColoured(entity, viewMatrix, projectionMatrix, light, 1.0f, 0.0f, 0.0f, camera,  entity.getDamper(), entity.getReflectivity());
                break;
            case COLOURED:
                normalColouredEntityRenderer.renderNormalSpecularColoured(entity, viewMatrix, projectionMatrix, light, camera, entity.getDamper(), entity.getReflectivity());
                break;
            case TEXTURED:
                break;
        }
    }
}
