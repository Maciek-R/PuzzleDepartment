package pl.android.puzzledepartment.objects;

import pl.android.puzzledepartment.data.VertexArray;

/**
 * Created by Maciek Ruszczyk on 2017-10-15.
 */

public class EntityModel {
    final float[] verticesArray;
    final float[] normalsArray;
    final float[] texturesArray;
    final int[] indicesArray;

    private VertexArray vertexArray = null;

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
    public VertexArray getNormalVertexArray() {
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

}
