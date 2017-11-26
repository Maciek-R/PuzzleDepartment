package pl.android.puzzledepartment.puzzles;

import java.util.ArrayList;
import java.util.List;

import pl.android.puzzledepartment.objects.EntityModel;
import pl.android.puzzledepartment.objects.complex_objects.DragonStatue;
import pl.android.puzzledepartment.util.geometry.Point;

/**
 * Created by Maciek Ruszczyk on 2017-11-26.
 */

public class DragonStatuePuzzle {
    private Point pos;
    private List<DragonStatue> statues;

    private boolean isCompleted = false;

    public DragonStatuePuzzle(Point pos, EntityModel dragonModel) {
        statues = new ArrayList<DragonStatue>();
        statues.add(new DragonStatue(new Point(pos.x-3f, pos.y, pos.z), dragonModel));
        statues.add(new DragonStatue(new Point(pos.x-1f, pos.y, pos.z), dragonModel));
        statues.add(new DragonStatue(new Point(pos.x+1f, pos.y, pos.z), dragonModel));
        statues.add(new DragonStatue(new Point(pos.x+3f, pos.y, pos.z), dragonModel));
    }

    public List<DragonStatue> getStatues() {
        return statues;
    }

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

    public boolean isCompleted() {
        return isCompleted;
    }
}
