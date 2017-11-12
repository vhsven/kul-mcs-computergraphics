package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.FileNotFoundException;

import javax.swing.JFrame;

import matrices.RotationYMatrix;

import shapes.Shape;
import tuples.Point3f;
import tuples.Vector3f;
import xml.SceneBuilder;

import common.Camera;

public abstract class Demo implements MouseListener, KeyListener, MouseWheelListener {

	protected JFrame frame;
	protected CgPanel panel;
	protected long startTime;
	protected Shape currentShape;
	protected SceneBuilder builder;
	protected final static boolean SHADOWS = true;
	public static int ANTI_ALIASING_FACTOR = 1; //1 | 2 | 4
	public final static int Nx = (int)Math.pow(2, 9);
	public final static int Ny = Nx;
	
	public Demo(String sdlFilePath) throws FileNotFoundException {
		startTime = System.currentTimeMillis();
		System.out.print("Loading Scene... ");
		builder = new SceneBuilder(sdlFilePath);


		panel = new CgPanel();
		panel.addMouseListener(this);
		panel.addMouseWheelListener(this);
		panel.setPreferredSize(new Dimension(Nx, Ny));
		
		frame = new JFrame("CG 2.0 - Sven Van Hove");
		frame.addKeyListener(this);
		//frame.setLayout(BorderLayout)
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		//frame.getContentPane().add(bar, BorderLayout.SOUTH);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		frame.pack();
	}
	
	public void mousePressed(MouseEvent e) { }
	public void mouseClicked(MouseEvent e)
	{
		if(e.getButton() == MouseEvent.BUTTON1) //left button
			timePerformance();
		else if(e.getButton() == MouseEvent.BUTTON2) //middle button
		{
			System.out.println("*SNAPSHOT*");
			panel.saveImage(System.currentTimeMillis() + ".png");
		}
	}
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) { }
	
	public abstract void drawPixels();
	
	public void timePerformance() {
		panel.clear(builder.scene.background.x, builder.scene.background.y, builder.scene.background.z);
		long starttime = System.currentTimeMillis();
		drawPixels();
		panel.repaint();
		panel.flush();
			
		long duration = System.currentTimeMillis() - starttime;
		System.out.println("Done! (" + duration + "ms)");
		double fps = 1000.0/(double)duration;
		System.out.println(fps + " FPS");
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		Point3f eye = builder.scene.cam.eye;;
		Vector3f right = builder.scene.cam.u;
		Vector3f up = builder.scene.cam.v;
		Vector3f dir = builder.scene.cam.w.invert();
		float multiplier = 1f;
		if(e.isControlDown()) multiplier *= 10;
		if(e.isAltDown()) multiplier *= 2;
		if(e.isShiftDown()) multiplier *= 0.1f;
		ANTI_ALIASING_FACTOR = 1;
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			builder.scene.cam = new Camera(eye.plus(right.scalarMultiplication(multiplier)), dir, up, 45);
		else if(e.getKeyCode() == KeyEvent.VK_LEFT)
			builder.scene.cam = new Camera(eye.plus(right.invert().scalarMultiplication(multiplier)), dir, up, 45);
		else if(e.getKeyCode() == KeyEvent.VK_UP)
			builder.scene.cam = new Camera(eye.plus(up.scalarMultiplication(multiplier)), dir, up, 45);
		else if(e.getKeyCode() == KeyEvent.VK_DOWN)
			builder.scene.cam = new Camera(eye.plus(up.invert().scalarMultiplication(multiplier)), dir, up, 45);
		else if(e.getKeyCode() == KeyEvent.VK_NUMPAD4)
		{
			RotationYMatrix rot = new RotationYMatrix(-15);
			rot.transformInline(eye);
			builder.scene.cam = new Camera(eye, new Point3f(0, 0, 0), new Vector3f(0, 1, 0), 45);
		}
		else if(e.getKeyCode() == KeyEvent.VK_NUMPAD6)
		{
			RotationYMatrix rot = new RotationYMatrix(15);
			rot.transformInline(eye);
			builder.scene.cam = new Camera(eye, new Point3f(0, 0, 0), new Vector3f(0, 1, 0), 45);
		}
		else if(e.getKeyCode() == KeyEvent.VK_1)
		{
			System.out.print("[AA 1x1] ");
			ANTI_ALIASING_FACTOR = 1;
		}
		else if(e.getKeyCode() == KeyEvent.VK_2)
		{
			System.out.print("[AA 2x2] ");
			ANTI_ALIASING_FACTOR = 2;
		}
		else if(e.getKeyCode() == KeyEvent.VK_4)
		{
			System.out.print("[AA 4x4] ");
			ANTI_ALIASING_FACTOR = 4;
		}
		else return;
		//System.out.println(builder.scene.cam);
		drawPixels();
		panel.flush();
		panel.repaint();
		System.out.println("Done!");
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		ANTI_ALIASING_FACTOR = 1;
		Point3f eye = builder.scene.cam.eye;
		Vector3f dir = builder.scene.cam.w.invert();
		float multiplier = 1f;
		if(e.isControlDown()) multiplier *= 10;
		if(e.isAltDown()) multiplier *= 2;
		if(e.isShiftDown()) multiplier *= 0.1f;
		dir = dir.scalarMultiplication(multiplier);
		if(e.getWheelRotation() == -1)
		{
			System.out.println("Zoom In x" + multiplier);
			builder.scene.cam = new Camera(eye.plus(dir), dir, new Vector3f(0, 1, 0), 45);
		}
		else if(e.getWheelRotation() == 1)
		{
			System.out.println("Zoom Out x" + multiplier);
			builder.scene.cam = new Camera(eye.plus(dir.invert()), dir, new Vector3f(0, 1, 0), 45);
		} else return;
		//System.out.println(builder.scene.cam);
		drawPixels();
		panel.flush();
		panel.repaint();
		System.out.println("Done!");
	}
}
