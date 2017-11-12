package shapes.geometry;

import raytracer.Intersectable;

public abstract class Geometry implements Intersectable {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		if(name != null)
			return name;
		
		return super.toString();
	}
}
