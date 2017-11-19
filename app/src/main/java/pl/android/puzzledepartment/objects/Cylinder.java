package pl.android.puzzledepartment.objects;



import pl.android.puzzledepartment.data.VertexArray;

import pl.android.puzzledepartment.programs.ShaderProgram;
import pl.android.puzzledepartment.programs.color_programs.SimpleColorShaderProgram;
import pl.android.puzzledepartment.util.geometry.Circle;
import pl.android.puzzledepartment.util.geometry.Point;


import static pl.android.puzzledepartment.util.Constants.BYTES_PER_FLOAT;

/**
 * Created by Maciek Ruszczyk on 2017-10-06.
 */

public class Cylinder extends Entity{
    private static final int numberOfVertices = 31;

    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COORDINATES_COMPONENT_COUNT = 3;
    private static final int NORMAL_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COORDINATES_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private final VertexArray vertexArray;
    private Circle bottomCircle;
    private Circle topCircle;

    public Cylinder(Point point){
        super(point);
        ObjectBuilder.GeneratedVertexData data = ObjectBuilder.createCylinder(numberOfVertices);

        vertexArray = new VertexArray(data.vertexData);
        drawList = data.drawList;


        this.bottomCircle = bottomCircle;
        this.topCircle = topCircle;
    }

    @Override
    protected void initObjectProperties() {
        type = Type.COLOURED;
        this.isShining = true;
    }

    public void bindData(SimpleColorShaderProgram simpleColorShaderProgram) {
        vertexArray.setVertexAttribPointer(0, simpleColorShaderProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
    }

    public void bindData(ShaderProgram shaderProgram) {
        int offset = 0;
        vertexArray.setVertexAttribPointer(offset, shaderProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
        offset += POSITION_COMPONENT_COUNT;
        vertexArray.setVertexAttribPointer(offset, shaderProgram.getNormalAttributeLocation(), NORMAL_COMPONENT_COUNT, STRIDE);
        offset += NORMAL_COMPONENT_COUNT;
        vertexArray.setVertexAttribPointer(offset, shaderProgram.getColorAttributeLocation(), COLOR_COORDINATES_COMPONENT_COUNT, STRIDE);
    }

    @Override
    public void draw() {
        for(ObjectBuilder.DrawCommand d:drawList)
            d.draw();
    }
}
