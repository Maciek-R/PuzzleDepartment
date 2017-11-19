package pl.android.puzzledepartment.puzzles;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.objects.Cylinder;
import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.objects.complex_objects.Room;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector2f;
import pl.android.puzzledepartment.util.geometry.Vector3f;

/**
 * Created by Maciek Ruszczyk on 2017-10-21.
 */

public class TeleportPuzzle {
    private Point pos;
    private List<Vector2f> teleportPositions = new ArrayList<Vector2f>();
    private Vector2f lightPosition;
    private int numberOfLevels;
    private final Random random;

    private List<Entity> teleports = new ArrayList<Entity>();
    private List<Room> rooms = new ArrayList<Room>();
    private Entity correctTeleportPerLevel[];
    private int currentLevel = 0;

    public TeleportPuzzle(Point pos, Context context) {
        this.pos = pos;
        random = new Random();
        if(loadPuzzleFromFile(context, R.raw.teleportpuzzle)) {
            createScene();
            randomTeleports();
        }
    }

    private void randomTeleports() {
        correctTeleportPerLevel = new Cylinder[numberOfLevels];
        for(int i=0; i<numberOfLevels; ++i)
            correctTeleportPerLevel[i] = teleports.get(i*numberOfLevels + random.nextInt(numberOfLevels));
    }

    public void nextLevel() {
        currentLevel ++;
    }

    public void reset() {
        currentLevel = 0;
    }

    private void createScene() {
        for(int i=0; i<numberOfLevels; ++i) {
            for (Vector2f teleportPosition : teleportPositions) {
                teleports.add(new Cylinder(new Point(teleportPosition.x + pos.x, i*10 + pos.y, teleportPosition.y + pos.z)));
            }
            rooms.add(new Room(new Point(0 + pos.x, i*10 + pos.y, 0 + pos.z), 5f, 1f));
        }
    }

    private boolean loadPuzzleFromFile(Context context, int resourceId) {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;

            try {
                while (true) {
                    line = bufferedReader.readLine();
                    if(line == null)
                        break;

                    String[] currentLine = line.split(" ");
                    if (line.startsWith("p ")) {
                        Vector2f teleportPosition = new Vector2f(Float.parseFloat(currentLine[1]),
                                Float.parseFloat(currentLine[2]));
                        teleportPositions.add(teleportPosition);
                    }
                    else if(line.startsWith("lp ")){
                        lightPosition = new Vector2f(Float.parseFloat(currentLine[1]),
                                Float.parseFloat(currentLine[2]));
                    }else if(line.startsWith("levels ")){
                        numberOfLevels = Integer.parseInt(currentLine[1]);
                    }
                }
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
    }

    public List<Entity> getTeleports() {
        return teleports;
    }
    public List<Room> getRooms() {
        return rooms;
    }

    public boolean checkCorrectTeleport(Entity e) {
        if(currentLevel>=numberOfLevels)
        {
            reset();
            return false;
        }
        if (e == correctTeleportPerLevel[currentLevel]) {
                nextLevel();
            return true;
        }
        else {
            reset();
            return false;
        }
    }

    public Vector3f getPositionOnCurrentFloor() {
        return new Vector3f(0.0f + pos.x, currentLevel*10f + pos.y, 0.0f + pos.z);
    }

}
