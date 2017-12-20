package pl.android.puzzledepartment.managers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.gui.GuiEntity;
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
import pl.android.puzzledepartment.objects.complex_objects.EndTower;
import pl.android.puzzledepartment.objects.complex_objects.Room;
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
import pl.android.puzzledepartment.util.TextureHelper;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector2f;
import pl.android.puzzledepartment.util.geometry.Vector3f;


/**
 * Created by Maciek Ruszczyk on 2017-12-16.
 */

public class GameManager {

    private final Context context;

    private Cube cube;
    private ShaderCube shaderCube;
    private Cylinder cylinder;
    private List<Dragon> dragons;
    private EndTower endTower;
    private Dragon dragon;
    private List<Entity> keys;
    private Light light;
    private HeightMap heightMap;
    private Room room;
    private Camera camera;
    private List<AbstractPuzzle> puzzles;
    private Skybox skybox;
    private List<GuiEntity> guiEntities = new ArrayList<GuiEntity>();
    private GuiEntity actionGuiEntity;
    private int guiTexture;

    private ParticleSystem particleSystem;
    private ParticleShooter redParticleShooter;
    private ParticleShooter blueParticleShooter;
    private int particleTexture;
    private final Random random = new Random();
    private long globalStartTime;

    private EntityManager entityManager;
    private MasterRenderer masterRenderer;
    private CollisionManager collisionManager;
    private ActionManager actionManager;

    public GameManager(Context context) {
        this.context = context;

        entityManager = new EntityManager(context);
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

        puzzles = new ArrayList<AbstractPuzzle>();
        puzzles.add(new TeleportPuzzle(context, new Point(15f, 2f, -8f)));
        puzzles.add(new ParticlesOrderPuzzle(context, new Point(25f, 2f, -90f), particleTexture));
        puzzles.add(new ParticlesWalkPuzzle(context, new Point(23f, 5f, -70f), particleTexture, camera));
        puzzles.add(new ChessPuzzle(context, new Point(20f, 5f, -60f)));
        puzzles.add(new DragonStatuePuzzle(context, new Point(10.0f, 5.5f, 10.0f), entityManager.getEntityModel(R.raw.dragon)));
        puzzles.add(new MixColorPuzzle(context, new Point(10.0f, 8f, 10.0f), entityManager.getEntityModel(R.raw.lever_base), entityManager.getEntityModel(R.raw.lever_hand)));

        cube = new Cube(new Point(-16f, 3.0f, -33f), new Vector3f(5f, 5f, 5f));
        shaderCube = new ShaderCube(new Point(-0.5f, 5.0f, -3.0f));
        endTower = new EndTower(new Point(-5f, 2.0f, 45f), entityManager.getEntityModel(R.raw.endtower), entityManager.getEntityModel(R.raw.door));
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
        collisionManager.addObserver(this);
        collisionManager.add(cube);
        collisionManager.add(room);
        collisionManager.add(puzzles);
        collisionManager.add(endTower);

        actionManager = new ActionManager();
        actionManager.addPuzzle(puzzles);
        actionManager.add(endTower);
    }

    public void update() {
        masterRenderer.render(skybox);
        masterRenderer.prepareCamera(camera);
        masterRenderer.render(heightMap);
        masterRenderer.render(light);
        masterRenderer.render(cube);
        masterRenderer.render(room);
        masterRenderer.render(endTower);

        masterRenderer.renderWithNormals(shaderCube);
        masterRenderer.renderWithNormals(cylinder);
        masterRenderer.renderWithNormals(dragon);
        masterRenderer.renderWithNormals(keys);

        for(AbstractPuzzle puzzle:puzzles) {
            if(puzzle.isCompleted() && !puzzle.wasKeySpawned()) {
                Key key = new Key(puzzle.getKeySpawnPosition(), puzzle.getKeyColor(), puzzle.getKeyGuiTexture(), entityManager.getEntityModel(R.raw.key));
                keys.add(key);
                collisionManager.add(key);
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
        for (Entity key : keys) {
            key.update();
        }
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

    public void handleJump() {
        camera.jump();
    }

    public void handleTouchPress(float normalizedX, float normalizedY) {
        if (actionGuiEntity.pressed(normalizedX, normalizedY)) {
            actionManager.activate();
        }
    }

    public void setCameraRotation(float deltaRotationX, float deltaRotationY) {
        camera.rotateY(deltaRotationY);
        camera.rotateX(deltaRotationX);
    }

    public void setCameraDirection(float deltaMoveX, float deltaMoveY) {
        camera.setDirection(deltaMoveX, deltaMoveY);
    }

    public void createProjectionMatrix(int width, int height) {
        masterRenderer.createProjectionMatrix(width, height);
    }

    public void onCollisionNotify(Entity entity) {
        entity.onCollisionNotify();
        if (entity instanceof Key) {
            guiEntities.add(((Key)entity).getGuiEntity());
            GameState.INSTANCE.incKeysTakenCount();
        }
    }
}
