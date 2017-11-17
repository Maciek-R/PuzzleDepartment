precision mediump float;

uniform samplerCube u_TextureUnit;
varying vec3 v_Position;

const float lowerLimit = 0.0f;
const float upperLimit = 0.3f;

void main()
{
    vec4 finalColour = textureCube(u_TextureUnit, v_Position);
    float fogFactor = (v_Position.y - lowerLimit) / (upperLimit - lowerLimit);
    fogFactor = clamp(fogFactor, 0.0f, 1.0f);
    gl_FragColor = mix(vec4(0.5f, 0.62f, 0.69f, 1.0), finalColour, fogFactor);
}