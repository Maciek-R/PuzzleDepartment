package pl.android.puzzledepartment.objects;



import android.graphics.Color;

import pl.android.puzzledepartment.data.VertexArray;
import pl.android.puzzledepartment.programs.ShaderProgram;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
import static pl.android.puzzledepartment.util.Constants.BYTES_PER_FLOAT;

/**
 * Created by Maciek Ruszczyk on 2017-11-26.
 */

public class SimpleColorShaderCube extends Entity {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int NORMAL_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private final static float[] VERTEX_DATA = {
            //Back
            0.5f,   0.5f, -0.5f,	 0.0f,  0.0f, -1.0f,
            0.5f,  -0.5f, -0.5f,	 0.0f,  0.0f, -1.0f,
            -0.5f,   0.5f, -0.5f,	 0.0f,  0.0f, -1.0f,
            -0.5f,   0.5f, -0.5f,	 0.0f,  0.0f, -1.0f,
            0.5f,  -0.5f, -0.5f,	 0.0f,  0.0f, -1.0f,
            -0.5f,  -0.5f, -0.5f,	 0.0f,  0.0f, -1.0f,
            //Front
            -0.5f,   0.5f,  0.5f,	 0.0f,  0.0f,  1.0f,
            -0.5f,  -0.5f,  0.5f,	 0.0f,  0.0f,  1.0f,
            0.5f,   0.5f,  0.5f,	 0.0f,  0.0f,  1.0f,
            0.5f,   0.5f,  0.5f,	 0.0f,  0.0f,  1.0f,
            -0.5f,  -0.5f,  0.5f,	 0.0f,  0.0f,  1.0f,
            0.5f,  -0.5f,  0.5f,     0.0f,  0.0f,  1.0f,
            //Left
            -0.5f,  0.5f, -0.5f,	-1.0f,  0.0f,  0.0f,
            -0.5f, -0.5f, -0.5f,	-1.0f,  0.0f,  0.0f,
            -0.5f,  0.5f,  0.5f,	-1.0f,  0.0f,  0.0f,
            -0.5f,  0.5f,  0.5f,	-1.0f,  0.0f,  0.0f,
            -0.5f, -0.5f, -0.5f,	-1.0f,  0.0f,  0.0f,
            -0.5f, -0.5f,  0.5f,	-1.0f,  0.0f,  0.0f,
            //Right
            0.5f,  0.5f,  0.5f,		 1.0f,  0.0f,  0.0f,
            0.5f, -0.5f,  0.5f,		 1.0f,  0.0f,  0.0f,
            0.5f,  0.5f, -0.5f,		 1.0f,  0.0f,  0.0f,
            0.5f,  0.5f, -0.5f,		 1.0f,  0.0f,  0.0f,
            0.5f, -0.5f,  0.5f,		 1.0f,  0.0f,  0.0f,
            0.5f, -0.5f, -0.5f,	     1.0f,  0.0f,  0.0f,
            //Bottom
            -0.5f,  -0.5f,  0.5f,	 0.0f, -1.0f,  0.0f,
            -0.5f,  -0.5f, -0.5f,	 0.0f, -1.0f,  0.0f,
            0.5f,  -0.5f,  0.5f,	 0.0f, -1.0f,  0.0f,
            0.5f,  -0.5f,  0.5f,	 0.0f, -1.0f,  0.0f,
            -0.5f,  -0.5f, -0.5f,	 0.0f, -1.0f,  0.0f,
            0.5f,  -0.5f, -0.5f,	 0.0f, -1.0f,  0.0f,
            //Top
            -0.5f,  0.5f, -0.5f,	 0.0f,  1.0f,  0.0f,
            -0.5f,  0.5f,  0.5f,	 0.0f,  1.0f,  0.0f,
            0.5f,  0.5f, -0.5f,	     0.0f,  1.0f,  0.0f,
            0.5f,  0.5f, -0.5f,	     0.0f,  1.0f,  0.0f,
            -0.5f,  0.5f,  0.5f,	 0.0f,  1.0f,  0.0f,
            0.5f,  0.5f,  0.5f,	     0.0f,  1.0f,  0.0f
    };

    private final VertexArray vertexArray;

    public SimpleColorShaderCube(Point pos) {
        super(pos, 0, new Vector3f(1f, 1f, 1f));
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    @Override
    protected void initObjectProperties() {
        type = Type.UNCOLOURED;
    }

    public void bindData(ShaderProgram shaderProgram) {
        int offset = 0;
        vertexArray.setVertexAttribPointer(offset, shaderProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
        offset+=POSITION_COMPONENT_COUNT;
        vertexArray.setVertexAttribPointer(offset, shaderProgram.getNormalAttributeLocation(), NORMAL_COMPONENT_COUNT, STRIDE);
    }

    @Override
    public void draw() {
        glDrawArrays(GL_TRIANGLES, 0, 36);
    }

    public void setNextColor() {
        switch(color){
            case Color.RED: color = Color.GREEN; break;
            case Color.GREEN: color = Color.BLUE; break;
            case Color.BLUE: color = Color.RED; break;
        }
    }
}
