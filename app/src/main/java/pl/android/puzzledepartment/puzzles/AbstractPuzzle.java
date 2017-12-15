package pl.android.puzzledepartment.puzzles;




import pl.android.puzzledepartment.util.geometry.Point;

/**
 * Created by Maciek Ruszczyk on 2017-12-15.
 */

public abstract class AbstractPuzzle {
    protected boolean isCompleted = false;
    protected boolean wasKeySpawned = false;

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
}
