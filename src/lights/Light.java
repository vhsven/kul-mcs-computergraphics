package lights;

import tuples.Color3f;

public abstract class Light {
	protected final float intensity;
	protected final Color3f color;
	
	public Light(float intensity, Color3f color)
	{
		assert intensity >=0 && intensity <= 1;
		
		this.intensity = intensity;
		this.color = color;
		
		this.color.normalize();
	}

	public float getIntensity() {
		return intensity;
	}

	public Color3f getColor() {
		return color;
	}

	@Override
	public abstract String toString();

}
