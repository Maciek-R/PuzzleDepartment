package pl.android.puzzledepartment.objects;

import android.graphics.Bitmap;
import android.graphics.Color;

import pl.android.puzzledepartment.data.IndexBuffer;
import pl.android.puzzledepartment.data.VertexBuffer;
import pl.android.puzzledepartment.programs.HeightmapShaderProgram;

import static android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glDrawElements;

/**
 * Created by Maciek on 2017-10-07.
 */

public class HeightMap {
    private static final int POSITION_COMPONENT_COUNT = 3;

    private final int width;
    private final int height;
    private final int numElements;
    private final VertexBuffer vertexBuffer;
    private final IndexBuffer indexBuffer;

    public HeightMap(Bitmap bitmap) {
        width = bitmap.getWidth();
        height = bitmap.getHeight();

        if (width * height > 65536)
            throw new RuntimeException("Heightmap is too large for indexBuffer.");

        numElements = calculateNumElements();
        vertexBuffer = new VertexBuffer(loadBitmapData(bitmap));
        indexBuffer = new IndexBuffer(createIndexData());
    }

    public void bindData(HeightmapShaderProgram heightmapShaderProgram) {
        vertexBuffer.setVertexAttribPointer(0, heightmapShaderProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, 0);
    }

    public void draw() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer.getBufferId());
        glDrawElements(GL_TRIANGLES, numElements, GL_UNSIGNED_SHORT, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private short[] createIndexData() {
        final short[] indexData = new short[numElements];
        int offset = 0;

        for(int row = 0; row < height - 1; ++row) {
            for(int col = 0; col < width - 1; ++col) {
                short topLeftIndex = (short) (row*width + col);
                short topRightIndex = (short) (row*width + col + 1);
                short bottomLeftIndex = (short) ((row+1)*width + col);
                short bottomRightIndex = (short) ((row+1)*width + col + 1);

                indexData[offset++] = topLeftIndex;
                indexData[offset++] = bottomLeftIndex;
                indexData[offset++] = topRightIndex;

                indexData[offset++] = topRightIndex;
                indexData[offset++] = bottomLeftIndex;
                indexData[offset++] = bottomRightIndex;
            }
        }
        return indexData;
    }

    private int calculateNumElements() {
        return (width - 1) * (height - 1) * 6;
    }

    private float[] loadBitmapData(Bitmap bitmap) {
        final int[] pixels = new int[height * width];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        bitmap.recycle();

        final float[] heightMapVertices = new float[width * height * POSITION_COMPONENT_COUNT];
        int offset = 0;
        for(int row = 0; row < height; ++row) {
            for(int col = 0; col < width; ++col) {
                final float xPosition = ((float)col / (float)(width - 1)) - 0.5f;
                final float yPosition = (float) Color.red(pixels[row*width + col]) / (float)255;
                final float zPosition = ((float)row / (float)(height - 1)) - 0.5f;

                heightMapVertices[offset++] = xPosition;
                heightMapVertices[offset++] = yPosition;
                heightMapVertices[offset++] = zPosition;
            }
        }
        return heightMapVertices;
    }
}