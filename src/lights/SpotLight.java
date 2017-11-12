package lights;

import tuples.Color3f;
import tuples.Point3f;
import tuples.Vector3f;

public class SpotLight extends Light {
	private Point3f position;
	private Vector3f direction;
	private float theta;

	public SpotLight(Point3f position, Vector3f direction, float theta, float intensity, Color3f color) {
		super(intensity, color);
		this.position = position;
		this.direction = direction;
		this.theta = theta;
	}

	@Override
	public String toString() {
		return "<SpotLight loc=" + position.toString() + " dir=" + direction.toString() + " theta=" + theta + " />";
	}

	public Point3f getPosition() {
		return position;
	}

	public void setPosition(Point3f position) {
		this.position = position;
	}

	public Vector3f getDirection() {
		return direction;
	}

	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}

	public float getTheta() {
		return theta;
	}

	public void setTheta(float theta) {
		this.theta = theta;
	}
	
}
