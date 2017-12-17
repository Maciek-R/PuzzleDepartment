package pl.android.puzzledepartment.objects;

import android.graphics.Color;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.data.IntegerIndexBuffer;
import pl.android.puzzledepartment.data.VertexBuffer;
import pl.android.puzzledepartment.gui.GuiEntity;
import pl.android.puzzledepartment.managers.GameState;
import pl.android.puzzledepartment.programs.ShaderProgram;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector2f;
import pl.android.puzzledepartment.util.geometry.Vector3f;

import static android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_INT;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glDrawElements;
import static pl.android.puzzledepartment.util.Constants.BYTES_PER_FLOAT;

/**
 * Created by Maciek Ruszczyk on 2017-12-08.
 */

public class Key extends Entity {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int NORMAL_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private final VertexBuffer vertexBuffer;
    private final IntegerIndexBuffer intIndexBuffer;
    private final int indicesLength;

    private final int guiTextureId;
    private GuiEntity guiEntity;

    public Key(Point pos, int color, int guiTextureId, EntityModel entityModel) {
        this(pos, color, entityModel, guiTextureId, new Vector3f(1f, 1f, 1f));
    }

    public Key(Point pos, int color, EntityModel entityModel, int guiTextureId, Vector3f scale) {
        super(pos, 0.0f, new Vector3f(0.5f*scale.x, 0.5f*scale.y, 0.5f*scale.z));
        this.color = color;
        this.guiTextureId = guiTextureId;

        vertexBuffer = entityModel.getNormalVertexBuffer();
        intIndexBuffer = entityModel.getIntIndexBuffer();
        indicesLength = entityModel.indicesArray.length;
    }

    public void update(){
        rotate(30.0f);
    }

    public void onCollisionNotify() {
        //GuiEntity keyGuiEntity = new GuiEntity(, new Vector2f(-0.9f+0.18f*camera.getKeysTakenCount(), 0.9f), new Vector2f(0.08f, 0.08f));
        setIsVisible(false);
        guiEntity = new GuiEntity(guiTextureId, new Vector2f(-0.9f+0.18f* GameState.INSTANCE.getKeysTakenCount(), 0.9f), new Vector2f(0.08f, 0.08f));
        guiEntity.setIsVisible(true);
    }

    public GuiEntity getGuiEntity() {
        return guiEntity;
    }

    @Override
    protected void initObjectProperties() {
        type = Type.UNCOLOURED;
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
