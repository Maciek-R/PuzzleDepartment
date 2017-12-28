package pl.android.puzzledepartment.puzzles;

import android.content.Context;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.managers.TextureManager;
import pl.android.puzzledepartment.objects.EntityModel;
import pl.android.puzzledepartment.objects.HeightMap;
import pl.android.puzzledepartment.objects.Tip;
import pl.android.puzzledepartment.objects.complex_objects.DragonStatue;
import pl.android.puzzledepartment.util.geometry.Point;

/**
 * Created by Maciek Ruszczyk on 2017-11-26.
 */

public class DragonStatuePuzzle extends AbstractPuzzle{
    private List<DragonStatue> statues;

    public DragonStatuePuzzle(TextureManager textureManager, Point pos, EntityModel dragonModel, EntityModel vaseModel, HeightMap heightMap, Tip tip) {
        super(textureManager, pos, tip);
        statues = new ArrayList<DragonStatue>();
        statues.add(new DragonStatue(new Point(pos.x, heightMap.getHeight(pos.x, pos.z-3f)+0.5f, pos.z-3f), dragonModel, vaseModel));
        statues.add(new DragonStatue(new Point(pos.x, heightMap.getHeight(pos.x, pos.z-1f)+0.5f, pos.z-1f), dragonModel, vaseModel));
        statues.add(new DragonStatue(new Point(pos.x, heightMap.getHeight(pos.x, pos.z+1f)+0.5f, pos.z+1f), dragonModel, vaseModel));
        statues.add(new DragonStatue(new Point(pos.x, heightMap.getHeight(pos.x, pos.z+3f)+0.5f, pos.z+3f), dragonModel, vaseModel));
    }

    public List<DragonStatue> getStatues() {
        return statues;
    }

    @Override
    public void update() {
        if(checkStatues())
            isCompleted = true;
    }

    public boolean checkStatues() {
        return DragonStatue.Direction.RIGHT.equals(statues.get(0).getDirection()) &&
                DragonStatue.Direction.RIGHT.equals(statues.get(2).getDirection()) &&
                DragonStatue.Direction.LEFT.equals(statues.get(1).getDirection()) &&
                DragonStatue.Direction.LEFT.equals(statues.get(3).getDirection());
    }

    @Override
    public Point getKeySpawnPosition() {
        return new Point(pos.x-3f, pos.y+1f, pos.z);
    }

    @Override
    public Point getTipPosition() {
        return new Point(pos.x-3f, pos.y, pos.z);
    }

    @Override
    public int getKeyColor() {
        return Color.CYAN;
    }

    @Override
    protected int getKeyGuiTexturePath() {
        return R.drawable.cyankey;
    }

    @Override
    public void setInFinalStage() {
        statues.get(0).setDirection(DragonStatue.Direction.RIGHT);
        statues.get(1).setDirection(DragonStatue.Direction.LEFT);
        statues.get(2).setDirection(DragonStatue.Direction.RIGHT);
        statues.get(3).setDirection(DragonStatue.Direction.LEFT);
    }
}
