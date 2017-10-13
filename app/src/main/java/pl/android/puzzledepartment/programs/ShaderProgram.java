package pl.android.puzzledepartment.programs;

import android.content.Context;

import pl.android.puzzledepartment.objects.Light;
import pl.android.puzzledepartment.util.ShaderHelper;
import pl.android.puzzledepartment.util.TextResourceReader;

import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;

/**
 * Created by Maciek Ruszczyk on 2017-10-06.
 */

public class ShaderProgram {
    //Uniform
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_IT_MODEL_VIEW_MATRIX = "u_IT_ModelMatrix";

    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String U_COLOR = "u_Color";
    //Attribute
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_NORMAL = "a_Normal";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

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
    public void setUniforms(float[] invertedModelMatrix, float[] modelViewProjectionMatrix, Light light) {};
    public int getPositionAttributeLocation() {
       return -1;
    }
    public int getColorAttributeLocation() {
       return -1;
    }
    public int getNormalAttributeLocation() { return -1; }
}
