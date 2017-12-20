package pl.android.puzzledepartment.puzzles;




import android.content.Context;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.util.TextureHelper;
import pl.android.puzzledepartment.util.geometry.Point;

/**
 * Created by Maciek Ruszczyk on 2017-12-15.
 */

public abstract class AbstractPuzzle {
    protected Point pos;
    protected boolean isCompleted = false;
    protected boolean wasKeySpawned = false;
    protected int guiKeyTexture;

    protected AbstractPuzzle(Context context, Point pos) {
        this.pos = pos;
        guiKeyTexture = TextureHelper.loadTexture(context, getKeyGuiTexturePath());
    }
    public Point getPosition(){
        return pos;
    }
    public abstract Point getKeySpawnPosition();
    public boolean isCompleted(){
        return isCompleted;
    }
    public boolean wasKeySpawned(){
        return wasKeySpawned;
    }
    public void setWasKeySpawned(boolean wasKeySpawned) {
        this.wasKeySpawned = wasKeySpawned;
    }
    public abstract int getKeyColor();
    public void update() {
    }
    public void update(float elapsedTime){
    }
    protected abstract int getKeyGuiTexturePath();
    public int getKeyGuiTexture() {
        return guiKeyTexture;
    }
}
