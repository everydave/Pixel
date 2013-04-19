package com.pixel.window;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Window extends JFrame implements Runnable {
	
	private Renderer renderer;

	public Window(int width, int height) {
		super("Pixel");
		setResizable(false);
		setSize(width, height);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@Override
	public void paint(Graphics g) {
		BufferedImage buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graphics = (Graphics2D) buffer.getGraphics();
		if(renderer != null) {
			renderer.post(graphics, getWidth(), getHeight());
		}
		g.drawImage(buffer, 0, 0, Color.BLACK, null);
	}
	
	public void run() {
		repaint();
	}
	
	public void renderer(Renderer renderer) {
		this.renderer = renderer;
	}
	
}
