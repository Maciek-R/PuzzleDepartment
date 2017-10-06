package pl.android.puzzledepartment.objects;

import android.opengl.GLES10;

import java.nio.ByteBuffer;

import pl.android.puzzledepartment.data.VertexArray;
import pl.android.puzzledepartment.programs.ColorShaderProgram;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glDrawElements;
import static pl.android.puzzledepartment.Constants.BYTES_PER_FLOAT;
/**
 * Created by Maciek on 2017-10-06.
 */

public class Cube {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COORDINATES_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private float posX, posY, posZ;

    private final static float[] VERTEX_DATA = {
            // Order: X, Y, Z, R, G, B
            -0.5f, 0.5f, 0.5f,           1f, 0f, 0f,
            0.5f, 0.5f, 0.5f,            0f, 1f, 0f,
            -0.5f, -0.5f, 0.5f,          0f, 0f, 1f,
            0.5f, -0.5f, 0.5f,           1f, 1f, 0f,
            -0.5f, 0.5f, -0.5f,          1f, 0f, 1f,
            0.5f, 0.5f, -0.5f,           0f, 1f, 1f,
            -0.5f, -0.5f, -0.5f,         1f, 0f, 1f,
            0.5f, -0.5f, -0.5f,          1f, 1f, 1f
    };
    private final static byte[] INDEX_DATA = {
            // Front
            1, 3, 0,
            0, 3, 2,
            // Back
            4, 6, 5,
            5, 6, 7,
            // Left
            0, 2, 4,
            4, 2, 6,
            // Right
            5, 7, 1,
            1, 7, 3,
            // Top
            0, 4, 1,
            1, 4, 5,
            // Bottom
            6, 2, 7,
            7, 2, 3
    };

    private final VertexArray vertexArray;
    private final ByteBuffer indexArray;

    public Cube(float x, float y, float z) {
        vertexArray = new VertexArray(VERTEX_DATA);
        indexArray = ByteBuffer.allocateDirect(6*6).put(INDEX_DATA);
        indexArray.position(0);

        this.posX = x;
        this.posY = y;
        this.posZ = z;
    }

    public void bindData(ColorShaderProgram colorShaderProgram) {
        vertexArray.setVertexAttribPointer(0, colorShaderProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT, colorShaderProgram.getColorAttributeLocation(), COLOR_COORDINATES_COMPONENT_COUNT, STRIDE);
    }

    public void draw() {
        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_BYTE, indexArray);
    }

    public float getX(){
        return posX;
    }
    public float getY(){
        return posY;
    }
    public float getZ(){
        return posZ;
    }
}
