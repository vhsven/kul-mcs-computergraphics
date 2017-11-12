package lights;

import tuples.Color3f;
import tuples.Vector3f;

public class DirectionalLight extends Light {
	private Vector3f direction;
	
	public DirectionalLight(float intensity, Color3f color, Vector3f direction) {
		super(intensity, color);
		this.direction = direction;
	}

	@Override
	public String toString() {
		return "<DirectionalLight dir=" + direction.toString() + " />";
	}

	public Vector3f getDirection() {
		return direction;
	}

	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}
}
