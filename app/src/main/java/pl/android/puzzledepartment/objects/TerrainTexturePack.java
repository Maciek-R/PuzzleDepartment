package pl.android.puzzledepartment.objects;

/**
 * Created by Maciek Ruszczyk on 2017-10-29.
 */

public class TerrainTexturePack {

    private TerrainTexture backgroundTexture;
    private TerrainTexture redTexture;
    private TerrainTexture blueTexture;
    private TerrainTexture greenTexture;

    public TerrainTexturePack(TerrainTexture backgroundTexture, TerrainTexture redTexture, TerrainTexture blueTexture, TerrainTexture greenTexture) {
        this.backgroundTexture = backgroundTexture;
        this.redTexture = redTexture;
        this.blueTexture = blueTexture;
        this.greenTexture = greenTexture;
    }

    public TerrainTexture getBackgroundTexture() {
        return backgroundTexture;
    }

    public TerrainTexture getRedTexture() {
        return redTexture;
    }

    public TerrainTexture getBlueTexture() {
        return blueTexture;
    }

    public TerrainTexture getGreenTexture() {
        return greenTexture;
    }
}
