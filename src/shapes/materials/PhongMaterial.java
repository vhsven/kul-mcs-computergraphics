package shapes.materials;

import java.util.Collection;

import lights.PointLight;

import common.Scene;

import tuples.Color3f;
import tuples.Point3f;
import tuples.Vector3f;

public class PhongMaterial extends Material {
	float shininess;
	Color3f specularColor;
	
	public PhongMaterial(Color3f color, Color3f specularColor, float shininess)
	{
		super(color);
		this.specularColor = specularColor;
		this.shininess = shininess;
	}
	
	public PhongMaterial(float r, float g, float b, Color3f specularColor, float shininess) {
		super(r, g, b);
		this.specularColor = specularColor;
		this.shininess = shininess;
	}

	public PhongMaterial(Color3f color, float shininess)
	{
		super(color);
		this.specularColor = new Color3f(1f, 1f, 1f);
		this.shininess = shininess;
	}
	
	public PhongMaterial(float r, float g, float b, float shininess) {
		super(r, g, b);
		this.specularColor = new Color3f(1f, 1f, 1f);
		this.shininess = shininess;
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
			float diffuseFactor = Math.max(0, normal.dotProduct(l));
			
			Vector3f h = view.normalize().plus(l).normalize();
			float phongFactor = (float) Math.pow(Math.max(0, normal.dotProduct(h)), shininess);
			
			float intensity = light.getIntensity();
			Color3f lightColor = light.getColor();
			float r = intensity * baseColor.x * lightColor.x * diffuseFactor * attenuation + intensity * specularColor.x * lightColor.x * phongFactor;
			float g = intensity * baseColor.y * lightColor.y * diffuseFactor * attenuation + intensity * specularColor.y * lightColor.y * phongFactor;
			float b = intensity * baseColor.z * lightColor.z * diffuseFactor * attenuation + intensity * specularColor.z * lightColor.z * phongFactor;
			
			shadingColor.plus(r, g, b);
		}
		
		shadingColor.normalize();
		return shadingColor;
	}
	
	@Override
	public String toString() {
		return "Phong Shading (" + shininess + "): " + color;
	}

	public float getShininess() {
		return shininess;
	}
}
