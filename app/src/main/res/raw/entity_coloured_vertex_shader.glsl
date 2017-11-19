uniform mat4 u_Matrix;
uniform mat4 u_ModelMatrix;
uniform mat4 u_IT_ModelMatrix;
uniform vec4 u_Color;
uniform vec3 u_LightPos;
uniform vec3 u_CameraPos;
uniform float u_Type;

attribute vec4 a_Position;
attribute vec4 a_Normal;
attribute vec4 a_Color;

varying vec4 v_Color;
varying vec3 v_Normal;
varying vec3 v_ToLightDir;
varying vec3 v_ToCameraDir;

void main()
{
    vec3 worldPosition = vec3(u_ModelMatrix * a_Position);
    v_Normal = vec3(u_IT_ModelMatrix * a_Normal);
    v_ToLightDir = u_LightPos - worldPosition;
    v_ToCameraDir = u_CameraPos - worldPosition;

    gl_Position = u_Matrix * a_Position;
    v_Color = a_Color;
}
