package pl.android.puzzledepartment.puzzles;

import android.content.Context;
import android.graphics.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.managers.EntityManager;
import pl.android.puzzledepartment.managers.TextureManager;
import pl.android.puzzledepartment.objects.Department;
import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.objects.EntityModel;
import pl.android.puzzledepartment.objects.Tip;
import pl.android.puzzledepartment.objects.complex_objects.Room;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector2f;
import pl.android.puzzledepartment.util.geometry.Vector3f;

/**
 * Created by Maciek Ruszczyk on 2017-10-21.
 */

public class TeleportPuzzle extends AbstractPuzzle{
    private enum DepartmentType{ELKA, MINI, MECH, MEL};
    private List<Vector2f> teleportPositions = new ArrayList<Vector2f>();
    private Vector2f lightPosition;
    private int numberOfLevels;
    private final Random random;

    private List<Entity> teleports = new ArrayList<Entity>();
    private List<Room> rooms = new ArrayList<Room>();
    private List<Entity> correctTeleportPerLevel;
    private int currentLevel = 0;

    private final EntityManager entityManager;

    public TeleportPuzzle(Context context, TextureManager textureManager, Point pos, EntityManager entityManager, Tip tip) {
        super(textureManager, pos, tip);
        random = new Random();
        this.entityManager = entityManager;
        if(loadPuzzleFromFile(context, R.raw.teleportpuzzle)) {
            createScene();
        }
    }

    public void nextLevel() {
        currentLevel ++;
    }

    public void reset() {
        currentLevel = 0;
    }

    private void createScene() {
        correctTeleportPerLevel = new ArrayList<>();
        List<DepartmentType> departments = new ArrayList<>();
        departments.add(DepartmentType.ELKA);
        departments.add(DepartmentType.MECH);
        departments.add(DepartmentType.MEL);
        departments.add(DepartmentType.MINI);

        for(int i=0; i<numberOfLevels; ++i) {
            Collections.shuffle(departments);
            int l = 0;
            for (Vector2f teleportPosition : teleportPositions) {

                DepartmentType departmentType = departments.get(l);
                Entity d = getDepartment(departmentType, teleportPosition, i);
                teleports.add(d);
                if(DepartmentType.ELKA.equals(departmentType))
                    correctTeleportPerLevel.add(d);
                ++l;
            }
            rooms.add(new Room(new Point(0 + pos.x, i*10 + pos.y, 0 + pos.z), 5f, 1f));
        }
    }

    private Entity getDepartment(DepartmentType departmentType, Vector2f teleportPosition, int i) {
        int color = Color.BLUE;
        EntityModel entityModel = null;
        switch(departmentType){
            case ELKA:
                color = Color.BLUE;
                entityModel = entityManager.getDepartmentElka();
                break;
            case MINI:
                color = Color.BLUE;
                entityModel = entityManager.getDepartmentMini();
                break;
            case MECH:
                color = Color.BLUE;
                entityModel = entityManager.getDepartmentMech();
                break;
            case MEL:
                color = Color.BLUE;
                entityModel = entityManager.getDepartmentMel();
                break;
        }
        return new Department(new Point(teleportPosition.x + pos.x, i*10 + pos.y+0.5f, teleportPosition.y + pos.z), color, entityModel);
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
        if(currentLevel >= numberOfLevels - 1){
            reset();
            return false;
        }
        if (e == correctTeleportPerLevel.get(currentLevel)) {
                nextLevel();
            if(currentLevel >= numberOfLevels-1){
                isCompleted = true;
            }
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

    @Override
    public Point getKeySpawnPosition() {
        return new Point(pos.x, (numberOfLevels-1) * 10f + pos.y + 3f, pos.z);
    }

    @Override
    public Point getTipPosition() {
        return new Point(pos.x, pos.y, pos.z-2f);
    }

    @Override
    public int getKeyColor() {
        return Color.BLUE;
    }
    @Override
    protected int getKeyGuiTexturePath() {
        return R.drawable.bluekey;
    }
}
