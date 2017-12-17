package pl.android.puzzledepartment.managers;

/**
 * Created by Maciek Ruszczyk on 2017-12-17.
 */

public enum GameState {
    INSTANCE;

    private final static int NUMBER_OF_ALL_KEYS = 6;
    private int keysTakenCount = 0;

    public int getKeysTakenCount() {
        return keysTakenCount;
    }

    public void incKeysTakenCount() {
        keysTakenCount++;
    }

    public boolean isAllKeyTaken() {
        return keysTakenCount == NUMBER_OF_ALL_KEYS;
    }
}
