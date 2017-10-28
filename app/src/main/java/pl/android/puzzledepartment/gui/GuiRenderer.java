package pl.android.puzzledepartment.gui;

import java.util.List;

import pl.android.puzzledepartment.data.VertexArray;
import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.programs.ShaderProgram;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDepthMask;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Created by Maciek Ruszczyk on 2017-10-28.
 */

public class GuiRenderer {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private final float[] modelMatrix = new float[16];

    private final VertexArray vertexArray;
    private final static float vertexData[] ={
            -1f, 1f, -1f, -1f, 1f, 1f, 1f, -1f
    };
    private final ShaderProgram shaderProgram;

    public GuiRenderer(ShaderProgram shaderProgram) {
        this.vertexArray = new VertexArray(vertexData);
        this.shaderProgram = shaderProgram;
    }

    public void bindData(ShaderProgram shaderProgram) {
        vertexArray.setVertexAttribPointer(0, shaderProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, 0);
    }

    public void render(List<GuiEntity> guiEntities) {
        shaderProgram.useProgram();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);
        for (GuiEntity gui : guiEntities) {
            prepareModelMatrix(gui);
            shaderProgram.setUniforms(modelMatrix, gui.getTextureId());
            bindData(shaderProgram);
            glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexData.length/2);
        }
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
    }

    private void prepareModelMatrix(GuiEntity gui) {
        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, gui.getPosition().x, gui.getPosition().y, 0.0f);
        scaleM(modelMatrix, 0, gui.getScale().x, gui.getScale().y, 1.0f);
    }
}
