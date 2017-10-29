precision mediump float;

uniform vec3 u_CameraPos;
uniform vec3 u_LightPos;
uniform vec3 u_LightColor;

uniform float u_Damper;
uniform float u_Reflectivity;

varying vec4 v_Color;
varying vec3 v_Normal;
varying vec3 v_FragPos;

void main()
{
    vec3 toLightDir = normalize(u_LightPos - v_FragPos);
    float diff = max(dot(v_Normal, toLightDir), 0.0);
    vec3 diffuse = diff * u_LightColor;

    vec3 to_Camera_Dir = normalize(u_CameraPos - v_FragPos);
    vec3 lightDir = -toLightDir;
    vec3 reflectedLightDirection = reflect(lightDir, v_Normal);

    float specularFactor = max(dot(reflectedLightDirection, to_Camera_Dir), 0.0);
    float damperFactor = pow(specularFactor, u_Damper);
    vec3 finalSpecular = damperFactor * u_Reflectivity * u_LightColor;

    gl_FragColor = v_Color * vec4(diffuse + vec3(0.3f, 0.3f, 0.3f), 1.0) + vec4(finalSpecular, 1.0);
}