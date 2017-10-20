package pl.android.puzzledepartment.managers;

import android.os.SystemClock;

/**
 * Created by Maciek Ruszczyk on 2017-10-20.
 */

public class TimeManager {

    private static long lastFrameTime = SystemClock.elapsedRealtime();
    private static float deltaInSeconds;

    public static float getDeltaTimeInSeconds() {
        return deltaInSeconds;
    }

    public static void update() {
        long currentFrameTime = SystemClock.elapsedRealtime();
        deltaInSeconds = (currentFrameTime - lastFrameTime)/1000f;
        lastFrameTime = currentFrameTime;
    }
}
