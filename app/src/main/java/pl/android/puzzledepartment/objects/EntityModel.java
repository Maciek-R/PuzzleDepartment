package pl.android.puzzledepartment.objects;

/**
 * Created by Maciek Ruszczyk on 2017-10-15.
 */

public class EntityModel {
    final float[] verticesArray;
    final float[] normalsArray;
    final float[] texturesArray;
    final int[] indicesArray;

    public EntityModel(float[] verticesArray, float[] texturesArray, float[] normalsArray, int[] indicesArray) {
        this.verticesArray = verticesArray;
        this.texturesArray = texturesArray;
        this.normalsArray = normalsArray;
        this.indicesArray = indicesArray;
    }
}
