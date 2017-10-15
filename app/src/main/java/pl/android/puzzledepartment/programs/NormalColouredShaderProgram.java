package pl.android.puzzledepartment.programs;

import android.content.Context;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.objects.Light;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by Maciek Ruszczyk on 2017-10-13.
 */

public class NormalColouredShaderProgram extends ShaderProgram {

    private final int uMatrixLocation;
    private final int uIT_ModelMatrixLocation;
    private final int aPositionLocation;
    private final int aColorLocation;
    private final int aNormalLocation;

    public NormalColouredShaderProgram(Context context) {
        super(context, R.raw.normal_coloured_vertex_shader, R.raw.normal_coloured_fragment_shader);

        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uIT_ModelMatrixLocation = glGetUniformLocation(program, U_IT_MODEL_VIEW_MATRIX);

        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aColorLocation = glGetAttribLocation(program, A_COLOR);
        aNormalLocation = glGetAttribLocation(program, A_NORMAL);
    }

    @Override
    public void setUniforms(float[] invertedModelMatrix, float[] modelViewProjectionMatrix, Light light){
        glUniformMatrix4fv(uIT_ModelMatrixLocation, 1, false, invertedModelMatrix, 0);
        glUniformMatrix4fv(uMatrixLocation, 1, false, modelViewProjectionMatrix, 0);
    }

    @Override
    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
    @Override
    public int getColorAttributeLocation() {
        return aColorLocation;
    }
    @Override
    public int getNormalAttributeLocation() {
        return aNormalLocation;
    }
}
