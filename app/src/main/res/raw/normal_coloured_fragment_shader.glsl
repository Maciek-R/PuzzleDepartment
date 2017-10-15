precision mediump float;

uniform vec3 u_LightPos;
uniform vec3 u_LightColor;

varying vec4 v_Color;
varying vec3 v_Normal;
varying vec3 v_FragPos;

void main()
{
    vec3 lightDir = normalize(u_LightPos - v_FragPos);
    float diff = max(dot(v_Normal, lightDir), 0.0);
    vec3 diffuse = diff * u_LightColor;

    gl_FragColor = v_Color * vec4(diffuse, 1.0);
}