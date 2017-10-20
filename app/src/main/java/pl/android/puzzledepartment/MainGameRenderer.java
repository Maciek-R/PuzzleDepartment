package pl.android.puzzledepartment;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import pl.android.puzzledepartment.managers.CollisionManager;
import pl.android.puzzledepartment.managers.TimeManager;
import pl.android.puzzledepartment.objects.Camera;
import pl.android.puzzledepartment.objects.Cube;
import pl.android.puzzledepartment.objects.Cylinder;
import pl.android.puzzledepartment.objects.Dragon;
import pl.android.puzzledepartment.objects.HeightMap;
import pl.android.puzzledepartment.objects.Light;
import pl.android.puzzledepartment.objects.ShaderCube;
import pl.android.puzzledepartment.render_engine.MasterRenderer;
import pl.android.puzzledepartment.rooms.Room;
import pl.android.puzzledepartment.util.Logger;
import pl.android.puzzledepartment.util.geometry.Circle;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;

/**
 * Created by Maciek Ruszczyk on 2017-10-06.
 */

public class MainGameRenderer implements Renderer {

    private final Context context;

    private Cube cube;
    private ShaderCube shaderCube;
    private Cylinder cylinder;
    private Dragon dragon;
    private Light light;
    private HeightMap heightMap;
    private Room room;
    private Camera camera;

    private MasterRenderer masterRenderer;
    private CollisionManager collisionManager;

    public MainGameRenderer(Context context){
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
//        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);

        cube = new Cube(new Point(-0.5f, 0.5f, -2));
        shaderCube = new ShaderCube(new Point(-0.5f, 4.5f, -2));
        cylinder = new Cylinder(new Point(0.0f, 1.0f, 0.0f));
        //light = new Light(new Point(3f, 4.5f, -2), new Vector3f(1f, 1f, 1f));
        light = new Light(new Point(2f, 2.5f, 0f), new Vector3f(1f, 1f, 1f));
        //dragon = new Dragon(new Point(-2.5f, 3.0f, -2), context);
        heightMap = new HeightMap(((BitmapDrawable)context.getResources().getDrawable(R.drawable.heightmap)).getBitmap(), new Vector3f(50f, 10f, 50f));
        room = new Room(new Point(0f, 0.5f, 10f));
        camera = new Camera();
        masterRenderer = new MasterRenderer(context, light);
        collisionManager = new CollisionManager();
        collisionManager.add(cube);
        collisionManager.add(room);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);
        masterRenderer.createProjectionMatrix(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        TimeManager.update();

        masterRenderer.prepareCamera(camera);
        masterRenderer.render(heightMap);
        masterRenderer.renderLight(light);
        masterRenderer.render(cube);
        masterRenderer.renderNormalColoured(shaderCube);
        masterRenderer.renderNormalColoured(cylinder);
        masterRenderer.render(room);
        //masterRenderer.renderNormalUnColoured(dragon);

        light.move2();
        camera.update(heightMap, collisionManager);
       // cube.rotate(0.5f);
        //shaderCube.rotate(-1.0f);
        //dragon.rotate(2.0f);
        //cylinder.rotate(1f);
    }

    public void handleMoveCamera(float deltaMoveX, float deltaMoveY) {
      //  camera.countNextPossiblePosition(deltaMoveY/32f, deltaMoveX/32f, heightMap);
     //   if(!collisionManager.checkCollision(camera))
       //     camera.move();
        camera.setDirection(deltaMoveX/1024f, deltaMoveY/1024f);
    }

    public void handleRotationCamera(float deltaRotateX, float deltaRotateY) {
        camera.rotateY(deltaRotateY / 8f);
        camera.rotateX(deltaRotateX / 8f);
    }

    public void handleTouchPress(float normalizedX, float normalizedY){
        if(Logger.ON)
            Log.v("RENDERER", "Touch Press");
    }

    public void handleJumpCamera() {
        camera.jump();
    }
}
