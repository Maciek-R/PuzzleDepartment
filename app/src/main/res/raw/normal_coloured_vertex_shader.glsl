uniform mat4 u_Matrix;
uniform mat4 u_ModelMatrix;
uniform mat4 u_IT_ModelMatrix;
uniform vec3 u_LightPos;
uniform vec3 u_LightColor;

attribute vec4 a_Position;
attribute vec4 a_Normal;
attribute vec4 a_Color;

varying vec4 v_Color;
varying vec3 v_Normal;
varying vec3 v_FragPos;

vec3 normal;



void main()
{
    v_FragPos = vec3(u_ModelMatrix * a_Position);
    v_Normal = normalize(vec3(u_IT_ModelMatrix * a_Normal));
    gl_Position = u_Matrix * a_Position;
    v_Color = a_Color;
}
