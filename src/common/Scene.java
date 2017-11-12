package common;

import java.util.Collection;
import java.util.HashSet;

import lights.Light;
import lights.PointLight;

import tuples.Color3f;

public class Scene {
	private final static float ambientFactor = 0.4f;
	public final static Color3f AMBIENT = new Color3f(ambientFactor, ambientFactor, ambientFactor);
	
	public Camera cam;
	public final Color3f background;
	public final HashSet<Light> lights;
	public final HashSet<PointLight> pointLights = new HashSet<PointLight>();
	
	public Scene(Camera cam, Color3f background, Collection<Light> lights)
	{
		this.cam = cam;
		this.background = background;
		this.lights = new HashSet<Light>(lights);
		generatePointLights();
	}
	
	private void generatePointLights()
	{
		for(Light light : lights)
			if(light instanceof PointLight)
				pointLights.add((PointLight)light);
	}
}