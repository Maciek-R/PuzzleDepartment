package pl.android.puzzledepartment.objects;

import java.util.ArrayList;
import java.util.List;

import pl.android.puzzledepartment.util.geometry.*;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;

/**
 * Created by Maciek Ruszczyk on 2017-10-07.
 */

public class ObjectBuilder {
    static interface DrawCommand{
        void draw();
    }
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COORDINATES_COMPONENT_COUNT = 3;
    private static final int FLOATS_PER_VERTEX = POSITION_COMPONENT_COUNT + COLOR_COORDINATES_COMPONENT_COUNT;
    private final float[] vertexData;
    private int offset;
    private final List<DrawCommand> drawList;

    static class GeneratedVertexData{
        final float[] vertexData;
        final List<DrawCommand> drawList;

        GeneratedVertexData(float[] vertexData, List<DrawCommand> drawList) {
            this.vertexData = vertexData;
            this.drawList = drawList;
        }
    }

    private ObjectBuilder(int numOfVertices) {
        vertexData = new float[numOfVertices * FLOATS_PER_VERTEX];
        drawList = new ArrayList<>();
    }
    private static int sizeOfCircleInVertices(int numPoints){
        return numPoints + 2;
    }
    private static int sizeOfWallOfCylinderInVertices(int numPoints){
        return (numPoints + 1)*2;
    }
    private GeneratedVertexData build(){
        return new GeneratedVertexData(vertexData, drawList);
    }

    static GeneratedVertexData createCylinder(Circle bottomCircle, Circle topCircle, int numPoints) {
        int size = sizeOfCircleInVertices(numPoints) * 2 + sizeOfWallOfCylinderInVertices(numPoints);

        ObjectBuilder builder = new ObjectBuilder(size);

        builder.appendCircle(bottomCircle, numPoints);
        builder.appendCircle(topCircle, numPoints);
        builder.appendWallOfCylinder(bottomCircle, topCircle, numPoints);

        return builder.build();
    }

    private void appendCircle(Circle circle, int numPoints) {
        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int numVertices = sizeOfCircleInVertices(numPoints);

        vertexData[offset++] = circle.center.x;
        vertexData[offset++] = circle.center.y;
        vertexData[offset++] = circle.center.z;

        vertexData[offset++] = 0f;
        vertexData[offset++] = 0.5f;
        vertexData[offset++] = 0.5f;

        for(int i=0; i<=numPoints; ++i){
            float angleInRadians = ((float)i / (float)numPoints) * ((float) Math.PI * 2f);

            vertexData[offset++] = circle.center.x + circle.radius * (float)Math.cos(angleInRadians);
            vertexData[offset++] = circle.center.y;
            vertexData[offset++] = circle.center.z + circle.radius * (float)Math.sin(angleInRadians);

            vertexData[offset++] = 0f;
            vertexData[offset++] = (float) (Math.cos((double)angleInRadians))/2 + 0.5f;
            vertexData[offset++] = (float) (Math.sin((double)angleInRadians))/2 + 0.5f;
        }
        drawList.add(() -> glDrawArrays(GL_TRIANGLE_FAN, startVertex, numVertices));
    }
    private void appendWallOfCylinder(Circle bottomCircle, Circle topCircle, int numPoints){
        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int numVertices = sizeOfWallOfCylinderInVertices(numPoints);
        final float yStart = bottomCircle.center.y;
        final float yEnd = topCircle.center.y;

        for(int i=0; i<=numPoints; ++i){
            float angleInRadians = ((float)i/(float)numPoints) * ((float) Math.PI*2f);

            float xBottomPosition = bottomCircle.center.x + bottomCircle.radius * (float)Math.cos(angleInRadians);
            float xTopPosition = topCircle.center.x + topCircle.radius * (float)Math.cos(angleInRadians);
            float zBottomPosition = bottomCircle.center.z + bottomCircle.radius * (float)Math.sin(angleInRadians);
            float zTopPosition = topCircle.center.z + topCircle.radius * (float)Math.sin(angleInRadians);

            vertexData[offset++] = xBottomPosition;
            vertexData[offset++] = yStart;
            vertexData[offset++] = zBottomPosition;
            vertexData[offset++] = 0f;
            vertexData[offset++] = (float) (Math.cos((double)angleInRadians))/2 + 0.5f;
            vertexData[offset++] = (float) (Math.sin((double)angleInRadians))/2 + 0.5f;

            vertexData[offset++] = xTopPosition;
            vertexData[offset++] = yEnd;
            vertexData[offset++] = zTopPosition;
            vertexData[offset++] = 0f;
            vertexData[offset++] = (float) (Math.cos((double)angleInRadians))/2 + 0.5f;
            vertexData[offset++] = (float) (Math.sin((double)angleInRadians))/2 + 0.5f;
        }
        drawList.add(() -> glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices));
    }
}
