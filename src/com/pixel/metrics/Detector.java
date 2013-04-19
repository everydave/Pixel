package com.pixel.metrics;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;

import com.pixel.Pixel;

public class Detector implements Runnable {
	
	private Mat matrix;
	private MatOfRect result;
	
	public Detector() {
		result = new MatOfRect();
	}
	
	public void post(Mat matrix) {
		if(this.matrix != null) {
			return;
		}
		synchronized(this) {
			this.matrix = matrix;
		}
	}

	@Override
	public void run() {
		while(true) {
			try {
				if(matrix != null && matrix.isContinuous()) {
					result = Pixel.getDetection().detect(Facet.FACE, matrix);
					matrix = null;
				}
				Thread.sleep(200L);
			} catch(Exception e) {
				//Pixel.getRenderer().setDebug("HERE.. " + matrix);
				e.printStackTrace();
			}
		}
	}
	
	public MatOfRect getResult() {
		return result;
	}
	
	public Rect getBestResult() {
		Rect[] results = result.toArray();
		if(results.length > 0) {
			return results[0];
		}
		return null;
	}
	
	
	

}
