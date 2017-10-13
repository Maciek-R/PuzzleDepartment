package pl.android.puzzledepartment.objects;

import pl.android.puzzledepartment.programs.ShaderProgram;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

/**
 * Created by Maciek Ruszczyk on 2017-10-13.
 */

public class Light extends Cube
{
    private final Vector3f lightColor;

    public Light(Point pos, Vector3f lightColor) {
        super(pos);
        this.lightColor = lightColor;
    }
    public Vector3f getLightColor() {
        return lightColor;
    }
}
