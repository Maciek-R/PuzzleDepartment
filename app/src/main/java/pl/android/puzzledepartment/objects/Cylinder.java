package pl.android.puzzledepartment.objects;



import java.util.List;

import pl.android.puzzledepartment.data.VertexArray;

import pl.android.puzzledepartment.programs.ColorShaderProgram;
import pl.android.puzzledepartment.programs.ShaderProgram;
import pl.android.puzzledepartment.programs.SimpleColorShaderProgram;
import pl.android.puzzledepartment.util.geometry.Circle;
import pl.android.puzzledepartment.util.geometry.Point;


import static pl.android.puzzledepartment.Constants.BYTES_PER_FLOAT;

/**
 * Created by Maciek on 2017-10-06.
 */

public class Cylinder extends Entity{
    private static final int numberOfVertices = 7;

    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COORDINATES_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private final VertexArray vertexArray;
    private Circle bottomCircle;
    private Circle topCircle;

    public Cylinder(Circle bottomCircle, Circle topCircle){
        super(bottomCircle.center);
        ObjectBuilder.GeneratedVertexData data = ObjectBuilder.createCylinder(bottomCircle, topCircle, numberOfVertices);

        vertexArray = new VertexArray(data.vertexData);
        drawList = data.drawList;

        this.bottomCircle = bottomCircle;
        this.topCircle = topCircle;
    }

    public void bindData(SimpleColorShaderProgram simpleColorShaderProgram) {
        vertexArray.setVertexAttribPointer(0, simpleColorShaderProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
    }

    public void bindData(ShaderProgram shaderProgram) {
        vertexArray.setVertexAttribPointer(0, shaderProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT, shaderProgram.getColorAttributeLocation(), COLOR_COORDINATES_COMPONENT_COUNT, STRIDE);
    }
}
