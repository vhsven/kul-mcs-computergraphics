package shapes.materials;

import java.util.Collection;

import lights.PointLight;
import common.Scene;
import tuples.Color3f;
import tuples.Point3f;
import tuples.Vector3f;

public class DiffuseMaterial extends Material {

	public DiffuseMaterial(Color3f color) {
		super(color);
	}
	
	public DiffuseMaterial(float r, float g, float b) {
		super(r, g, b);
	}
	
	@Override
	protected Color3f getMaterialSpecificShade(Collection<PointLight> lights, Color3f baseColor, Point3f location, Vector3f normal, Vector3f view)
	{
		Color3f shadingColor = Scene.AMBIENT.clone();
		shadingColor.times(baseColor);
		
		for(PointLight light : lights)
		{
			Vector3f l = light.getPosition().minus(location); //vector points from this -> light
			float dist = l.getNorm();
			l = l.normalize();
			
			float attenuation = getAttenuationFactor(dist, 1f, 0f, 0f);
			float dotProduct = Math.max(0, normal.dotProduct(l));
			
			float intensity = light.getIntensity();
			Color3f lightColor = light.getColor();
			float r = intensity * baseColor.x * lightColor.x * dotProduct * attenuation;
			float g = intensity * baseColor.y * lightColor.y * dotProduct * attenuation;
			float b = intensity * baseColor.z * lightColor.z * dotProduct * attenuation;
			
			shadingColor.plus(r, g, b);
		}
		
		shadingColor.normalize();
		return shadingColor;
	}

	@Override
	public String toString() {
		return "Diffuse Shading: " + color;
	}
}
