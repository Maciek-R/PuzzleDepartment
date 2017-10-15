precision mediump float;

varying vec4 v_Color;
varying vec3 v_Normal;
varying vec3 v_FragPos;

void main()
{
    gl_FragColor = v_Color;
}