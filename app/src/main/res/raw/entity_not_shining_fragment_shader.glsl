precision mediump float;

uniform vec3 u_CameraPos;
uniform vec3 u_LightColor;

uniform float u_Damper;
uniform float u_Reflectivity;
uniform float u_IsShining;

varying vec4 v_Color;
varying vec3 v_Normal;
varying vec3 v_ToLightDir;
varying vec3 v_ToCameraDir;

void main()
{
    vec3 unitNormal = normalize(v_Normal);
    vec3 unitToLightDir = normalize(v_ToLightDir);
    vec3 unitToCameraDir = normalize(v_ToCameraDir);

    float diffuseFactor = max(dot(unitNormal, unitToLightDir), 0.0);
    vec3 diffuse = diffuseFactor * u_LightColor;

    gl_FragColor = v_Color * vec4(diffuse + vec3(0.3f, 0.3f, 0.3f), 1.0);
}