package main;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import lights.PointLight;
import matrices.Matrix4f;
import raytracer.IntersectRecord;
import raytracer.Ray;
import shapes.geometry.HierarchicalBoundingBoxNode;
import tuples.Color3f;
import tuples.Point3f;
import tuples.Vector3f;

//reflecties
//normBU.normalise();
//double dotDN = ray.getDirection().dotProduct(normBU);
//normBU.multiply(dotDN*2);
//reflectRay.setDirection(new Vector3f(ray.getDirection().x - normBU.x , ray.getDirection().y - normBU.y , ray.getDirection().z - normBU.z));


/**
 * DEEL I
 * ------
 * DONE het genereren van oogstralen
 * DONE het intersecteren van een straal en een driehoek
 * DONE het correct visualiseren van een scène (visibiliteit bepalen)
 * DONE diffuse shading
 * DONE de mogelijkheid om een puntlichtbron in je scène te hebben
 * DONE het implementeren van de dataformaten(obj/xml) waarmee je je scenes kan voorstellen.
 * 
 * DEEL II
 * -------
 * DONE Phong shading
 * DONE scène graph
 * DONE schaduwen
 * DONE acceleratiestructuur
 * 
 * STARS +++++
 * -----------
 * DONE +	 Anti-Aliasing
 * DONE	+	 Textuur
 * 		+	 Environment mapping
 * DONE ++	 Bump mapping
 * 		++	 Constructive Solid Geometry
 * DONE	++	 Bijkomende geometrische primitieven - Disk/Sphere/Cone/Cylinder
 * 		+++	 Oppervlakte lichtbronnen en zachte schaduwen
 * 		+++	 Recursive raytracing met speculaire reflectie en refractie 
 * 		+++	 Glossy reflectie
 * 		++++ Depth of field
 * 		++++ Motion blur
 */
public class RayTracerDemo extends Demo {
	private IntersectRecord viewRecord = new IntersectRecord();
	private Ray viewRay = new Ray();
	private Ray shadowRay = new Ray();
	private Ray transformedShadowRay = new Ray();
	
	public static void main(String[] args) throws IOException {
		//"assets/scenes/cornell.sdl"
		//"assets/scenes/woodentable.sdl"
		//"assets/scenes/walnut.sdl"
		//"assets/scenes/sphere.sdl"
		//"assets/scenes/bunny.sdl"
		//"assets/scenes/teapot.sdl"
		//"assets/scenes/default.sdl"
		//"assets/scenes/table.sdl"
		//"assets/scenes/twoShapesTest.sdl"
		//"assets/scenes/axes.sdl"
		//"assets/scenes/tree.sdl"
		//"assets/scenes/planets.sdl"
		//"assets/scenes/bump.sdl"
		new RayTracerDemo("assets/scenes/teapot.sdl");
	}
	
	public RayTracerDemo(String sdlFilePath) throws IOException
	{
		super(sdlFilePath);
//		for(Shape shape : builder.getShapes())
//		{
//			System.out.println(shape);
//			System.out.println(shape.getTransform());
//			System.out.println(shape.getInverseTransform());
//		}
			
		frame.setTitle("CG Ray Tracer - Sven Van Hove");
		frame.setVisible(true);
		System.out.println("Done! (" + (System.currentTimeMillis() - startTime) + "ms)");
	}
	
	@Override
	public void drawPixels() {
		System.out.print("Ray Tracing... ");
		for(int px=0; px < Nx; px++)
			for(int py=0; py < Ny; py++)
				processPixel(viewRay, px, py, false);
	}

	private int[][] workTime = new int[Nx][Ny];
	
