package pl.android.puzzledepartment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import pl.android.puzzledepartment.gui.GuiEntity;
import pl.android.puzzledepartment.managers.ActionManager;
import pl.android.puzzledepartment.managers.CollisionManager;
import pl.android.puzzledepartment.managers.EntityManager;
import pl.android.puzzledepartment.managers.TimeManager;
import pl.android.puzzledepartment.objects.Camera;
import pl.android.puzzledepartment.objects.Cube;
import pl.android.puzzledepartment.objects.Cylinder;
import pl.android.puzzledepartment.objects.Dragon;
import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.objects.HeightMap;
import pl.android.puzzledepartment.objects.Key;
import pl.android.puzzledepartment.objects.Light;
import pl.android.puzzledepartment.objects.ShaderCube;
import pl.android.puzzledepartment.objects.Skybox;
import pl.android.puzzledepartment.objects.TerrainTexture;
import pl.android.puzzledepartment.objects.TerrainTexturePack;
import pl.android.puzzledepartment.objects.particles.ParticleShooter;
import pl.android.puzzledepartment.objects.particles.ParticleSystem;
import pl.android.puzzledepartment.puzzles.AbstractPuzzle;
import pl.android.puzzledepartment.puzzles.ChessPuzzle;
import pl.android.puzzledepartment.puzzles.DragonStatuePuzzle;
import pl.android.puzzledepartment.puzzles.MixColorPuzzle;
import pl.android.puzzledepartment.puzzles.ParticlesOrderPuzzle;
import pl.android.puzzledepartment.puzzles.ParticlesWalkPuzzle;
import pl.android.puzzledepartment.puzzles.TeleportPuzzle;
import pl.android.puzzledepartment.render_engine.MasterRenderer;
import pl.android.puzzledepartment.objects.complex_objects.Room;
import pl.android.puzzledepartment.util.Logger;
import pl.android.puzzledepartment.util.TextureHelper;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector2f;
import pl.android.puzzledepartment.util.geometry.Vector3f;

import static android.opengl.GLES20.GL_BACK;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_CULL_FACE;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;

import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glCullFace;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;


/**
 * Created by Maciek Ruszczyk on 2017-10-06.
 */

public class MainGameRenderer implements Renderer {

    private final Context context;

    private ParticleSystem particleSystem;
    private ParticleShooter redParticleShooter;
    private ParticleShooter blueParticleShooter;
    private int particleTexture;

    private Cube cube;
    private ShaderCube shaderCube;
    private Cylinder cylinder;
    private List<Dragon> dragons;
    private Dragon dragon;
    private List<Entity> keys;
    private Light light;
    private HeightMap heightMap;
    private Room room;
    private Camera camera;

    private MasterRenderer masterRenderer;
    private CollisionManager collisionManager;
    private ActionManager actionManager;
    private EntityManager entityManager;

    private List<AbstractPuzzle> puzzles;
    private final Random random = new Random();

    private List<GuiEntity> guiEntities = new ArrayList<GuiEntity>();
    private GuiEntity actionGuiEntity;
    private int guiTexture;

    private Skybox skybox;
    private long globalStartTime;

