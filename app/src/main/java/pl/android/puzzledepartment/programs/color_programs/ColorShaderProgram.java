package pl.android.puzzledepartment.programs.color_programs;

import android.content.Context;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.programs.entity_programs.EntityShaderProgram;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by Maciek Ruszczyk on 2017-10-06.
 */

public abstract class ColorShaderProgram extends EntityShaderProgram {
    private final int uMatrixLocation;
    private final int aPositionLocation;

    public ColorShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId){
        super(context, vertexShaderResourceId, fragmentShaderResourceId);

        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
    }

    public void loadMatrix(float[] matrix)
    {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }

    public abstract void loadColour(int color);

    @Override
    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
}
