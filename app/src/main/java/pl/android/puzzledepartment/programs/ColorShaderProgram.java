package pl.android.puzzledepartment.programs;

import android.content.Context;

import pl.android.puzzledepartment.R;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by Maciek on 2017-10-06.
 */

public class ColorShaderProgram extends ShaderProgram{

    private final int uMatrixLocation;
    private final int aPositionLocation;
    private final int aColorLocation;

    public ColorShaderProgram(Context context) {
        super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);

        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aColorLocation = glGetAttribLocation(program, A_COLOR);
    }
    @Override
    public void setUniforms(float[] matrix){
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }

    @Override
    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
    @Override
    public int getColorAttributeLocation() {
        return aColorLocation;
    }
}
