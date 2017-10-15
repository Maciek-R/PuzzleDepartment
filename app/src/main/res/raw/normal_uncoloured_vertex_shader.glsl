uniform mat4 u_Matrix;
uniform mat4 u_IT_ModelMatrix;
uniform vec4 u_Color;

attribute vec4 a_Position;
attribute vec4 a_Normal;

varying vec4 v_Color;
varying vec3 v_Normal;
varying vec3 v_FragPos;

vec3 normal;

vec4 getDirectionalLighting();


void main()
{
    normal = normalize(vec3(u_IT_ModelMatrix * a_Normal));
    gl_Position = u_Matrix * a_Position;
    v_Color = getDirectionalLighting();
}

vec4 getDirectionalLighting()
{
    vec3 lightPos = vec3(-2.5f, 1.0f, -2.0f);
    vec3 u_VectorToLight = lightPos - vec3(a_Position);
    u_VectorToLight = vec3(0.0f, 0.0f, 1.0f);
    return vec4(vec3(u_Color) * 0.7 * max(dot(normal, u_VectorToLight), 0.0), 1.0);
}