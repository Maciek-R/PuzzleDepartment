package pl.android.puzzledepartment.objects;

import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;


/**
 * Created by Maciek Ruszczyk on 2017-12-20.
 */

public class LeverBase extends ModelEntity{

    public LeverBase(Point pos, int color, EntityModel entityModel ) {
        this(pos, color, entityModel, new Vector3f(1f, 1f, 1f));
    }

    public LeverBase(Point pos, int color, EntityModel entityModel, Vector3f scale) {
        super(pos, 90.0f, scale, entityModel);
        this.color = color;
    }

    @Override
    protected void initObjectProperties() {
        type = Entity.Type.UNCOLOURED;
        isShining = true;
    }
}
