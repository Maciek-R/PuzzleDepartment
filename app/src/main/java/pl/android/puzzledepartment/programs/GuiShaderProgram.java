package pl.android.puzzledepartment.programs;

import android.content.Context;

import pl.android.puzzledepartment.R;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by Maciek Ruszczyk on 2017-10-28.
 */

public class GuiShaderProgram extends ShaderProgram{
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;

    private final int aPositionLocation;

    public GuiShaderProgram(Context context) {
        super(context, R.raw.gui_vertex_shader, R.raw.gui_fragment_shader);

        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
    }
    @Override
    public void setUniforms(float[] matrix, int textureId){
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);
        glUniform1i(uTextureUnitLocation, 0);
    }

    @Override
    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
}