package pl.android.puzzledepartment.puzzles;

import android.content.Context;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.objects.Cylinder;
import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.objects.ShaderCube;
import pl.android.puzzledepartment.objects.Tip;
import pl.android.puzzledepartment.util.geometry.Point;

/**
 * Created by Maciek Ruszczyk on 2017-11-26.
 */

public class ChessPuzzle extends AbstractPuzzle{
    private final static int NUMBER_OF_CUBES_TO_GET = 15;
    private final static int WIDTH = 5;
    private final static int HEIGHT = 5;
    private Random random;

    private Entity chess[][];
    private List<Entity> cubes;
    private Entity nextCube;
    private List<Entity> alreadySelectedCubes;
    private Entity teleport;
    private Entity firstCube;

    public ChessPuzzle(Context context, Point pos, Tip tip) {
        super(context, pos, tip);
        random = new Random();
        cubes = new ArrayList<Entity>();
        chess = new ShaderCube[HEIGHT][WIDTH];
        for(int i=0; i<HEIGHT; ++i) {
            for(int j=0; j<WIDTH; ++j) {
                chess[i][j] = new ShaderCube(new Point(pos.x + j, pos.y+20f, pos.z + i));
                cubes.add(chess[i][j]);
            }
        }
        teleport = new Cylinder(this.pos);
        alreadySelectedCubes = new ArrayList<Entity>();
        nextCube = selectNextCube();
        firstCube = nextCube;
    }

    public Entity selectNextCube() {
        if(nextCube!= null)
            nextCube.singleHorRotate(-90f);
        if (alreadySelectedCubes.size() >= NUMBER_OF_CUBES_TO_GET) {
            nextCube = null;
            isCompleted = true;
            return null;
        }
        int x;
        int z;
        do {
            x = random.nextInt(WIDTH);
            z = random.nextInt(HEIGHT);
            nextCube = chess[z][x];
        }while(alreadySelectedCubes.contains(nextCube));

        alreadySelectedCubes.add(nextCube);
        nextCube.singleHorRotate(90f);
        return nextCube;
    }

    public List<Entity> getEntities() {
        return cubes;
    }

    public Entity getTeleport(){
        return teleport;
    }

    public Entity getFirstCube(){
        return firstCube;
    }

    public Entity getNextCube() {
        return nextCube;
    }

    public List<Entity> getSelectedEntities() {
        return alreadySelectedCubes;
    }

    @Override
    public Point getKeySpawnPosition() {
        return new Point(firstCube.getPos().x, firstCube.getPos().y+1f, firstCube.getPos().z);
    }

    @Override
    public Point getTipPosition() {
        return pos;
    }

    @Override
    public int getKeyColor() {
        return Color.MAGENTA;
    }

    @Override
    protected int getKeyGuiTexturePath() {
        return R.drawable.magentakey;
    }
}
