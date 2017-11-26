package pl.android.puzzledepartment.puzzles;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import pl.android.puzzledepartment.objects.SimpleColorShaderCube;
import pl.android.puzzledepartment.objects.complex_objects.Lever;
import pl.android.puzzledepartment.util.geometry.Point;

/**
 * Created by Maciek Ruszczyk on 2017-11-26.
 */

public class MixColorPuzzle {

    public final static int LEVELS = 3;
    private Point pos;
    private List<SimpleColorShaderCube> cubes;
    private List<Lever> levers;

    private int colors[] = new int[9];
    private int level = 0;

    public MixColorPuzzle(Point pos) {
        cubes = new ArrayList<SimpleColorShaderCube>();
        for(int i=0; i<3; ++i)
            cubes.add(new SimpleColorShaderCube(new Point(pos.x + i, pos.y, pos.z)));

        levers = new ArrayList<Lever>();
        levers.add(new Lever(new Point(pos.x, pos.y, pos.z + 5f), cubes.get(0)));
        levers.add(new Lever(new Point(pos.x+1f, pos.y, pos.z + 5f), cubes.get(1)));

        initLevels();
        loadColors();
    }

    private void initLevels() {
        colors[0] = Color.GREEN;
        colors[1] = Color.RED;
        colors[2] = Color.YELLOW;

        colors[3] = Color.RED;
        colors[4] = Color.BLUE;
        colors[5] = Color.MAGENTA;

        colors[6] = Color.GREEN;
        colors[7] = Color.BLUE;
        colors[8] = Color.CYAN;
    }

    private void loadColors() {
        for(int i=0; i<3; ++i) {
            cubes.get(i).setColor(colors[level*3+i]);
        }
        ++level;
    }

    public List<SimpleColorShaderCube> getCubes() {
        return cubes;
    }

    public List<Lever> getLevers() {
        return levers;
    }
}
