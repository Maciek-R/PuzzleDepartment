package pl.android.puzzledepartment.puzzles;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.managers.TextureManager;
import pl.android.puzzledepartment.objects.Cylinder;
import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.objects.ShaderCube;
import pl.android.puzzledepartment.objects.TexturedCube;
import pl.android.puzzledepartment.objects.Tip;
import pl.android.puzzledepartment.util.geometry.Point;

/**
 * Created by Maciek Ruszczyk on 2017-11-26.
 */

public class ChessPuzzle extends AbstractPuzzle{
    private enum LayersIsoOsi{PHYSICAL, DATA, NET, TRANSPORT, SESSION, PRESENTATION, APPLICATION}

    private List<Entity> chess;
    private Entity baseCube;
    private List<Entity> allCubes;


    private List<Entity> cubes;
    private Entity nextCube;
    private int nextCubeIndex = 0;
    private List<Entity> alreadySelectedCubes;
    private Entity teleport;

    public ChessPuzzle(TextureManager textureManager, Point pos, Tip tip) {
        super(textureManager, pos, tip);
        Random random = new Random();
        cubes = new ArrayList<>();
        chess = new ArrayList<>();

        List<Point> layerPositions = Arrays.asList(new Point(pos.x + 2f, pos.y+20f, pos.z), new Point(pos.x + 2f, pos.y+20f, pos.z+2f),
                    new Point(pos.x, pos.y+20f, pos.z+2f), new Point(pos.x - 2f, pos.y+20f, pos.z+2f),
                    new Point(pos.x - 2f, pos.y+20f, pos.z), new Point(pos.x - 2f, pos.y+20f, pos.z - 2f),
                    new Point(pos.x, pos.y+20f, pos.z - 2f));
        Collections.shuffle(layerPositions);
        List<LayersIsoOsi> layers = Arrays.asList(LayersIsoOsi.values());

        baseCube = new ShaderCube(new Point(pos.x, pos.y+20f, pos.z));
        for(int i=0; i<LayersIsoOsi.values().length; ++i)
            chess.add(new TexturedCube(layerPositions.get(i), getTexture(textureManager, layers.get(i))));

        teleport = new Cylinder(this.pos);
        allCubes = new ArrayList<>();
        allCubes.addAll(chess);
        allCubes.add(baseCube);

        alreadySelectedCubes = new ArrayList<>();
        nextCube = selectNextCube();
    }

    private int getTexture(final TextureManager textureManager, LayersIsoOsi layersIsoOsi) {
        int textureId = -1;
        switch(layersIsoOsi){
            case PHYSICAL:
                textureId = R.drawable.layer_physical; break;
            case DATA:
                textureId = R.drawable.layer_data; break;
            case NET:
                textureId = R.drawable.layer_net; break;
            case TRANSPORT:
                textureId = R.drawable.layer_transport; break;
            case SESSION:
                textureId =  R.drawable.layer_session; break;
            case PRESENTATION:
                textureId = R.drawable.layer_presentation; break;
            case APPLICATION:
                textureId = R.drawable.layer_app; break;
        }
        return textureManager.getTextureId(textureId);
    }

    private Entity selectNextCube() {
        if(nextCube!= null) {
            nextCube.setPos(new Point(nextCube.getPos().x, nextCube.getPos().y-1f, nextCube.getPos().z));
            alreadySelectedCubes.add(nextCube);
        }
        if (alreadySelectedCubes.size() >= LayersIsoOsi.values().length) {
            nextCube = null;
            isCompleted = true;
            return null;
        }
        nextCube = chess.get(nextCubeIndex++);
        return nextCube;
    }

    public void checkNextCube(Entity e) {
        if(e.equals(getNextCube()))
            selectNextCube();
        else if(!alreadySelectedCubes.contains(e) && !e.equals(baseCube)){
            reset();
        }
    }

    private void reset() {
        nextCubeIndex = 0;
        nextCube = null;
        nextCube = selectNextCube();
        for(Entity e:alreadySelectedCubes)
            e.setPos((new Point(e.getPos().x, e.getPos().y+1f, e.getPos().z)));
        alreadySelectedCubes.clear();
    }

    public List<Entity> getChessCubes() {
        return chess;
    }

    public List<Entity> getEntities() {
        return cubes;
    }

    public Entity getTeleport(){
        return teleport;
    }

    public Entity getBaseCube(){
        return baseCube;
    }

    private Entity getNextCube() {
        return nextCube;
    }

    public List<Entity> getAllCubes() {
        return allCubes;
    }

    @Override
    public Point getKeySpawnPosition() {
        return new Point(baseCube.getPos().x, baseCube.getPos().y+1f, baseCube.getPos().z);
    }

    @Override
    public Point getTipPosition() {
        return new Point(pos.x+2f, pos.y+0.5f, pos.z);
    }

    @Override
    public int getKeyColor() {
        return Color.MAGENTA;
    }

    @Override
    protected int getKeyGuiTexturePath() {
        return R.drawable.magentakey;
    }

    @Override
    public void setInFinalStage() {
        for(Entity e:chess)
            e.setPos(new Point(e.getPos().x, e.getPos().y-1f, e.getPos().z));
    }
}
