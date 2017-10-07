package pl.android.puzzledepartment.programs;

import android.content.Context;

import pl.android.puzzledepartment.R;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by Maciek on 2017-10-06.
 */

public class SimpleColorShaderProgram extends ShaderProgram {
    private final int uMatrixLocation;
    private final int aPositionLocation;
    private final int uColorLocation;

    public SimpleColorShaderProgram(Context context) {
        super(context, R.raw.simple_color_vertex_shader, R.raw.simple_color_fragment_shader);

        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        uColorLocation = glGetUniformLocation(program, U_COLOR);
    }

    public void setUniforms(float[] matrix, float r, float g, float b){
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        glUniform4f(uColorLocation, r, g, b, 1.0f);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
}
