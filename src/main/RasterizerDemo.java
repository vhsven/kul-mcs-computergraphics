package main;

import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;

import shapes.Shape;
import shapes.geometry.IndexedTriangleSet;
import shapes.geometry.Triangle;
import shapes.materials.PhongMaterial;
import tuples.Color3f;
import tuples.Vector3f;
import matrices.Matrix4f;
import matrices.RotationMatrix;
import common.BoundingBox2D;

/**
 * DONE het rasteriseren van een driehoek
 * DONE perspectieve view transformatie
 * DONE visibiliteitsbepaling m.b.v. het Z-buffer algorithme
 * DONE diffuse shading
 * DONE de mogelijkheid om een puntlichtbron in je scène te hebben
 * DONE het implementeren van de dataformaten(obj/xml) waarmee je je scenes kan voorstellen.
 */
public class RasterizerDemo extends Demo {
	public static void main(String[] args) throws FileNotFoundException {
		new RasterizerDemo("assets/scenes/bunny.sdl");
	}
	
	public RasterizerDemo(String sdlFilePath) throws FileNotFoundException
	{
		super(sdlFilePath);
		currentShape = new Shape(new IndexedTriangleSet("assets/objects/bunny.obj"), 
								new PhongMaterial(new Color3f(0.5f, 0.5f, 0.5f), 100), 
								null, null);
		for(Triangle t : (IndexedTriangleSet)currentShape.getGeometry())
			t.setShadingColors(	currentShape.getMaterial().getShade(builder.scene.pointLights, t.vertex1, t.normal1, builder.scene.cam.getViewVector(t.vertex1), null, null, null), 
								currentShape.getMaterial().getShade(builder.scene.pointLights, t.vertex2, t.normal2, builder.scene.cam.getViewVector(t.vertex2), null, null, null), 
								currentShape.getMaterial().getShade(builder.scene.pointLights, t.vertex3, t.normal3, builder.scene.cam.getViewVector(t.vertex3), null, null, null));
		
		builder.scene.cam.getCompleteCameraTransformationMatrix().transform((IndexedTriangleSet)currentShape.getGeometry());
		
		frame.setTitle("CG Rasterizer - Sven Van Hove");
		frame.setVisible(true);
		System.out.println("Done! (" + (System.currentTimeMillis() - startTime) + "ms)");
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.print("Rasterizing... ");
		super.mouseClicked(e);
		if(e.getButton() == MouseEvent.BUTTON1 && e.isControlDown())
			specialEffect();
		else if(e.getButton() == MouseEvent.BUTTON3)
			panel.visualizeDepth();
	}

	private void specialEffect() {
		for(int i=0; i < 500; i++)
		{
			Matrix4f cam = builder.scene.cam.getCompleteCameraTransformationMatrix();
			Matrix4f invCam = cam.inv();
			RotationMatrix rotate = new RotationMatrix(new Vector3f(0, 0, 1), 18);
			Matrix4f effectMatrix = cam.multiplyRightWith(rotate).multiplyRightWith(invCam);
			effectMatrix.transform((IndexedTriangleSet)currentShape.getGeometry());
			panel.clear();
			drawPixels();
			panel.repaint();
			panel.flush();
		}
	}
	
	@Override
	public void drawPixels() {
		for(Triangle triangle : (IndexedTriangleSet)currentShape.getGeometry())
		{
			BoundingBox2D box = triangle.getBoundingBox2D();
			for(int column=box.left; column <= box.right; column++)
				for(int row=box.bottom; row <= box.top; row++)
					if(triangle.isInTriangle(column, row))
						panel.drawPixel(column, row, triangle.getColor(column, row), triangle.getDepth(column, row));
		}
	}

}
