package pl.android.puzzledepartment.objects;

import java.nio.ByteBuffer;
import pl.android.puzzledepartment.data.VertexArray;
import pl.android.puzzledepartment.programs.ShaderProgram;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glDrawElements;
import static pl.android.puzzledepartment.util.Constants.BYTES_PER_FLOAT;
/**
 * Created by Maciek Ruszczyk on 2017-10-06.
 */

public class Cube extends Entity {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COORDINATES_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

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

    public Cube(Point pos) {
        super(pos);
        vertexArray = new VertexArray(VERTEX_DATA);
        indexArray = ByteBuffer.allocateDirect(6*6).put(INDEX_DATA);
        indexArray.position(0);

     //   drawList.add(() -> glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_BYTE, indexArray));
    }

    public Cube(Point pos, Vector3f scale) {
        super(pos, scale);
        vertexArray = new VertexArray(VERTEX_DATA);
        indexArray = ByteBuffer.allocateDirect(6*6).put(INDEX_DATA);
        indexArray.position(0);
    }

    public void bindData(ShaderProgram shaderProgram) {
        vertexArray.setVertexAttribPointer(0, shaderProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT, shaderProgram.getColorAttributeLocation(), COLOR_COORDINATES_COMPONENT_COUNT, STRIDE);
    }

    @Override
    public void draw() {
       // for(ObjectBuilder.DrawCommand d:drawList)
          //  d.draw();
        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_BYTE, indexArray);
    }
}
