package pl.android.puzzledepartment.managers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;

import java.util.ArrayList;
import java.util.List;

import pl.android.puzzledepartment.LoadGameMode;
import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.gui.GuiEntity;
import pl.android.puzzledepartment.objects.Camera;
import pl.android.puzzledepartment.objects.Cube;
import pl.android.puzzledepartment.objects.Department;
import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.objects.HeightMap;
import pl.android.puzzledepartment.objects.Key;
import pl.android.puzzledepartment.objects.Light;
import pl.android.puzzledepartment.objects.Skybox;
import pl.android.puzzledepartment.objects.TerrainTexture;
import pl.android.puzzledepartment.objects.TerrainTexturePack;
import pl.android.puzzledepartment.objects.Tip;
import pl.android.puzzledepartment.objects.complex_objects.EndTower;
import pl.android.puzzledepartment.objects.complex_objects.Room;
import pl.android.puzzledepartment.puzzles.AbstractPuzzle;
import pl.android.puzzledepartment.puzzles.ChessPuzzle;
import pl.android.puzzledepartment.puzzles.DragonStatuePuzzle;
import pl.android.puzzledepartment.puzzles.MixColorPuzzle;
import pl.android.puzzledepartment.puzzles.ParticlesOrderPuzzle;
import pl.android.puzzledepartment.puzzles.ParticlesWalkPuzzle;
import pl.android.puzzledepartment.puzzles.TeleportPuzzle;
import pl.android.puzzledepartment.render_engine.MasterRenderer;
import pl.android.puzzledepartment.state.LoaderGameState;
import pl.android.puzzledepartment.state.SaverGameState;
import pl.android.puzzledepartment.util.TextureHelper;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector2f;
import pl.android.puzzledepartment.util.geometry.Vector3f;


/**
 * Created by Maciek Ruszczyk on 2017-12-16.
 */

public class GameManager {

    private final Context context;
    private final GameState gameState;
    private final SaverGameState saverGameState;
    private final LoaderGameState loaderGameState;

    private Entity elka;
    private Entity mech;
    private Entity mini;
    private Entity mel;

    private Cube cube;
    private EndTower endTower;
    private List<Key> keys;
    private Light light;
    private HeightMap heightMap;
    private Room room;
    private Camera camera;
    private List<AbstractPuzzle> puzzles;
    private Skybox skybox;
    private List<GuiEntity> guiEntities = new ArrayList<GuiEntity>();
    private List<Integer> keyCollectedColors = new ArrayList<Integer>();
    private GuiEntity actionGuiEntity;
    private GuiEntity notEnoughGuiEntity;
    private int guiTexture;
    private int notEnoughKeyTexture;

    private int particleTexture;

    private TextureManager textureManager;
    private EntityManager entityManager;
    private MasterRenderer masterRenderer;
    private CollisionManager collisionManager;
    private ActionManager actionManager;

