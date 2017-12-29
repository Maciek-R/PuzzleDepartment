package pl.android.puzzledepartment.objects;

import pl.android.puzzledepartment.action.Actionable;
import pl.android.puzzledepartment.data.IntegerIndexBuffer;
import pl.android.puzzledepartment.data.VertexBuffer;
import pl.android.puzzledepartment.gui.GuiEntity;
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
 * Created by Maciek Ruszczyk on 2017-12-21.
 */

public class Tip extends Entity implements Actionable, Collisionable{
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int NORMAL_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private final VertexBuffer vertexBuffer;
    private final IntegerIndexBuffer intIndexBuffer;
    private final int indicesLength;

    private GuiEntity guiEntity;

    public Tip(int color, EntityModel entityModel, GuiEntity guiEntity) {
        this(new Point(0f, 0f, 0f), color, entityModel, new Vector3f(1f, 1f, 1f), guiEntity);
    }

    public Tip(Point pos, int color, EntityModel entityModel, GuiEntity guiEntity) {
        this(pos, color, entityModel, new Vector3f(1f, 1f, 1f), guiEntity);
    }

    public Tip(Point pos, int color, EntityModel entityModel, Vector3f scale, GuiEntity guiEntity) {
        super(pos, 0.0f, scale);
        this.color = color;
        this.guiEntity = guiEntity;

        vertexBuffer = entityModel.getNormalVertexBuffer();
        intIndexBuffer = entityModel.getIntIndexBuffer();
        indicesLength = entityModel.indicesArray.length;
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

    @Override
    public void action() {
        guiEntity.setVisibleForFewSeconds(3f);
    }

    @Override
    public Point getPosition() {
        return pos;
    }

    @Override
    public boolean isInAction() {
        return false;
    }

    @Override
    public void updateAction() {
    }
}
