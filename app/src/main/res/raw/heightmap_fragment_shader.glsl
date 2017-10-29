precision mediump float;

uniform sampler2D u_TextureUnit;

varying vec3 v_Color;
varying vec2 v_PassTextureCoordinates;

void main()
{
    //gl_FragColor = vec4(v_Color, 1.0);
    gl_FragColor = texture2D(u_TextureUnit, v_PassTextureCoordinates);
}