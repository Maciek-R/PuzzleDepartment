package pl.android.puzzledepartment.programs;

import android.content.Context;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.objects.HeightMap;
import pl.android.puzzledepartment.objects.TerrainTexturePack;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE1;
import static android.opengl.GLES20.GL_TEXTURE2;
import static android.opengl.GLES20.GL_TEXTURE3;
import static android.opengl.GLES20.GL_TEXTURE4;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by Maciek Ruszczyk on 2017-10-07.
 */

public class HeightmapShaderProgram extends ShaderProgram{
    private final int uMatrixLocation;
    private final int aPositionLocation;
    private final int aTextureCoordsLocation;
    private final int uBackgroundTextureUnitLocation;
    private final int uRedTextureUnitLocation;
    private final int uGreenTextureUnitLocation;
    private final int uBlueTextureUnitLocation;
    private final int uBlendMapTextureUnitLocation;

    public HeightmapShaderProgram(Context context) {
        super(context, R.raw.heightmap_vertex_shader, R.raw.heightmap_fragment_shader);

        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aTextureCoordsLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
        uBackgroundTextureUnitLocation = glGetUniformLocation(program, U_BACKGROUND_TEXTURE_UNIT);
        uRedTextureUnitLocation = glGetUniformLocation(program, U_RED_TEXTURE_UNIT);
        uGreenTextureUnitLocation = glGetUniformLocation(program, U_GREEN_TEXTURE_UNIT);
        uBlueTextureUnitLocation = glGetUniformLocation(program, U_BLUE_TEXTURE_UNIT);
        uBlendMapTextureUnitLocation = glGetUniformLocation(program, U_BLENDMAP_TEXTURE_UNIT);
    }

    public void setUniforms(float[] matrix, HeightMap heightMap){
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        glUniform1i(uBackgroundTextureUnitLocation, 0);
        glUniform1i(uRedTextureUnitLocation, 1);
        glUniform1i(uGreenTextureUnitLocation, 2);
        glUniform1i(uBlueTextureUnitLocation, 3);
        glUniform1i(uBlendMapTextureUnitLocation, 4);

        TerrainTexturePack terrainTexturePack = heightMap.getTerrainTexturePack();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, terrainTexturePack.getBackgroundTexture().getTextureId());
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, terrainTexturePack.getRedTexture().getTextureId());
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, terrainTexturePack.getGreenTexture().getTextureId());
        glActiveTexture(GL_TEXTURE3);
        glBindTexture(GL_TEXTURE_2D, terrainTexturePack.getBlueTexture().getTextureId());
        glActiveTexture(GL_TEXTURE4);
        glBindTexture(GL_TEXTURE_2D, heightMap.getBlendMap().getTextureId());
    }

    @Override
    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    @Override
    public int getTextureCoordsAttributeLocation() { return aTextureCoordsLocation; }
}
