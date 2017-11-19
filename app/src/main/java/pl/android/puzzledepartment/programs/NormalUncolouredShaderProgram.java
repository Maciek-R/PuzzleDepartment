package pl.android.puzzledepartment.programs;

import android.content.Context;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.objects.Camera;
import pl.android.puzzledepartment.objects.Light;
import pl.android.puzzledepartment.programs.entity_programs.EntityShaderProgram;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform3f;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by Maciek Ruszczyk on 2017-10-15.
 */

public class NormalUncolouredShaderProgram extends EntityShaderProgram {

    private final int uMatrixLocation;
    private final int uModelMatrixLocation;
    private final int uIT_ModelMatrixLocation;
    private final int uColorLocation;
    private final int uCameraPos;
    private final int uLightPos;
    private final int uLightColor;
    private final int uDamper;
    private final int uReflectivity;

    private final int aPositionLocation;
    private final int aNormalLocation;

    public NormalUncolouredShaderProgram(Context context) {
        super(context, R.raw.normal_uncoloured_vertex_shader, R.raw.normal_uncoloured_fragment_shader);

        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uModelMatrixLocation = glGetUniformLocation(program, U_MODEL_MATRIX);
        uIT_ModelMatrixLocation = glGetUniformLocation(program, U_IT_MODEL_VIEW_MATRIX);
        uColorLocation = glGetUniformLocation(program, U_COLOR);
        uCameraPos = glGetUniformLocation(program, U_CAMERA_POS);
        uLightPos = glGetUniformLocation(program, U_LIGHT_POS);
        uLightColor = glGetUniformLocation(program, U_LIGHT_COLOR);
        uDamper = glGetUniformLocation(program, U_DAMPER);
        uReflectivity = glGetUniformLocation(program, U_REFLECTIVITY);


        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aNormalLocation = glGetAttribLocation(program, A_NORMAL);
    }

    /*@Override
    public void setUniforms(float[] modelMatrix, float[] invertedModelMatrix, float[] modelViewProjectionMatrix, Light light, float red, float green, float blue){
        glUniformMatrix4fv(uModelMatrixLocation, 1, false, modelMatrix, 0);
        glUniformMatrix4fv(uIT_ModelMatrixLocation, 1, false, invertedModelMatrix, 0);
        glUniformMatrix4fv(uMatrixLocation, 1, false, modelViewProjectionMatrix, 0);
        glUniform4f(uColorLocation, red, green, blue, 1.0f);
        glUniform3f(uLightPos, light.getPos().x, light.getPos().y, light.getPos().z);
        glUniform3f(uLightColor, light.getLightColor().x, light.getLightColor().y, light.getLightColor().z);
    }

    @Override
    public void setUniforms(float[] modelMatrix, float[] invertedModelMatrix, float[] modelViewProjectionMatrix, Light light, float red, float green, float blue, Camera camera, float damper, float reflectivity){
        glUniformMatrix4fv(uModelMatrixLocation, 1, false, modelMatrix, 0);
        glUniformMatrix4fv(uIT_ModelMatrixLocation, 1, false, invertedModelMatrix, 0);
        glUniformMatrix4fv(uMatrixLocation, 1, false, modelViewProjectionMatrix, 0);
        glUniform4f(uColorLocation, red, green, blue, 1.0f);
        glUniform3f(uLightPos, light.getPos().x, light.getPos().y, light.getPos().z);
        glUniform3f(uLightColor, light.getLightColor().x, light.getLightColor().y, light.getLightColor().z);
        glUniform3f(uCameraPos, camera.getPosX(), camera.getLookPosY(), camera.getPosZ());
        glUniform1f(uDamper, damper);
        glUniform1f(uReflectivity, reflectivity);
    }*/

    @Override
    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
    @Override
    public int getNormalAttributeLocation() {
        return aNormalLocation;
    }
}
