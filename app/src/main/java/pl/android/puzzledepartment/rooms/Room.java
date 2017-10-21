package pl.android.puzzledepartment.rooms;

import java.util.ArrayList;
import java.util.List;

import pl.android.puzzledepartment.objects.Cube;
import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

/**
 * Created by Maciek Ruszczyk on 2017-10-20.
 */

public class Room {

    private Point center;
    private static final float SPACE = 1.0f;
    private List<Entity> entities;

    public Room(Point center, float size) {
        this.center = center;
        entities = new ArrayList<Entity>();

        createWall(new Point(0.0f, -0.5f, 0.0f), new Vector3f(size*2+1f, 0.5f, size*2+1f));

        createWall(new Point(-size, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, size*2-1f));
        createWall(new Point(size, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, size*2-1f));
        createWall(new Point(0.0f, 0.0f, size), new Vector3f(size*2+1f, 1.0f, 1.0f));
        createWall(new Point(0.0f, 0.0f, -size), new Vector3f(size*2+1f, 1.0f, 1.0f));
    }

    private void createWall(Point point, Vector3f scale) {
        Cube cube = new Cube(new Point(point.x + center.x, point.y + center.y, point.z + center.z), scale);
        entities.add(cube);
    }

    public List<Entity> getEntities() {
        return entities;
    }
}
