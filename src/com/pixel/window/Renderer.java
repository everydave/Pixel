package com.pixel.window;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

import com.pixel.Pixel;
import com.pixel.face.FaceRec;
import com.pixel.face.MatchResult;
import com.pixel.util.Util;

public class Renderer implements KeyListener {
	
	private long fps;
	private String debug;
	private BufferedImage face;
	private BufferedImage matched;
	private List<BufferedImage> training = new ArrayList<BufferedImage>();
	
	public Renderer(long fps) {
		this.fps = fps;
		this.debug = "";
	}
	
	public void match() {
		if(face != null) {
			long start = System.currentTimeMillis();
	        /*if (args.length< 4){
	        	printError("Usage:  java FaceRec  imageName imageDir  numberOfEigenfaces threshold");
	        }
			String imgToCheck = args[0];
			String imgDir = args[1];
			String numFaces = args[2];
			String thresholdVal = args[3];*/
			
			try {
				ImageIO.write(face, "PNG", new File("./user.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			String imgToCheck ="./user.png";
			String imgDir = "./input/";
			//String imgToCheck ="/home/sajan/Pictures/PGMSETS/amber1.pgm";
			//String imgDir = "/home/sajan/Pictures/PGMSETS";
			
			//String imgToCheck ="/home/sajan/Pictures/pngimgs/probes/amber1.png";
			//String imgDir = "/home/sajan/Pictures/pngimgs/gallery";
			String numFaces = "4";
			String thresholdVal = "25";
			MatchResult r = new FaceRec().processSelections(imgToCheck,imgDir,numFaces,thresholdVal);
			if (r.getMatchSuccess()){
				debug = (imgToCheck + " matches "+r.getMatchFileName()+" at distance=" + r.getMatchDistance());
				try {
					matched = ImageIO.read(new File(r.getMatchFileName()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				debug = ("match failed:" + r.getMatchMessage());
			}
			long end = System.currentTimeMillis();
			debug = debug + ("-- time taken ="+ (end - start) / 1000.0 +" seconds");
		}
	}
	
	public void toEigen() {
		try {
			ImageIO.write(face, "PNG", new File("./input/" + System.currentTimeMillis() + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void post(Graphics2D graphics, int width, int height) {
		Mat frame = new Mat();
		Pixel.getDetector().post(frame);
		Pixel.getVideo().read(frame);
		BufferedImage stream = Util.matToBufferedImage(frame);
		Rect result = Pixel.getDetector().getBestResult();
		if(result != null) {
			BufferedImage sub = stream.getSubimage(result.x, result.y, result.width, result.height);
			face = new BufferedImage(80, 80, BufferedImage.TYPE_BYTE_GRAY);
			face.getGraphics().drawImage(sub, 0, 0, 80, 80, null);
		}
		graphics.drawImage(stream, 0, 0, width, height, null);
		graphics.setColor(Color.RED);
		graphics.drawString(debug, 50, 50);
		if(face != null) {
			graphics.drawImage(face, 20, height-20-80, null);
		}
		if(matched != null) {
			graphics.drawImage(matched, 20 + 80 + 20, height-20-80, null);
		}
		for(int i = 0; i < training.size(); i++) {
			BufferedImage image = training.get(i);
			graphics.drawImage(image, width - 20 - 80, 40 + (i * 20), 20, 20, null);
		}
	}
	
	public long getTargetFPS() {
		return fps;
	}

	public void setDebug(String debug) {
		this.debug = debug;
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_M:
			match();
			break;
		case KeyEvent.VK_P:
			toEigen();
			break;
		case KeyEvent.VK_SPACE:
			if(face != null) {
				if(training.size() < 20) {
					training.add(face);
				}
			}
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