	private void plotWorkTime()
	{
		for(int px=0; px < Nx; px++)
			for(int py=0; py < Ny; py++)
			{
				float percent = workTime[px][py] / 20f;
				Color3f color = new Color3f(percent, 0f, 0f);
				panel.drawPixel(px, py, color, 0);
			}
	}
	private void processPixel(Ray ray, int px, int py, boolean debug) {
		if(debug) System.out.println("Calculating ray through pixel(" + px + ", " + py + ")...");
		if(ANTI_ALIASING_FACTOR == 1)
		{
			builder.scene.cam.updateViewRay(ray, px, py);
			long start = System.currentTimeMillis();
			Color3f shade = rayIntersection(ray, debug);
			workTime[px][py] = (int) (System.currentTimeMillis() - start);
			if(debug) System.out.println("Shade: " + shade + "\r\n\r\n");
			panel.drawPixel(px, py, shade, 0);
		}
		else
		{
			int px2 = px * ANTI_ALIASING_FACTOR; //sample from higher resolution
			int py2 = py * ANTI_ALIASING_FACTOR;
			ArrayList<Color3f> shades = new ArrayList<Color3f>();
			for(int x=0; x < ANTI_ALIASING_FACTOR; x++) //for each pixel: step through the neighbours
				for(int y=0; y < ANTI_ALIASING_FACTOR; y++) //and average out the calculated shadings
				{
					builder.scene.cam.updateViewRay(ray, px2 + x, py2 + y);
					Color3f shade = rayIntersection(ray, debug);
					shades.add(shade);
				}
			Color3f shade = Color3f.getAverage(shades);
			if(debug) System.out.println("Shade: " + shade + "\r\n\r\n");
			panel.drawPixel(px, py, shade, 0);
		}
	}

	private Color3f rayIntersection(Ray ray, boolean debug) {
		viewRecord.object = null; //reset intersected object
		viewRecord.shape = null;
		
		HierarchicalBoundingBoxNode rootNode = builder.getRootNode();
		if(rootNode.intersectsWith(ray, 0, Double.POSITIVE_INFINITY, viewRecord))
		{
			if(debug) System.out.println(viewRecord.toString());
			currentShape = viewRecord.shape; //last shape to have intersected with
			assert currentShape != null;
			
			Matrix4f matrix = currentShape.getTransform();
			Matrix4f inverse = currentShape.getInverseTransform();
			Matrix4f invTranpose = inverse.transpose();
			
			//transform intersection data from object- to world coordinates
			Point3f location = matrix.transform(viewRecord.location);
			Vector3f normal = invTranpose.transform(viewRecord.normal).normalize();
			
			Vector3f viewVector = builder.scene.cam.getViewVector(location);
			Collection<PointLight> 	nonBlockedLights = builder.scene.pointLights;
			
			if(debug && SHADOWS) System.out.println("Processing Shadows...");
			if(SHADOWS) nonBlockedLights = getNonBlockedLights(location, debug);
			
			return currentShape.getShade
			(
					nonBlockedLights, 
					location, 
					normal, 
					viewVector, 
					viewRecord.textureCoord
			);
		}
		else
		{
			if(debug) System.out.println("No Intersection");
			return builder.scene.background;
		}
	}
	
	private Collection<PointLight> getNonBlockedLights(Point3f location, boolean debug)
	{
		ArrayList<PointLight> result = new ArrayList<PointLight>(builder.scene.pointLights); //start with complete set
		shadowRay.eye = location;
		for(PointLight light : builder.scene.pointLights)
		{
			Vector3f l = light.getPosition().minus(location);
			shadowRay.setDirection(l); //points to light
			
			HierarchicalBoundingBoxNode rootNode = builder.getRootNode();
			if(rootNode.intersectsWith(shadowRay, 0.00001, Double.POSITIVE_INFINITY))
			{
				result.remove(light);
				if(debug)
				{
					System.out.println("Old Ray: " + shadowRay);
					System.out.println("New Ray: " + transformedShadowRay);
					System.out.println("Removing " + light.toString());
				}
			}
		}
		return result;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		if(e.getButton() == MouseEvent.BUTTON3 && e.isControlDown())
			plotWorkTime();
		
		else if(e.getButton() == MouseEvent.BUTTON3)
		{
			int px = e.getPoint().x;
			int py = panel.getHeight() - e.getPoint().y;
			Ray ray = new Ray();
			processPixel(ray, px, py, true);
			panel.repaint();
			panel.flush();
		}
	}
}