    public MainGameRenderer(Context context){
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glEnable(GL_DEPTH_TEST);

        camera = new Camera(0f, 0f, 0f);
        skybox = new Skybox(TextureHelper.loadCubeMap(context, new int[]{R.drawable.left, R.drawable.right, R.drawable.bottom, R.drawable.top, R.drawable.front, R.drawable.back}));
        guiTexture = TextureHelper.loadTexture(context, R.drawable.action);
        actionGuiEntity = new GuiEntity(guiTexture, new Vector2f(-0.6f, 0.6f), new Vector2f(0.2f, 0.2f));
        guiEntities.add(actionGuiEntity);
        particleTexture = TextureHelper.loadTexture(context, R.drawable.particle_texture);
        particleSystem = new ParticleSystem(10000, particleTexture);
        globalStartTime = System.nanoTime();

        redParticleShooter = new ParticleShooter(new Point(2f, 4.0f, -20f), new Vector3f(0f, 0.5f, 0f), Color.rgb(255, 50, 5), 60f, 0.5f);
        blueParticleShooter = new ParticleShooter(new Point(30f, 4.0f, -20f), new Vector3f(0f, 0.5f, 0f), Color.rgb(10, 10, 255), 360f, 0.5f);

        entityManager = new EntityManager(context);
        puzzles = new ArrayList<AbstractPuzzle>();
        puzzles.add(new TeleportPuzzle(new Point(15f, 2f, -8f), context));
        puzzles.add(new ParticlesOrderPuzzle(new Point(25f, 2f, -90f), particleTexture));
        puzzles.add(new ParticlesWalkPuzzle(new Point(23f, 5f, -70f), particleTexture, camera));
        puzzles.add(new ChessPuzzle(new Point(20f, 5f, -60f)));
        puzzles.add(new DragonStatuePuzzle(new Point(10.0f, 5.5f, 10.0f), entityManager.getEntityModel(R.raw.dragon)));
        puzzles.add(new MixColorPuzzle(new Point(10.0f, 8f, 10.0f)));

        cube = new Cube(new Point(-16f, 3.0f, -33f), new Vector3f(5f, 5f, 5f));
        shaderCube = new ShaderCube(new Point(-0.5f, 5.0f, -3.0f));
        cylinder = new Cylinder(new Point(0.0f, 6.0f, -5.0f));
        light = new Light(new Point(2f, 4.5f, 3f), Color.rgb(255, 255, 255));
        dragon = new Dragon(new Point(-2f, 3f, -2f), entityManager.getEntityModel(R.raw.dragon));
        keys = new ArrayList<>();
        heightMap = new HeightMap(((BitmapDrawable)context.getResources().getDrawable(R.drawable.heightmap)).getBitmap()
                    , new Vector3f(200f, 10f, 200f)
                    , new TerrainTexturePack(new TerrainTexture(TextureHelper.loadTexture(context, R.drawable.mountain))
                                            , new TerrainTexture(TextureHelper.loadTexture(context, R.drawable.mud))
                                            , new TerrainTexture(TextureHelper.loadTexture(context, R.drawable.grassy2))
                                            , new TerrainTexture(TextureHelper.loadTexture(context, R.drawable.water)))
                    , new TerrainTexture(TextureHelper.loadTexture(context, R.drawable.bluredcolourmap)));
        room = new Room(new Point(-25f, 0.5f, 25f), 3f, 20f);

        masterRenderer = new MasterRenderer(context, light, camera);
        collisionManager = new CollisionManager();
        collisionManager.add(cube);
        collisionManager.add(room);
        collisionManager.add(puzzles);

        actionManager = new ActionManager();
        actionManager.addPuzzle(puzzles);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);
        masterRenderer.createProjectionMatrix(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        TimeManager.update();

        masterRenderer.render(skybox);
        masterRenderer.prepareCamera(camera);
        masterRenderer.render(heightMap);
        masterRenderer.render(light);
        masterRenderer.render(cube);
        masterRenderer.render(room);

        masterRenderer.renderWithNormals(shaderCube);
        masterRenderer.renderWithNormals(cylinder);
        masterRenderer.renderWithNormals(dragon);
        masterRenderer.renderWithNormals(keys);

        for(AbstractPuzzle puzzle:puzzles) {
            if(puzzle.isCompleted() && !puzzle.wasKeySpawned()) {
                keys.add(new Key(puzzle.getKeySpawnPosition(), puzzle.getKeyColor(), entityManager.getEntityModel(R.raw.key)));
                puzzle.setWasKeySpawned(true);
            }
        }
        updateAndRenderPuzzles();

        light.move2();
        camera.update(heightMap, collisionManager);
        actionManager.moveInActionObjects();
        if(actionManager.isNearAnyActionableObject(camera))
            actionGuiEntity.setIsVisible(true);
        else
            actionGuiEntity.setIsVisible(false);

        masterRenderer.renderGuis(guiEntities);

        dragon.rotate(60.0f);
    }

    private void updateAndRenderPuzzles() {
        float elapsedTime = TimeManager.getElapsedTimeFromBeginningInSeconds();
        for (AbstractPuzzle puzzle : puzzles) {
            puzzle.update();
            puzzle.update(elapsedTime);
            masterRenderer.render(puzzle, elapsedTime);
        }

        redParticleShooter.addParticles(particleSystem, elapsedTime, 5);
        blueParticleShooter.addParticles(particleSystem, elapsedTime, 5);
        masterRenderer.render(particleSystem, elapsedTime);
        masterRenderer.render(particleSystem, elapsedTime);
    }

    public void handleMoveCamera(float deltaMoveX, float deltaMoveY) {
        camera.setDirection(deltaMoveX/1024f, deltaMoveY/1024f);
    }

    public void handleRotationCamera(float deltaRotateX, float deltaRotateY) {
        camera.rotateY(deltaRotateY / 8f);
        camera.rotateX(deltaRotateX / 8f);
    }

    public void handleTouchPress(float normalizedX, float normalizedY){
        if(Logger.ON)
            Log.v("RENDERER", "Touch Press: X: "+normalizedX+" Y: "+normalizedY);

        if (actionGuiEntity.pressed(normalizedX, normalizedY)) {
            actionManager.activate();
        }
    }

    public void handleJumpCamera() {
        camera.jump();
    }
}
