package pl.android.puzzledepartment.render_engine;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import pl.android.puzzledepartment.gui.GuiEntity;
import pl.android.puzzledepartment.gui.GuiRenderer;
import pl.android.puzzledepartment.objects.Camera;
import pl.android.puzzledepartment.objects.Dragon;
import pl.android.puzzledepartment.objects.complex_objects.DragonStatue;
import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.objects.HeightMap;
import pl.android.puzzledepartment.objects.Light;
import pl.android.puzzledepartment.objects.Skybox;
import pl.android.puzzledepartment.objects.particles.ParticleSystem;
import pl.android.puzzledepartment.programs.color_programs.AttributeColorShaderProgram;
import pl.android.puzzledepartment.programs.color_programs.ColorShaderProgram;
import pl.android.puzzledepartment.programs.entity_programs.EntityColouredNotShiningShaderProgram;
import pl.android.puzzledepartment.programs.entity_programs.EntityColouredShiningShaderProgram;
import pl.android.puzzledepartment.programs.entity_programs.EntityShaderProgram;
import pl.android.puzzledepartment.programs.GuiShaderProgram;
import pl.android.puzzledepartment.programs.HeightmapShaderProgram;
import pl.android.puzzledepartment.programs.ParticleShaderProgram;
import pl.android.puzzledepartment.programs.color_programs.SimpleColorShaderProgram;
import pl.android.puzzledepartment.programs.SkyboxShaderProgram;
import pl.android.puzzledepartment.programs.entity_programs.EntityUncolouredNotShiningShaderProgram;
import pl.android.puzzledepartment.programs.entity_programs.EntityUncolouredShiningShaderProgram;
import pl.android.puzzledepartment.puzzles.TeleportPuzzle;
import pl.android.puzzledepartment.objects.complex_objects.Room;
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
    private final EntityShaderProgram entityUnColouredNotShiningShaderProgram;
    private final EntityShaderProgram entityColouredNotShiningShaderProgram;
    private final EntityShaderProgram entityUnColouredShiningShaderProgram;
    private final EntityShaderProgram entityColouredShiningShaderProgram;

    private final ColorShaderProgram attributeColorShaderProgram;
    private final ColorShaderProgram simpleColorShaderProgram;

    private final ParticleRenderer particleRenderer;
    private final HeightmapRenderer heightmapRenderer;
    private final SkyboxRenderer skyboxRenderer;

    private final GuiRenderer guiRenderer;

    private final float[] viewMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];

    private List<Dragon> entities;

    private Light light;

    public MasterRenderer(Context context, Light light) {
        entityRenderer = new EntityRenderer();
        entityUnColouredNotShiningShaderProgram = new EntityUncolouredNotShiningShaderProgram(context);
        entityColouredNotShiningShaderProgram = new EntityColouredNotShiningShaderProgram(context);
        entityUnColouredShiningShaderProgram = new EntityUncolouredShiningShaderProgram(context);
        entityColouredShiningShaderProgram = new EntityColouredShiningShaderProgram(context);

        attributeColorShaderProgram = new AttributeColorShaderProgram(context);
        simpleColorShaderProgram = new SimpleColorShaderProgram(context);

        particleRenderer = new ParticleRenderer(new ParticleShaderProgram(context));
        heightmapRenderer = new HeightmapRenderer(new HeightmapShaderProgram(context));
        skyboxRenderer = new SkyboxRenderer(new SkyboxShaderProgram(context));

        guiRenderer = new GuiRenderer(new GuiShaderProgram(context));

        this.light = light;
        entities = new ArrayList<Dragon>();
    }

    public void render(List<Entity> entities) {
        for(Entity entity:entities)
            render(entity);
    }

    public void render(Entity entity) {
        ColorShaderProgram colorShaderProgram = null;
        if(Entity.Type.UNCOLOURED.equals(entity.getType()))
            colorShaderProgram = simpleColorShaderProgram;

        else if(Entity.Type.COLOURED.equals(entity.getType()))
            colorShaderProgram = attributeColorShaderProgram;

        entityRenderer.render(colorShaderProgram, entity, viewProjectionMatrix);
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

    public void render(DragonStatue dragonStatue){
        render(dragonStatue.getCube());
    }
    public void render(Room room) {
        render(room.getEntities());
    }
    public void render(TeleportPuzzle teleportPuzzle) {
        render(teleportPuzzle.getTeleports());
        for(Room r:teleportPuzzle.getRooms())
            render(r);
    }

    public void render(ParticleSystem particleSystem, float currentTime) {
        particleRenderer.render(particleSystem, viewProjectionMatrix, currentTime);
    }

    public void renderGuis(List<GuiEntity> guiEntities) {
        guiRenderer.render(guiEntities);
    }

    public void render(Skybox skybox, Camera camera) {
        setIdentityM(viewMatrix, 0);
        skybox.rotate();
        rotateM(viewMatrix, 0, camera.getRotationY(), 1f, 0f, 0f);
        rotateM(viewMatrix, 0, camera.getRotationX(), 0f, 1f, 0f);
        rotateM(viewMatrix, 0, skybox.getRotation(), 0f, 1f, 0f);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        skyboxRenderer.render(skybox, viewProjectionMatrix);
    }

    public void renderWithNormals(Entity entity, Camera camera) {
        EntityShaderProgram entityShaderProgram = null;
        switch(entity.getType())
        {
            case UNCOLOURED:
                if(entity.isShining()) {
                    entityShaderProgram = entityUnColouredShiningShaderProgram;
                    break;
                }
                else {
                    entityShaderProgram = entityUnColouredNotShiningShaderProgram;
                    break;
                }
            case COLOURED:
                if(entity.isShining()) {
                    entityShaderProgram = entityColouredShiningShaderProgram;
                    break;
                }
                else {
                    entityShaderProgram = entityColouredNotShiningShaderProgram;
                    break;
                }
            case TEXTURED:
                if(entity.isShining()) {
                    break;
                }
                else {
                    break;
                }
        }
        entityRenderer.renderWithNormals(entityShaderProgram, entity, viewMatrix, projectionMatrix, light, camera);
    }
}
