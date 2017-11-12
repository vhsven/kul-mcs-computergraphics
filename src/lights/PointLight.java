package lights;

import tuples.Color3f;
import tuples.Point3f;

public class PointLight extends Light {
	Point3f position;
	
	public PointLight(Point3f position, float intensity, Color3f color)
	{
		super(intensity, color);
		this.position = position;
	}
	
	@Override
	public String toString() {
		return "<PointLight loc=" + position.toString() + " />";
	}

	public Point3f getPosition() {
		return position;
	}

	public void setPosition(Point3f position) {
		this.position = position;
	}
}
