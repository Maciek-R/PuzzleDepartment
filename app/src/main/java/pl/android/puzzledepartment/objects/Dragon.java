package pl.android.puzzledepartment.objects;

import android.content.Context;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.data.VertexArray;
import pl.android.puzzledepartment.programs.ShaderProgram;
import pl.android.puzzledepartment.util.OBJLoader;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.GL_UNSIGNED_INT;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glDrawElements;
import static pl.android.puzzledepartment.util.Constants.BYTES_PER_FLOAT;

/**
 * Created by Maciek Ruszczyk on 2017-10-15.
 */

public class Dragon extends Entity{
    private static final int POSITION_COMPONENT_COUNT = 3;
    //private static final int COLOR_COORDINATES_COMPONENT_COUNT = 3;
    private static final int NORMAL_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private final VertexArray vertexArray;
    private final IntBuffer indexArray;
    private final int indicesLength;

    public Dragon(Point pos, EntityModel entityModel) {
        super(pos, 0.0f, new Vector3f(0.25f, 0.25f, 0.25f));

        vertexArray = entityModel.getNormalVertexArray();
        indexArray = entityModel.getIndexArray();
        indicesLength = entityModel.indicesArray.length;

       // drawList.add(() -> glDrawElements(GL_TRIANGLES, entityModel.indicesArray.length, GL_UNSIGNED_BYTE, indexArray));
    }

    @Override
    public void draw() {
        glDrawElements(GL_TRIANGLES, indicesLength, GL_UNSIGNED_INT, indexArray);
    }


    @Override
    public void bindData(ShaderProgram shaderProgram) {
        int offset = 0;
        vertexArray.setVertexAttribPointer(offset, shaderProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
        offset += POSITION_COMPONENT_COUNT;
        vertexArray.setVertexAttribPointer(offset, shaderProgram.getNormalAttributeLocation(), NORMAL_COMPONENT_COUNT, STRIDE);
    }
}
