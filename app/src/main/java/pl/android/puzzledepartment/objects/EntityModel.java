package pl.android.puzzledepartment.objects;

import java.nio.IntBuffer;

import pl.android.puzzledepartment.data.IntegerIndexBuffer;
import pl.android.puzzledepartment.data.VertexBuffer;

/**
 * Created by Maciek Ruszczyk on 2017-10-15.
 */

public class EntityModel {
    final float[] verticesArray;
    final float[] normalsArray;
    final float[] texturesArray;
    final int[] indicesArray;

    private VertexBuffer vertexBuffer = null;
    private IntegerIndexBuffer intIndexBuffer = null;

    public EntityModel(float[] verticesArray, float[] texturesArray, float[] normalsArray, int[] indicesArray) {
        this.verticesArray = verticesArray;
        this.texturesArray = texturesArray;
        this.normalsArray = normalsArray;
        this.indicesArray = indicesArray;
    }

    /**
     * Order: x, y, z, nx, ny, nz
     *
     * @return VertexArray
     */
    public VertexBuffer getNormalVertexBuffer() {
        if(vertexBuffer != null)
            return vertexBuffer;

        float[] vertexData = new float[verticesArray.length + normalsArray.length];

        int verticesOffset = 0;
        int normalsOffset = 0;
        int offset = 0;
        while (verticesOffset < verticesArray.length) {

            vertexData[offset++] = verticesArray[verticesOffset++];
            vertexData[offset++] = verticesArray[verticesOffset++];
            vertexData[offset++] = verticesArray[verticesOffset++];

            vertexData[offset++] = normalsArray[normalsOffset++];
            vertexData[offset++] = normalsArray[normalsOffset++];
            vertexData[offset++] = normalsArray[normalsOffset++];
        }
        vertexBuffer = new VertexBuffer(vertexData);
        return vertexBuffer;
    }

    public IntegerIndexBuffer getIntIndexBuffer() {
        if(intIndexBuffer!=null)
            return intIndexBuffer;

        intIndexBuffer = new IntegerIndexBuffer(indicesArray);
        return intIndexBuffer;
    }

    public int getIndicesArrayLength() {
        return indicesArray.length;
    }

    /**
     * Order: x, y, z, nx, ny, nz
     *
     * @return VertexArray
     */
    /*public VertexArray getNormalVertexArray() {
        if(vertexArray != null)
            return vertexArray;

        float[] vertexData = new float[verticesArray.length + normalsArray.length];

        int verticesOffset = 0;
        int normalsOffset = 0;
        int offset = 0;
        while (verticesOffset < verticesArray.length) {

            vertexData[offset++] = verticesArray[verticesOffset++];
            vertexData[offset++] = verticesArray[verticesOffset++];
            vertexData[offset++] = verticesArray[verticesOffset++];

            vertexData[offset++] = normalsArray[normalsOffset++];
            vertexData[offset++] = normalsArray[normalsOffset++];
            vertexData[offset++] = normalsArray[normalsOffset++];
        }
        vertexArray = new VertexArray(vertexData);
        return vertexArray;
    }



    public IntBuffer getIndexArray() {
        if(indexArray!=null)
            return indexArray;

        indexArray = IntBuffer.allocate(indicesArray.length).put(indicesArray);
        indexArray.position(0);

        return indexArray;
    }
*/
}
