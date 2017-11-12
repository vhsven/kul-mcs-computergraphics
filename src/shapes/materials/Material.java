package shapes.materials;

import java.util.Collection;
import lights.PointLight;
import shapes.textures.BumpTexture;
import shapes.textures.Texture;
import tuples.Color3f;
import tuples.Point3f;
import tuples.TexCoord2f;
import tuples.Vector3f;

public abstract class Material {
	protected final Color3f color;
	
	public Material(Color3f color)
	{
		this.color = color;
	}
	
	public Material(float r, float g, float b)
	{
		this.color = new Color3f(r, g, b);
	}
	
	public Color3f getColor()
	{
		return color.clone();
	}
	
	public Color3f getShade(Collection<PointLight> lights, Point3f location, Vector3f normal, Vector3f view, TexCoord2f textureCoord, Texture texture, BumpTexture bump)
	{
		Vector3f myNormal = normal;
		if(bump != null && textureCoord != null)
			myNormal = bump.getNewNormal(normal, textureCoord);
		
		Color3f myColor = color;
		if(texture != null && textureCoord != null)
			myColor = texture.getColor(textureCoord);
		
		return getMaterialSpecificShade(lights, myColor, location, myNormal, view);
	}
	
	protected abstract Color3f getMaterialSpecificShade(Collection<PointLight> lights, Color3f baseColor, Point3f location, Vector3f normal, Vector3f view);
	
	protected static float getAttenuationFactor(float dist, float c0, float c1, float c2) {
		return Math.min(1/(c0 + dist*c1 + dist*dist*c2), 1);
	}
	
	@Override
	public abstract String toString();
}
