uniform mat4 u_Matrix;
attribute vec3 a_Position;
attribute vec2 a_TextureCoordinates;

varying vec3 v_Color;
varying vec2 v_PassTextureCoordinates;

void main()
{
    v_PassTextureCoordinates = a_TextureCoordinates * 64.0;
    gl_Position = u_Matrix * vec4(a_Position, 1.0);
}