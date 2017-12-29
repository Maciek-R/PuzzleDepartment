package pl.android.puzzledepartment.objects;

import pl.android.puzzledepartment.data.IntegerIndexBuffer;
import pl.android.puzzledepartment.data.VertexBuffer;
import pl.android.puzzledepartment.programs.ShaderProgram;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

import static android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_INT;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glDrawElements;
import static pl.android.puzzledepartment.util.Constants.BYTES_PER_FLOAT;

/**
 * Created by Maciek Ruszczyk on 2017-12-17.
 */

public class Door extends Entity{
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int NORMAL_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private final VertexBuffer vertexBuffer;
    private final IntegerIndexBuffer intIndexBuffer;
    private final int indicesLength;

    public Door(Point pos, int color, EntityModel entityModel) {
        this(pos, color, entityModel, new Vector3f(1.0f, 1.74f, 1.8f));
    }

    public Door(Point pos, int color, EntityModel entityModel, Vector3f scale) {
        super(pos, 0.0f, scale);
        this.color = color;
        verAngle-=9f;

        vertexBuffer = entityModel.getNormalVertexBuffer();
        intIndexBuffer = entityModel.getIntIndexBuffer();
        indicesLength = entityModel.getIndicesArrayLength();
    }

    public void update(){
    }

    public void onCollisionNotify() {
    }

    @Override
    protected void initObjectProperties() {
        type = Entity.Type.UNCOLOURED;
        isShining = true;
    }

    @Override
    public void bindData(ShaderProgram shaderProgram) {
        int offset = 0;
        vertexBuffer.setVertexAttribPointer(offset * BYTES_PER_FLOAT, shaderProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
        offset += POSITION_COMPONENT_COUNT;
        vertexBuffer.setVertexAttribPointer(offset * BYTES_PER_FLOAT, shaderProgram.getNormalAttributeLocation(), NORMAL_COMPONENT_COUNT, STRIDE);
    }

    @Override
    public void draw() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, intIndexBuffer.getBufferId());
        glDrawElements(GL_TRIANGLES, indicesLength, GL_UNSIGNED_INT, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }
}
