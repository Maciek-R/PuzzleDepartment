package pl.android.puzzledepartment;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import pl.android.puzzledepartment.objects.Cube;
import pl.android.puzzledepartment.programs.ColorShaderProgram;
import pl.android.puzzledepartment.util.MatrixHelper;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
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
    private final float[] modelViewProjectionMatrix = new float[16];

    private Cube cube;

    private ColorShaderProgram colorShaderProgram;

    public MainGameRenderer(Context context){
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
//        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);

        colorShaderProgram = new ColorShaderProgram(context);
        cube = new Cube(-0.5f, 0.5f, -2);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float)width / (float) height, 1f, 50f);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        colorShaderProgram.useProgram();

            setIdentityM(modelMatrix, 0);
            translateM(modelMatrix, 0, cube.getX(), cube.getY(), cube.getZ());
            multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelMatrix, 0);
            colorShaderProgram.setUniforms(modelViewProjectionMatrix);
            cube.bindData(colorShaderProgram);
            cube.draw();


    }
}
