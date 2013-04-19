package com.pixel.metrics;

import java.util.HashMap;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.objdetect.CascadeClassifier;

public class Detection {

	private HashMap<Facet, CascadeClassifier> classifers = new HashMap<Facet, CascadeClassifier>();
	
	public Detection() {
		classifers.put(Facet.FACE, new CascadeClassifier("class/face.xml"));
		classifers.put(Facet.EYE , new CascadeClassifier("class/eye.xml"));
	}
	
	public MatOfRect detect(Facet facet, Mat mat) {
		MatOfRect detection = new MatOfRect();
		classifers.get(facet).detectMultiScale(mat, detection);
		return detection;
	}
	

}
