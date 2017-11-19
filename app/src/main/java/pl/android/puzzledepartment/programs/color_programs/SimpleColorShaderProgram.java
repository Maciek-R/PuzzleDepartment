package pl.android.puzzledepartment.programs.color_programs;

import android.content.Context;
import android.graphics.Color;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.programs.entity_programs.EntityShaderProgram;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by Maciek Ruszczyk on 2017-10-06.
 */

public class SimpleColorShaderProgram extends ColorShaderProgram {
    private final int uColorLocation;

    public SimpleColorShaderProgram(Context context) {
        super(context, R.raw.simple_color_vertex_shader, R.raw.simple_color_fragment_shader);

        uColorLocation = glGetUniformLocation(program, U_COLOR);
    }

    @Override
    public void loadColour(int color)
    {
        glUniform4f(uColorLocation, Color.red(color) / 255f, Color.green(color) / 255f, Color.blue(color) / 255f, 1.0f);
    }
   /* public void setUniforms(float[] matrix, float r, float g, float b){
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        glUniform4f(uColorLocation, r, g, b, 1.0f);
    }*/
}