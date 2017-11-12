package shapes.textures;

import tuples.TexCoord2f;
import tuples.Vector3f;

public class BumpTexture extends Texture {

	public BumpTexture(String src) {
		super(src);
	}
	
	public BumpTexture()
	{
		super("assets/textures/bump/default.png");
	}
	
	public Vector3f getNewNormal(Vector3f N, TexCoord2f texCoord)
	{
		//texture should be gray-scale (r=g=b), so we can just ignore other colors
		int px = getPx(texCoord.x);
		int px_plus_1 = px + 1;
		int px_minus_1 = px - 1;
		if(px == 0)
			px_minus_1 = getWidth()-1;
		if(px == getWidth()-1)
			px_plus_1 = 0;
		
		int py = getPy(texCoord.y);
		int py_plus_1 = py + 1;
		int py_minus_1 = py - 1;
		if(py == 0)
			py_minus_1 = getHeight()-1;
		if(py == getHeight()-1)
			py_plus_1 = 0;
		
		float cxPlus1R = getColorByPixel(px_plus_1, py).x;
		float cxMin1R = getColorByPixel(px_minus_1, py).x;
		float uGradient = cxPlus1R - cxMin1R;
		
		float cyPlus1R = getColorByPixel(px, py_plus_1).x;
		float cyMin1R = getColorByPixel(px, py_minus_1).x;
		float vGradient = cyPlus1R - cyMin1R;
		
		Vector3f t = N.generateT();
		Vector3f T = t.crossProductRight(N).normalize(); //generate tangent vector
		Vector3f B = N.crossProductRight(T); //generate bitangent vector
		
		return N.plus(T.scalarMultiplication(uGradient)).plus(B.scalarMultiplication(vGradient)).normalize();
	}
}
