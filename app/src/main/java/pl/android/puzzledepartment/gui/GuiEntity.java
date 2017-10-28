package pl.android.puzzledepartment.gui;

import pl.android.puzzledepartment.data.VertexArray;
import pl.android.puzzledepartment.util.geometry.Vector2f;

/**
 * Created by Maciek Ruszczyk on 2017-10-28.
 */

public class GuiEntity {

    private final int textureId;
    private Vector2f position;
    private Vector2f scale;

    public GuiEntity(int textureId, Vector2f position) {
        this(textureId, position, new Vector2f(1.0f, 1.0f));
    }
    public GuiEntity(int textureId) {
        this(textureId, new Vector2f(0.0f, 0.0f), new Vector2f(1.0f, 1.0f));
    }
    public GuiEntity(int textureId, Vector2f position, Vector2f scale) {
        this.textureId = textureId;
        this.position = position;
        this.scale = scale;
    }

    public int getTextureId() {
        return textureId;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }
}
