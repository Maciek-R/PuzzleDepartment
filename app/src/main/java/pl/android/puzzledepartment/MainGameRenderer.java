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
import pl.android.puzzledepartment.objects.DragonStatue;
import pl.android.puzzledepartment.objects.HeightMap;
import pl.android.puzzledepartment.objects.Light;
import pl.android.puzzledepartment.objects.ShaderCube;
import pl.android.puzzledepartment.objects.Skybox;
import pl.android.puzzledepartment.objects.TerrainTexture;
import pl.android.puzzledepartment.objects.TerrainTexturePack;
import pl.android.puzzledepartment.objects.particles.ParticleShooter;
import pl.android.puzzledepartment.objects.particles.ParticleSystem;
import pl.android.puzzledepartment.puzzles.TeleportPuzzle;
import pl.android.puzzledepartment.render_engine.MasterRenderer;
import pl.android.puzzledepartment.rooms.Room;
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

import static android.opengl.GLES20.GL_FRONT;
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
    private Light light;
    private HeightMap heightMap;
    private Room room;
    private DragonStatue dragonStatue;
    private Camera camera;

    private MasterRenderer masterRenderer;
    private CollisionManager collisionManager;
    private ActionManager actionManager;
    private EntityManager entityManager;

    private TeleportPuzzle teleportPuzzle;
    private final Random random = new Random();

    private List<GuiEntity> guiEntities = new ArrayList<GuiEntity>();
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
        guiEntities.add(new GuiEntity(guiTexture, new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f)));
        particleTexture = TextureHelper.loadTexture(context, R.drawable.particle_texture);
        particleSystem = new ParticleSystem(10000, particleTexture);
        globalStartTime = System.nanoTime();
        final Vector3f particleDirection = new Vector3f(0f, 0.5f, 0f);
        redParticleShooter = new ParticleShooter(new Point(2f, 4.0f, -20f), particleDirection, Color.rgb(255, 50, 5), 60f, 0.5f);
        blueParticleShooter = new ParticleShooter(new Point(30f, 4.0f, -20f), particleDirection, Color.rgb(10, 10, 255), 360f, 0.5f);

        entityManager = new EntityManager(context);

        teleportPuzzle = new TeleportPuzzle(new Point(15f, 2f, -8f), context);

        cube = new Cube(new Point(-16f, 3.0f, -33f), new Vector3f(5f, 5f, 5f));
        shaderCube = new ShaderCube(new Point(-0.5f, 5.0f, -3.0f));
        cylinder = new Cylinder(new Point(0.0f, 6.0f, -5.0f));
        light = new Light(new Point(3f, 4.5f, -2), new Vector3f(1f, 1f, 1f));
        light = new Light(new Point(2f, 4.5f, 3f), new Vector3f(1f, 1f, 1f));
        dragon = new Dragon(new Point(-2f, 3f, -2f), entityManager.getEntityModel(R.raw.dragon));
       // dragons = new ArrayList<Dragon>();
        //for(int i=-5; i<5; i+=2)
         //        dragons.add(new Dragon(new Point(i, 3.0f, 0.0f), entityManager.getEntityModel(R.raw.dragon)));
        heightMap = new HeightMap(((BitmapDrawable)context.getResources().getDrawable(R.drawable.heightmap)).getBitmap()
                    , new Vector3f(200f, 10f, 200f)
                    , new TerrainTexturePack(new TerrainTexture(TextureHelper.loadTexture(context, R.drawable.mountain))
                                            , new TerrainTexture(TextureHelper.loadTexture(context, R.drawable.mud))
                                            , new TerrainTexture(TextureHelper.loadTexture(context, R.drawable.grassy2))
                                            , new TerrainTexture(TextureHelper.loadTexture(context, R.drawable.water)))
                    , new TerrainTexture(TextureHelper.loadTexture(context, R.drawable.bluredcolourmap)));
        room = new Room(new Point(-25f, 0.5f, 25f), 3f, 20f);
        dragonStatue = new DragonStatue(new Point(10.0f, 0.5f, 10.0f));

        masterRenderer = new MasterRenderer(context, light);
        collisionManager = new CollisionManager();
        collisionManager.add(cube);
        collisionManager.add(room);
        collisionManager.add(teleportPuzzle);

        actionManager = new ActionManager();
        actionManager.add(dragonStatue);
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

        masterRenderer.renderSkybox(skybox, camera);
        masterRenderer.prepareCamera(camera);
        masterRenderer.render(heightMap);
        masterRenderer.renderLight(light);
        masterRenderer.render(cube);
        masterRenderer.renderWithNormals(shaderCube, camera);
        masterRenderer.renderWithNormals(cylinder, camera);
        masterRenderer.render(room);
        masterRenderer.render(teleportPuzzle);
        masterRenderer.render(dragonStatue);

        //masterRenderer.renderNormalUnColoured(dragon);
        masterRenderer.renderWithNormals(dragon, camera);
       // for(Dragon d:dragons)
        //    masterRenderer.renderNormalUnColoured(d);

        light.move2();
        camera.update(heightMap, collisionManager);
        if(actionManager.isNearAnyActionableObject(camera))
            masterRenderer.renderGuis(guiEntities);
        drawParticles();

       // for(Dragon d:dragons)
       //     d.rotate(60f);
       // cube.rotate(0.5f);
        //shaderCube.rotate(-1.0f);
        dragon.rotate(60.0f);
        //cylinder.rotate(1f);
    }
    private void drawParticles() {
        float currentTime = (System.nanoTime() - globalStartTime) / 1000000000f;
        redParticleShooter.addParticles(particleSystem, currentTime, 5);
        blueParticleShooter.addParticles(particleSystem, currentTime, 5);

        masterRenderer.renderParticles(particleSystem, redParticleShooter, currentTime);
        masterRenderer.renderParticles(particleSystem, blueParticleShooter, currentTime);
    }


    public void handleMoveCamera(float deltaMoveX, float deltaMoveY) {
      //  camera.countNextPossiblePosition(deltaMoveY/32f, deltaMoveX/32f, heightMap);
     //   if(!collisionManager.checkCollision(camera))
       //     camera.move();
        camera.setDirection(deltaMoveX/1024f, deltaMoveY/1024f);
    }

    public void handleRotationCamera(float deltaRotateX, float deltaRotateY) {
        camera.rotateY(deltaRotateY / 8f);
        camera.rotateX(deltaRotateX / 8f);
    }

    public void handleTouchPress(float normalizedX, float normalizedY){
        if(Logger.ON)
            Log.v("RENDERER", "Touch Press");
    }

    public void handleJumpCamera() {
        camera.jump();
    }
}