    public GameManager(Context context, LoadGameMode loadGameMode) {
        this.context = context;
        camera = new Camera(0f, 0f, 0f);
        gameState = new GameState();
        collisionManager = new CollisionManager();
        textureManager = TextureManager.getInstance(context);
        textureManager.clean();
        entityManager = EntityManager.getInstance(context);
        entityManager.cleanVBO();
        saverGameState = new SaverGameState();
        loaderGameState = new LoaderGameState(entityManager, textureManager);

        skybox = new Skybox(TextureHelper.loadCubeMap(context, new int[]{R.drawable.left, R.drawable.right, R.drawable.bottom, R.drawable.top, R.drawable.front, R.drawable.back}));
        guiTexture = textureManager.getTextureId(R.drawable.action);
        actionGuiEntity = new GuiEntity(guiTexture, new Vector2f(-0.6f, 0.6f), new Vector2f(0.2f, 0.2f));
        notEnoughKeyTexture = textureManager.getTextureId(R.drawable.not_enough_keys);
        notEnoughGuiEntity = new GuiEntity(notEnoughKeyTexture, new Vector2f(0.0f, 0.1f), new Vector2f(0.8f, 0.3f));
        GuiEntity tipGuiTeleport = new GuiEntity(textureManager.getTextureId(R.drawable.tip_teleport), new Vector2f(0.0f, 0.1f), new Vector2f(0.8f, 0.3f));
        GuiEntity tipGuiDragon = new GuiEntity(textureManager.getTextureId(R.drawable.tip_dragon), new Vector2f(0.0f, 0.1f), new Vector2f(0.8f, 0.3f));
        GuiEntity tipGuiMixPuzzle = new GuiEntity(textureManager.getTextureId(R.drawable.tip_mix_puzzle), new Vector2f(0.0f, 0.1f), new Vector2f(0.9f, 0.3f));
        GuiEntity tipGuiParticleSlow = new GuiEntity(textureManager.getTextureId(R.drawable.tip_walk), new Vector2f(0.0f, 0.1f), new Vector2f(0.8f, 0.3f));
        GuiEntity tipGuiParticleOrder = new GuiEntity(textureManager.getTextureId(R.drawable.tip_particle), new Vector2f(0.0f, 0.1f), new Vector2f(0.8f, 0.3f));
        GuiEntity tipGuiChess = new GuiEntity(textureManager.getTextureId(R.drawable.tip_chess), new Vector2f(0.0f, 0.1f), new Vector2f(0.9f, 0.3f));

        guiEntities.add(actionGuiEntity);
        guiEntities.add(notEnoughGuiEntity);
        guiEntities.add(tipGuiTeleport);
        guiEntities.add(tipGuiDragon);
        guiEntities.add(tipGuiMixPuzzle);
        guiEntities.add(tipGuiParticleSlow);
        guiEntities.add(tipGuiParticleOrder);
        guiEntities.add(tipGuiChess);

        particleTexture = textureManager.getTextureId(R.drawable.particle_texture);

        cube = new Cube(new Point(-16f, 3.0f, -33f), new Vector3f(5f, 5f, 5f));
        endTower = new EndTower(new Point(-5f, 2.0f, 45f), entityManager.getEntityModel(R.raw.endtower), entityManager.getEntityModel(R.raw.door));
        endTower.addObserver(this);
        light = new Light(new Point(2f, 30.0f, 3f), Color.rgb(255, 255, 255));
        keys = new ArrayList<>();
        heightMap = new HeightMap(((BitmapDrawable)context.getResources().getDrawable(R.drawable.heightmap)).getBitmap()
                , new Vector3f(200f, 10f, 200f)
                , new TerrainTexturePack(new TerrainTexture(textureManager.getTextureId(R.drawable.mountain))
                , new TerrainTexture(textureManager.getTextureId(R.drawable.mud))
                , new TerrainTexture(textureManager.getTextureId(R.drawable.grassy2))
                , new TerrainTexture(textureManager.getTextureId(R.drawable.water)))
                , new TerrainTexture(textureManager.getTextureId(R.drawable.bluredcolourmap)));
        room = new Room(new Point(-25f, 0.5f, 25f), 3f, 20f);

        puzzles = new ArrayList<AbstractPuzzle>();
        puzzles.add(new TeleportPuzzle(context, textureManager, new Point(15f, 2f, -8f), entityManager,
                new Tip(Color.rgb(70, 15, 0), entityManager.getEntityModel(R.raw.tip), tipGuiTeleport)));
        puzzles.add(new ParticlesOrderPuzzle(textureManager, new Point(33f, 1.5f, -81f), particleTexture,
                new Tip(Color.rgb(70, 15, 0), entityManager.getEntityModel(R.raw.tip), tipGuiParticleOrder)));
        puzzles.add(new ParticlesWalkPuzzle(textureManager, new Point(-4.5f, 10.5f, -80f), particleTexture, camera,
                new Tip(Color.rgb(70, 15, 0), entityManager.getEntityModel(R.raw.tip), tipGuiParticleSlow)));
        puzzles.add(new ChessPuzzle(textureManager, new Point(46f, 1f, 60f),
                new Tip(Color.rgb(70, 15, 0), entityManager.getEntityModel(R.raw.tip), tipGuiChess)));
        puzzles.add(new DragonStatuePuzzle(textureManager, new Point(64.0f, 3.0f, 19.0f), entityManager.getEntityModel(R.raw.dragon), entityManager.getEntityModel(R.raw.vase),
                heightMap, new Tip(Color.rgb(70, 15, 0), entityManager.getEntityModel(R.raw.tip), tipGuiDragon)));
        puzzles.add(new MixColorPuzzle(textureManager, new Point(-2.0f, 5f, 69.0f), entityManager.getEntityModel(R.raw.lever_base), entityManager.getEntityModel(R.raw.lever_hand), heightMap,
                new Tip(Color.rgb(70, 15, 0), entityManager.getEntityModel(R.raw.tip), tipGuiMixPuzzle)));

        //tips.add(;


        elka = new Department(new Point(-2.5f, 5f, 46.3f), Color.BLUE, entityManager.getEntityModel(R.raw.elka));
        elka.singleVerRotate(55f);
        mech = new Department(new Point(2f, 5f, 0f), Color.BLUE, entityManager.getEntityModel(R.raw.mech));
        mini = new Department(new Point(4f, 5f, 0f), Color.BLUE, entityManager.getEntityModel(R.raw.mini));
        mel = new Department(new Point(6f, 5f, 0f), Color.BLUE, entityManager.getEntityModel(R.raw.mel));

        masterRenderer = new MasterRenderer(context, light, camera);
        collisionManager.addObserver(this);
        collisionManager.add(cube);
        collisionManager.add(room);
        collisionManager.add(puzzles);
        collisionManager.add(endTower);

        actionManager = new ActionManager();
        actionManager.addPuzzle(puzzles);
        actionManager.add(endTower);

        if (LoadGameMode.LOAD.equals(loadGameMode))
            loadGame();

        TimeManager.start();
    }

