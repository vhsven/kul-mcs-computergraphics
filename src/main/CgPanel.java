package main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;

import tuples.Color3f;


import java.io.*;


/**
 * Allows the drawing of single pixels with a given color.
 * Typical usage in render loop (assume object is called panel):
 *		panel.clear();
 *		for each pixel to be drawn:
 *			panel.drawPixel(...);
 *		panel.repaint();
 *		panel.flush();
 */
@SuppressWarnings("serial")
public class CgPanel extends JPanel  implements ComponentListener {

   /**
    * Construct a new CgPanel.
    */
   public CgPanel() {
      addComponentListener(this);
      componentResized(new ComponentEvent(this, ComponentEvent.COMPONENT_RESIZED));
   }

   /**
    * Draw a pixel at window location x,y with color r,g,b.
    * The coordinates x,y are supposed to be in the range [0...getWidth()[
    * and [0...getHeight()[ respectively.
    * The color channels r,g and b are supposed to be in the range [0...1]
    */ 
   private void drawPixel(int x, int y, float r, float g, float b, float h, boolean invertY) {
	   if(invertY)
 		  y = this.getHeight() - y;
	  int index = x+getWidth()*y;
      if (x>=0 && x<getWidth() && y>=0 && y<getHeight() && h >= zbuffer[index]) { //we look along the NEGATIVE z-axis, so keep BIGGEST z-value
    	  rgbpixels[index] = 255<<24 | (int)(255*r)<<16 | (int)(255*g)<<8 | (int)(255*b); 
    	  zbuffer[index] = h;
      }
   }
   
   public void drawPixel(int x, int y, Color3f color, float h)
   {
	   drawPixel(x, y, color.x, color.y, color.z, h, true);
   }
   
   /**
    * Clear the buffer with a black color.
    */
   public void clear() {
      //clear(0.2f, 0.2f, 0.2f);
	   clear(0f, 0f, 0f);
   }
   
   /**
    * Clear the buffer with the given color.
    */
   public void clear(float r, float g, float b) {
      int color = 255<<24 | (int)(255*r)<<16 | (int)(255*g)<<8 | (int)(255*b);
      for (int i=0; i<getWidth()*getHeight(); i++) {
         rgbpixels[i] = color;
         zbuffer[i] = -Float.MAX_VALUE;
      }
   }
   
   /**
    * Clears the screen and repaints it with a visual representation of the old depth.
    */
   public void visualizeDepth()
   {
	   float[] oldZ = zbuffer.clone();
	   float minDepth = findMinDepth(oldZ);
	   float maxDepth = findMaxDepth(oldZ);
	   //System.out.println("Min Depth: " + minDepth);
	   //System.out.println("Max Depth: " + maxDepth);
	   
	   clear();
	   for(int column=0; column < getWidth(); column++)
		   for(int row=0; row < getHeight(); row++)
		   {
			   float depth = oldZ[column+getWidth()*row];
			   if(depth == -Float.MAX_VALUE) continue; //leave unpainted
			   float intensity = (depth - maxDepth) / (minDepth - maxDepth); //[0.0-1.0]
			   drawPixel(column, row, intensity, 0f, 0f, depth, false);
		   }
   }

	private float findMaxDepth(float[] zbuffer) {
		float maxDepth = Float.MAX_VALUE;
		for(int i=0; i < getWidth()*getHeight(); i++)
		{
			if(zbuffer[i] < maxDepth && zbuffer[i] > -Float.MAX_VALUE) //-10 deeper than -1 => find smallest
				maxDepth = zbuffer[i];
		}
		return maxDepth;
	}
	
	private float findMinDepth(float[] zbuffer) {
		float minDepth = -Float.MAX_VALUE;
		for(int i=0; i < getWidth()*getHeight(); i++)
		{
			if(zbuffer[i] == -Float.MIN_VALUE)
				continue;
			if(zbuffer[i] > minDepth && zbuffer[i] < Float.MAX_VALUE) //-10 deeper than -1 => find biggest
				minDepth = zbuffer[i];
		}
		return minDepth;
	}

   /**
    * Force a redraw.
    */
   public void flush() {
      RepaintManager.currentManager(this).paintDirtyRegions();
   }

   /**
    * Save the buffer to a file.  The given file is supposed
    * to have the extension ".png".
    */
   public void saveImage(String file) {
      mis.newPixels();
      try {
         Graphics2D g2;
         BufferedImage buf = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
         g2 = buf.createGraphics();
         g2.drawImage(image,null,null);
         ImageIO.write(buf, "png", new File(file));
         System.out.println("Saving of image to " + file + " succeeded.");
      }
      catch (Exception e) {
         System.out.println("Saving of image to " + file + " failed.");
      }
   }
   
   public void paint(Graphics g) {
       update(g);
   }
   
   public void update(Graphics g) {
      mis.newPixels();
      g.drawImage(image,0,0,this);
   }
   
   
   // --------------------------------------------------------------------------------------------------
   // Implementation of ComponentListener interface
   // --------------------------------------------------------------------------------------------------
   public void componentResized(ComponentEvent e) {
      int maxIndex = getWidth()*getHeight();
      rgbpixels = new int[maxIndex];
      zbuffer = new float[maxIndex];
      for (int i=0; i<maxIndex; i++) {
         rgbpixels[i] = 255<<24;
         zbuffer[i] = -Float.MAX_VALUE;
      }
      mis = new MemoryImageSource(getWidth(), getHeight(), rgbpixels, 0, getWidth());
      mis.setAnimated(true);
      image = createImage(mis);
   }

   public void componentHidden(ComponentEvent e) { }
   public void componentMoved(ComponentEvent e) { }
   public void componentShown(ComponentEvent e) { }
   
   
      
   private int[] rgbpixels;
   private float[] zbuffer; //precision issues p175-176 -> use float instead
   private Image image;
   private MemoryImageSource mis;
}
