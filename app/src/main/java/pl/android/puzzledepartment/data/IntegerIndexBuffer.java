package pl.android.puzzledepartment.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_STATIC_DRAW;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBufferData;
import static android.opengl.GLES20.glGenBuffers;
import static pl.android.puzzledepartment.util.Constants.BYTES_PER_INT;
import static pl.android.puzzledepartment.util.Constants.BYTES_PER_SHORT;

/**
 * Created by Maciek Ruszczyk on 2017-10-20.
 */

public class IntegerIndexBuffer {
    private final int bufferId;

    public IntegerIndexBuffer(int[] vertexData) {
        final int buffers[] = new int[1];
        glGenBuffers(buffers.length, buffers, 0);
        if(buffers[0] == 0)
            throw new RuntimeException("\"Can't create a new index buffer object.");
        bufferId = buffers[0];

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferId);

        IntBuffer indexArray = ByteBuffer
                .allocateDirect(vertexData.length * BYTES_PER_INT)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer()
                .put(vertexData);
        indexArray.position(0);

        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexArray.capacity() * BYTES_PER_INT, indexArray, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public int getBufferId() {
        return bufferId;
    }
}