    public void update() {
        masterRenderer.render(skybox);
        masterRenderer.prepareCamera(camera);
        masterRenderer.render(heightMap);
        masterRenderer.render(light);
        masterRenderer.render(cube);
        masterRenderer.render(room);
        masterRenderer.render(endTower);

        masterRenderer.renderWithNormals(keys);

        masterRenderer.renderWithNormals(elka);
        masterRenderer.renderWithNormals(mech);
        masterRenderer.renderWithNormals(mini);
        masterRenderer.renderWithNormals(mel);

        for(AbstractPuzzle puzzle:puzzles) {
            if(puzzle.isCompleted() && !puzzle.wasKeySpawned()) {
                Key key = new Key(puzzle.getKeySpawnPosition(), puzzle.getKeyColor(), puzzle.getKeyGuiTexture(), entityManager.getEntityModel(R.raw.key));
                key.addObserver(this);
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
        for(GuiEntity g:guiEntities)
            g.update();

        for (Entity key : keys) {
            if(!key.isVisible())
                keys.remove(key);
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

    public void notEnoughKeysMessage(){
        notEnoughGuiEntity.setVisibleForFewSeconds(3);
    }

    public void onCollisionNotify(Entity entity) {
        entity.onCollisionNotify();
        if (entity instanceof Key) {
            guiEntities.add(((Key)entity).getGuiEntity());
            keyCollectedColors.add(((Key)entity).getColor());
            gameState.incKeysTakenCount();
        }
    }

    public boolean isAllKeyTaken() {
        return gameState.isAllKeyTaken();
    }
    public int getKeysTakenCount() {
        return gameState.getKeysTakenCount();
    }
    public void saveGame() {
        if(saverGameState != null)
            saverGameState.saveGameStateToFile(context, camera, gameState, keys, keyCollectedColors);
    }

    public void loadGame() {
        if (loaderGameState != null) {
            loaderGameState.loadGameStateFromFile(context, camera, gameState);
            for(Key key:loaderGameState.getKeys()){
                key.addObserver(this);
                keys.add(key);
                collisionManager.add(key);
                setSpawnedKeyForPuzzle(key);
            }

            for (Float collectedColorKey : loaderGameState.getCollectedColorKeys()) {

                for(AbstractPuzzle puzzle:puzzles){
                    if (puzzle.getKeyColor() == collectedColorKey.intValue()) {
                        puzzle.setIsCompleted(true);
                        puzzle.setWasKeySpawned(true);
                        puzzle.setInFinalStage();
                        GuiEntity keyGuiEntity = new GuiEntity(puzzle.getKeyGuiTexture(), new Vector2f(-0.9f+0.18f*getKeysTakenCount(), 0.9f), new Vector2f(0.08f, 0.08f));
                        keyGuiEntity.setIsVisible(true);
                        gameState.incKeysTakenCount();
                        guiEntities.add(keyGuiEntity);
                        keyCollectedColors.add(collectedColorKey.intValue());
                        break;
                    }
                }
            }
        }
    }

    private void setSpawnedKeyForPuzzle(Key key) {
        for(AbstractPuzzle puzzle:puzzles){
            if(puzzle.getKeyColor() == key.getColor()){
                puzzle.setWasKeySpawned(true);
                puzzle.setIsCompleted(true);
                puzzle.setInFinalStage();
                return;
            }
        }
    }
}
