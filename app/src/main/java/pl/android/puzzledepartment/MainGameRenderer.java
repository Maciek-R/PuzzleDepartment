package pl.android.puzzledepartment;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import pl.android.puzzledepartment.objects.Camera;
import pl.android.puzzledepartment.objects.Cube;
import pl.android.puzzledepartment.objects.Cylinder;
import pl.android.puzzledepartment.objects.HeightMap;
import pl.android.puzzledepartment.programs.ColorShaderProgram;
import pl.android.puzzledepartment.programs.HeightmapShaderProgram;
import pl.android.puzzledepartment.programs.SimpleColorShaderProgram;
import pl.android.puzzledepartment.render_engine.EntityRenderer;
import pl.android.puzzledepartment.render_engine.HeightmapRenderer;
import pl.android.puzzledepartment.render_engine.MasterRenderer;
import pl.android.puzzledepartment.util.Logger;
import pl.android.puzzledepartment.util.MatrixHelper;
import pl.android.puzzledepartment.util.geometry.Circle;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Created by Maciek on 2017-10-06.
 */

public class MainGameRenderer implements Renderer {

    private final Context context;
    private final float[] modelMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];

    private Cube cube;
    private Cylinder cylinder;
    private HeightMap heightMap;
    private Camera camera;

    private ColorShaderProgram colorShaderProgram;
    private SimpleColorShaderProgram simpleColorShaderProgram;
    private HeightmapShaderProgram heightmapShaderProgram;

    private MasterRenderer masterRenderer;

    public MainGameRenderer(Context context){
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
//        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);


        simpleColorShaderProgram = new SimpleColorShaderProgram(context);

        cube = new Cube(new Point(-0.5f, 0.5f, -2));
        cylinder = new Cylinder(new Circle(new Point(0f,0.5f,0f), 1f), new Circle(new Point(0f,2f,0f), 0.5f));
        heightMap = new HeightMap(((BitmapDrawable)context.getResources().getDrawable(R.drawable.heightmap)).getBitmap(), new Vector(50f, 10f, 50f));
        camera = new Camera();
        masterRenderer = new MasterRenderer(context);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);
        masterRenderer.createProjectionMatrix(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        masterRenderer.prepareCamera(camera);
        masterRenderer.render(heightMap);
        masterRenderer.render(cube);
        masterRenderer.render(cylinder);
    }

    public void handleMoveCamera(float deltaMoveX, float deltaMoveY) {
        camera.move(deltaMoveY/32f, deltaMoveX/32f);
        camera.setY(heightMap);
    }

    public void handleRotationCamera(float deltaRotateX, float deltaRotateY) {
        camera.rotateY(deltaRotateY / 8f);
        camera.rotateX(deltaRotateX / 8f);
    }

    public void handleTouchPress(float normalizedX, float normalizedY){
        if(Logger.ON)
            Log.v("RENDERER", "Touch Press");
    }
}
