precision mediump float;

varying vec4 v_Color;
varying float v_Visibility;

void main()
{
    gl_FragColor = mix(vec4(0.5f, 0.62f, 0.69f, 1.0), v_Color, v_Visibility);
}