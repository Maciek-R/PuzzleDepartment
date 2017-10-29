precision mediump float;

uniform sampler2D u_BackgroundTextureUnit;
uniform sampler2D u_RedTextureUnit;
uniform sampler2D u_GreenTextureUnit;
uniform sampler2D u_BlueTextureUnit;
uniform sampler2D u_BlendMapTextureUnit;

varying vec3 v_Color;
varying vec2 v_PassTextureCoordinates;

void main()
{
    vec2 tiledTextureCoordinates = v_PassTextureCoordinates * 64.0;

    vec4 blendTextureColour = texture2D(u_BlendMapTextureUnit, v_PassTextureCoordinates);

    float restColour = 1.0f - (blendTextureColour.r + blendTextureColour.g + blendTextureColour.b);

    vec4 background = texture2D(u_BackgroundTextureUnit, tiledTextureCoordinates) * restColour;
    vec4 redTexture = texture2D(u_RedTextureUnit, tiledTextureCoordinates) * blendTextureColour.r;
    vec4 greenTexture = texture2D(u_GreenTextureUnit, tiledTextureCoordinates) * blendTextureColour.g;
    vec4 blueTexture = texture2D(u_BlueTextureUnit, tiledTextureCoordinates) * blendTextureColour.b;

    vec4 totalColour = background + redTexture + greenTexture + blueTexture;

    gl_FragColor = totalColour;
}