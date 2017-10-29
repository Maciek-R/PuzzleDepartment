package pl.android.puzzledepartment.programs;

import android.content.Context;

import pl.android.puzzledepartment.objects.Camera;
import pl.android.puzzledepartment.objects.Light;
import pl.android.puzzledepartment.util.ShaderHelper;
import pl.android.puzzledepartment.util.TextResourceReader;

import static android.opengl.GLES20.glUseProgram;

/**
 * Created by Maciek Ruszczyk on 2017-10-06.
 */

public class ShaderProgram {
    //Uniform
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_MODEL_MATRIX = "u_ModelMatrix";
    protected static final String U_IT_MODEL_VIEW_MATRIX = "u_IT_ModelMatrix";

    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String U_COLOR = "u_Color";
    protected static final String U_CAMERA_POS = "u_CameraPos";
    protected static final String U_LIGHT_POS = "u_LightPos";
    protected static final String U_LIGHT_COLOR = "u_LightColor";
    protected static final String U_TIME = "u_Time";
    protected static final String U_DAMPER = "u_Damper";
    protected static final String U_REFLECTIVITY = "u_Reflectivity";
    //Attribute
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_NORMAL = "a_Normal";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    protected static final String A_DIRECTION_VECTOR = "a_DirectionVector";
    protected static final String A_PARTICLE_START_TIME = "a_ParticleStartTime";
    protected final int program;

    protected ShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId) {

        program = ShaderHelper.buildProgram(TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId),
                TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId));
    }
    public void useProgram() {
        glUseProgram(program);
    }

    public void setUniforms(float[] matrix){}
    public void setUniforms(float[] matrix, float r, float g, float b){}
    public void setUniforms(float[] modelMatrix, float[] invertedModelMatrix, float[] modelViewProjectionMatrix, Light light) {}
    public void setUniforms(float[] modelMatrix, float[] invertedModelMatrix, float[] modelViewProjectionMatrix, Light light, float red, float green, float blue) {}
    public void setUniforms(float[] modelMatrix, float[] invertedModelMatrix, float[] modelViewProjectionMatrix, Light light, float red, float green, float blue, Camera camera, float damper, float reflectivity) {}
    public void setUniforms(float[] matrix, int textureId){}
    public void setUniforms(float[] viewProjectionMatrix, float elapsedTime, int textureId){}
    public int getPositionAttributeLocation() {
       return -1;
    }
    public int getColorAttributeLocation() { return -1;}
    public int getNormalAttributeLocation() { return -1; }
    public int getDirectionVectorAttributeLocation() { return -1; }
    public int getParticleStartTimeAttributeLocation() {
        return -1;
    }
}
