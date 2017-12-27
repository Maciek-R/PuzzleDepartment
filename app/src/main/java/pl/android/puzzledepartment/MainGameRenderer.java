package pl.android.puzzledepartment;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import pl.android.puzzledepartment.managers.GameManager;
import pl.android.puzzledepartment.managers.TimeManager;
import pl.android.puzzledepartment.util.Logger;

import static android.opengl.GLES20.GL_BACK;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_CULL_FACE;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;

import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glCullFace;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;


/**
 * Created by Maciek Ruszczyk on 2017-10-06.
 */

public class MainGameRenderer implements Renderer {

    private final Context context;
    private GameManager gameManager;
    private LoadGameMode loadGameMode;

    public MainGameRenderer(Context context, LoadGameMode loadGameMode){
        this.context = context;
        this.loadGameMode = loadGameMode;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glEnable(GL_DEPTH_TEST);

        gameManager = new GameManager(context, loadGameMode);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);
        gameManager.createProjectionMatrix(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        TimeManager.update();
        gameManager.update();
    }

    public void handleMoveCamera(float deltaMoveX, float deltaMoveY) {
        gameManager.setCameraDirection(deltaMoveX/1024f, deltaMoveY/1024f);
    }

    public void handleRotationCamera(float deltaRotateX, float deltaRotateY) {
        gameManager.setCameraRotation(deltaRotateX / 8f, deltaRotateY / 8f);

    }

    public void handleTouchPress(float normalizedX, float normalizedY){
        if(Logger.ON)
            Log.v("RENDERER", "Touch Press: X: "+normalizedX+" Y: "+normalizedY);

        gameManager.handleTouchPress(normalizedX, normalizedY);
    }

    public void handleJumpCamera() {
        gameManager.handleJump();
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
