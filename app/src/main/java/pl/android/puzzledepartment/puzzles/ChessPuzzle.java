package pl.android.puzzledepartment.puzzles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.objects.ShaderCube;
import pl.android.puzzledepartment.util.geometry.Point;

/**
 * Created by Maciek Ruszczyk on 2017-11-26.
 */

public class ChessPuzzle {
    private final static int NUMBER_OF_CUBES_TO_GET = 6;
    private final static int WIDTH = 5;
    private final static int HEIGHT = 5;
    private Random random;

    private Point pos;
    private Entity chess[][];
    private List<Entity> cubes;

    private Entity nextCube;
    private List<Entity> alreadySelectedCubes;
    private boolean isCompleted = false;

    public ChessPuzzle(Point pos) {
        this.pos = pos;
        random = new Random();
        cubes = new ArrayList<Entity>();
        chess = new ShaderCube[HEIGHT][WIDTH];
        for(int i=0; i<HEIGHT; ++i) {
            for(int j=0; j<WIDTH; ++j) {
                chess[i][j] = new ShaderCube(new Point(pos.x + j, pos.y, pos.z + i));
                cubes.add(chess[i][j]);
            }
        }
        alreadySelectedCubes = new ArrayList<Entity>();
        nextCube = selectNextCube();
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

    public Entity getNextCube() {
        return nextCube;
    }

    public List<Entity> getSelectedEntities() {
        return alreadySelectedCubes;
    }
}
