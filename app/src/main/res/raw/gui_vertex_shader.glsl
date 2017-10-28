uniform mat4 u_Matrix;

attribute vec2 a_Position;

varying vec2 textureCoords;

void main()
{
    gl_Position = u_Matrix * vec4(a_Position, 0.0, 1.0);
    textureCoords = vec2((a_Position.x+1.0f)/2.0f, 1.0f - (a_Position.y+1.0f)/2.0f);
}