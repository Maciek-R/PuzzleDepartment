package pl.android.puzzledepartment;

import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Maciek on 2017-10-06.
 */

public class OnTouchListener implements View.OnTouchListener {
    private final GLSurfaceView glSurfaceView;
    private final MainGameRenderer mainGameRenderer;

    private float previousMoveX, previousMoveY;
    private int indexMove = -1;
    private float previousRotateX, previousRotateY;
    private int indexRotate = -1;

    public OnTouchListener(GLSurfaceView glSurfaceView, MainGameRenderer mainGameRenderer) {
        this.glSurfaceView = glSurfaceView;
        this.mainGameRenderer = mainGameRenderer;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event == null)
            return false;

        final int pointerIndex = event.getActionIndex();
        final float normalizedX = (event.getX(pointerIndex) / (float) view.getWidth()) * 2 - 1;         // to range (-1; 1)
        final float normalizedY = -((event.getY(pointerIndex) / (float) view.getHeight()) * 2 - 1);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                actionDown(event, normalizedX, normalizedY, pointerIndex);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                actionUp(event);
                break;
            case MotionEvent.ACTION_MOVE:
                actionMove(event);
                break;
        }
        return true;
    }

    private void actionMove(MotionEvent event) {
        for (int i = 0; i < event.getPointerCount(); ++i) {
            final float deltaMoveX;
            final float deltaMoveY;
            final float deltaRotateX;
            final float deltaRotateY;
            if (event.getPointerId(i) == indexMove) {
                deltaMoveX = event.getX(i) - previousMoveX;
                deltaMoveY = event.getY(i) - previousMoveY;

                previousMoveX = event.getX(i);
                previousMoveY = event.getY(i);
                glSurfaceView.queueEvent(() ->  mainGameRenderer.handleMoveCamera(deltaMoveX, deltaMoveY));
            }
            else if (event.getPointerId(i) == indexRotate) {
                deltaRotateX = event.getX(i) - previousRotateX;
                deltaRotateY = event.getY(i) - previousRotateY;

                previousRotateX = event.getX(i);
                previousRotateY = event.getY(i);
                glSurfaceView.queueEvent(() -> mainGameRenderer.handleRotationCamera(deltaRotateX, deltaRotateY));
            }
        }
    }

    private void actionDown(MotionEvent event, final float normalizedX, final float normalizedY, final int pointerIndex) {
        if (normalizedX < 0) {
            if (indexMove == -1) {
                previousMoveX = event.getX(pointerIndex);
                previousMoveY = event.getY(pointerIndex);
                Log.v("TAP1", String.valueOf(event.getPointerId(event.getActionIndex())));
                indexMove = event.getPointerId(event.getActionIndex());
            }
        } else {
            if (indexRotate == -1) {
                previousRotateX = event.getX(pointerIndex);
                previousRotateY = event.getY(pointerIndex);
                Log.v("TAP2", String.valueOf(event.getPointerId(event.getActionIndex())));
                indexRotate = event.getPointerId(event.getActionIndex());
            }
        }
        glSurfaceView.queueEvent(() -> mainGameRenderer.handleTouchPress(normalizedX, normalizedY));
    }

    private void actionUp(MotionEvent event) {
        Log.v("UP", String.valueOf(event.getPointerId(event.getActionIndex())));
        if (event.getPointerId(event.getActionIndex()) == indexMove) {
            indexMove = -1;
        } else if (event.getPointerId(event.getActionIndex()) == indexRotate) {
            indexRotate = -1;
        }
    }
}