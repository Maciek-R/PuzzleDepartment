package pl.android.puzzledepartment.puzzles;

import android.content.Context;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.objects.EntityModel;
import pl.android.puzzledepartment.objects.SimpleColorShaderCube;
import pl.android.puzzledepartment.objects.complex_objects.Lever;
import pl.android.puzzledepartment.util.geometry.Point;

/**
 * Created by Maciek Ruszczyk on 2017-11-26.
 */

public class MixColorPuzzle extends AbstractPuzzle{

    private int LEVELS_COUNT;
    private List<SimpleColorShaderCube> cubes;
    private List<Integer> colors;
    private List<Lever> levers;

    private List<Round> rounds;
    private int currentLevel = 0;

    public MixColorPuzzle(Context context, Point pos, EntityModel leverBaseModel, EntityModel leverHandleModel) {
        super(context, pos);
        colors = new ArrayList<>();
        cubes = new ArrayList<SimpleColorShaderCube>();

        for(int i=0; i<3; ++i)
            cubes.add(new SimpleColorShaderCube(new Point(pos.x + i, pos.y, pos.z)));
        for(int i=0; i<2; ++i)
            colors.add(new Integer(0));

        levers = new ArrayList<Lever>();
        levers.add(new Lever(new Point(pos.x, pos.y, pos.z + 5f), leverBaseModel, leverHandleModel, cubes.get(0)));
        levers.add(new Lever(new Point(pos.x+1f, pos.y, pos.z + 5f), leverBaseModel, leverHandleModel, cubes.get(1)));

        initLevels();
        loadInitColors();
    }

    private void initLevels() {
        rounds = new ArrayList<>();
        rounds.add(new Round(Color.GREEN, Color.RED, Color.YELLOW));
        rounds.add(new Round(Color.RED, Color.BLUE, Color.MAGENTA));
        rounds.add(new Round(Color.GREEN, Color.BLUE, Color.CYAN));

        LEVELS_COUNT = rounds.size();
    }

    private void loadInitColors() {
        for(int i=0; i<rounds.get(currentLevel).getColorToMixCount(); ++i)
            cubes.get(i).setColor(Color.RED);

        loadNextFinalColor();
    }

    private void loadNextFinalColor() {
        int colorsCountToMix = rounds.get(currentLevel).getColorToMixCount();
        cubes.get(colorsCountToMix).setColor(rounds.get(currentLevel).getFinalColor());
    }

    private void nextLevel() {
        ++currentLevel;
        loadNextFinalColor();
    }

    public List<SimpleColorShaderCube> getCubes() {
        return cubes;
    }

    public List<Lever> getLevers() {
        return levers;
    }

    @Override
    public void update() {
        if(!checkIfColorsAreCorrect())
            return;

        if(currentLevel >= LEVELS_COUNT - 1)
            isCompleted = true;
        else
            nextLevel();
    }

    private boolean checkIfColorsAreCorrect() {
        for(int i=0; i<2; ++i)
            colors.set(i, cubes.get(i).getColor());

        return rounds.get(currentLevel).checkCorrectColors(colors);
    }

    @Override
    public Point getKeySpawnPosition() {
        return new Point(pos.x, pos.y, pos.z);
    }
    @Override
    public int getKeyColor() {
        return Color.GREEN;
    }

    @Override
    protected int getKeyGuiTexturePath() {
        return R.drawable.greenkey;
    }

    private class Round{
        List<Integer> colorsNeedToBeMixed;
        int finalColor;

        public Round(int... colors) {
            colorsNeedToBeMixed = new ArrayList<>();
            for (int c : colors)
                colorsNeedToBeMixed.add(new Integer(c));

            finalColor = colorsNeedToBeMixed.remove(colorsNeedToBeMixed.size()-1);
        }

        public boolean checkCorrectColors(List<Integer> colors) {

           /* int c0 = colorsNeedToBeMixed.get(0);
            int c1 = colorsNeedToBeMixed.get(1);

            int cc0 = colors.get(0);
            int cc1 = colors.get(1);
            boolean x = colorsNeedToBeMixed.containsAll(colors);*/
            return colorsNeedToBeMixed.containsAll(colors) && colors.containsAll(colorsNeedToBeMixed);
           // return false;
        }

        public int getFinalColor() {
            return finalColor;
        }

        public int getColorToMixCount() {
            return colorsNeedToBeMixed.size();
        }
    }
}
