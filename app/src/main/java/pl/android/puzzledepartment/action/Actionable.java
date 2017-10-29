package pl.android.puzzledepartment.action;

import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

/**
 * Created by Maciek Ruszczyk on 2017-10-28.
 */

public interface Actionable {
    public void action();
    public Point getPosition();
    public Vector3f getScale();
}