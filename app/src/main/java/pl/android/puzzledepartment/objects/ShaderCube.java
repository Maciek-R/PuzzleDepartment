package pl.android.puzzledepartment.objects;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import pl.android.puzzledepartment.data.VertexArray;
import pl.android.puzzledepartment.programs.ShaderProgram;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glDrawElements;
import static pl.android.puzzledepartment.util.Constants.BYTES_PER_FLOAT;

/**
 * Created by Maciek Ruszczyk on 2017-10-13.
 */

public class ShaderCube extends Entity {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COORDINATES_COMPONENT_COUNT = 3;
    private static final int NORMAL_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COORDINATES_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    private static final int STRIDE_1 = (POSITION_COMPONENT_COUNT + COLOR_COORDINATES_COMPONENT_COUNT ) * BYTES_PER_FLOAT;
    private static final int STRIDE_2 = (NORMAL_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private final static float[] VERTEX_DATA = {
            -0.5f, -0.5f, -0.5f,	 0.0f,  0.0f, -1.0f,		1.0f, 0, 0,
            0.5f, -0.5f, -0.5f,		 0.0f,  0.0f, -1.0f,		1.0f, 0, 0,
            0.5f,  0.5f, -0.5f,		 0.0f,  0.0f, -1.0f,		1.0f, 0, 0,
            0.5f,  0.5f, -0.5f,		 0.0f,  0.0f, -1.0f,		1.0f, 0, 0,
            -0.5f,  0.5f, -0.5f,	 0.0f,  0.0f, -1.0f,		1.0f, 0, 0,
            -0.5f, -0.5f, -0.5f,	 0.0f,  0.0f, -1.0f,		1.0f, 0, 0,

            -0.5f, -0.5f,  0.5f,	 0.0f,  0.0f,  1.0f,		0, 1.0f, 0,
            0.5f, -0.5f,  0.5f,		 0.0f,  0.0f,  1.0f,		0, 1.0f, 0,
            0.5f,  0.5f,  0.5f,		 0.0f,  0.0f,  1.0f,		0, 1.0f, 0,
            0.5f,  0.5f,  0.5f,		 0.0f,  0.0f,  1.0f,		0, 1.0f, 0,
            -0.5f,  0.5f,  0.5f,	 0.0f,  0.0f,  1.0f,		0, 1.0f, 0,
            -0.5f, -0.5f,  0.5f,	 0.0f,  0.0f,  1.0f,		0, 1.0f, 0,

            -0.5f,  0.5f,  0.5f,	-1.0f,  0.0f,  0.0f,		1.0f, 1.0f, 0,
            -0.5f,  0.5f, -0.5f,	-1.0f,  0.0f,  0.0f,		1.0f, 1.0f, 0,
            -0.5f, -0.5f, -0.5f,	-1.0f,  0.0f,  0.0f,		1.0f, 1.0f, 0,
            -0.5f, -0.5f, -0.5f,	-1.0f,  0.0f,  0.0f,		1.0f, 1.0f, 0,
            -0.5f, -0.5f,  0.5f,	-1.0f,  0.0f,  0.0f,		1.0f, 1.0f, 0,
            -0.5f,  0.5f,  0.5f,	-1.0f,  0.0f,  0.0f,		1.0f, 1.0f, 0,

            0.5f,  0.5f,  0.5f,		 1.0f,  0.0f,  0.0f,		0, 1.0f, 1.0f,
            0.5f,  0.5f, -0.5f,		 1.0f,  0.0f,  0.0f,		0, 1.0f, 1.0f,
            0.5f, -0.5f, -0.5f,		 1.0f,  0.0f,  0.0f,		0, 1.0f, 1.0f,
            0.5f, -0.5f, -0.5f,		 1.0f,  0.0f,  0.0f,		0, 1.0f, 1.0f,
            0.5f, -0.5f,  0.5f,		 1.0f,  0.0f,  0.0f,		0, 1.0f, 1.0f,
            0.5f,  0.5f,  0.5f,		 1.0f,  0.0f,  0.0f,		0, 1.0f, 1.0f,

            -0.5f, -0.5f, -0.5f,	 0.0f, -1.0f,  0.0f,		0, 0, 1.0f,
            0.5f, -0.5f, -0.5f,		 0.0f, -1.0f,  0.0f,		0, 0, 1.0f,
            0.5f, -0.5f,  0.5f,		 0.0f, -1.0f,  0.0f,		0, 0, 1.0f,
            0.5f, -0.5f,  0.5f,		 0.0f, -1.0f,  0.0f,		0, 0, 1.0f,
            -0.5f, -0.5f,  0.5f,	 0.0f, -1.0f,  0.0f,		0, 0, 1.0f,
            -0.5f, -0.5f, -0.5f,	 0.0f, -1.0f,  0.0f,		0, 0, 1.0f,

            -0.5f,  0.5f, -0.5f,	 0.0f,  1.0f,  0.0f,		1.0f, 0, 1.0f,
            0.5f,  0.5f, -0.5f,		 0.0f,  1.0f,  0.0f,		1.0f, 0, 1.0f,
            0.5f,  0.5f,  0.5f,		 0.0f,  1.0f,  0.0f,		1.0f, 0, 1.0f,
            0.5f,  0.5f,  0.5f,		 0.0f,  1.0f,  0.0f,		1.0f, 0, 1.0f,
            -0.5f,  0.5f,  0.5f,	 0.0f,  1.0f,  0.0f,		1.0f, 0, 1.0f,
            -0.5f,  0.5f, -0.5f,	 0.0f,  1.0f,  0.0f,		1.0f, 0, 1.0f
    };

    private final VertexArray vertexArray;
   // private final VertexArray normalsArray;

    public ShaderCube(Point pos) {
        super(pos, 0, new Vector3f(1f, 1f, 1f));
        vertexArray = new VertexArray(VERTEX_DATA);
        //normalsArray = new VertexArray(NORMAL_DATA);

       // drawList.add(() -> glDrawArrays(GL_TRIANGLES, 0, 36));
    }

    public void bindData(ShaderProgram shaderProgram) {
        /*int offset = 0;
        vertexArray.setVertexAttribPointer(offset, shaderProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE_1);
        offset+=POSITION_COMPONENT_COUNT;
        vertexArray.setVertexAttribPointer(offset, shaderProgram.getColorAttributeLocation(), COLOR_COORDINATES_COMPONENT_COUNT, STRIDE_1);
       // offset+=NORMAL_COMPONENT_COUNT;
        normalsArray.setVertexAttribPointer(0, shaderProgram.getNormalAttributeLocation(), NORMAL_COMPONENT_COUNT, STRIDE_2);*/

        int offset = 0;
        vertexArray.setVertexAttribPointer(offset, shaderProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
        offset+=POSITION_COMPONENT_COUNT;
        vertexArray.setVertexAttribPointer(offset, shaderProgram.getNormalAttributeLocation(), NORMAL_COMPONENT_COUNT, STRIDE);
        offset+=NORMAL_COMPONENT_COUNT;
        vertexArray.setVertexAttribPointer(offset, shaderProgram.getColorAttributeLocation(), COLOR_COORDINATES_COMPONENT_COUNT, STRIDE);

    }

    @Override
    public void draw() {
        glDrawArrays(GL_TRIANGLES, 0, 36);
        //for(ObjectBuilder.DrawCommand d:drawList)
         //   d.draw();
    }
}
