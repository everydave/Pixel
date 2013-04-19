package com.pixel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.opencv.highgui.VideoCapture;

import com.pixel.metrics.Detection;
import com.pixel.metrics.Detector;
import com.pixel.window.Renderer;
import com.pixel.window.Window;

public class Pixel {

	private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(4);
	private static Logger logger = Logger.getLogger(Pixel.class.getName());
	private static Renderer  renderer;
	private static Window    window;
	private static Detection detection;
	private static VideoCapture video;
	private static Detector detector;

	public static void main(String[] args) {
		System.loadLibrary("opencv_java245");
		logger.info("Pixel");
		detector  = new Detector();
		video     = new VideoCapture(0);
		window    = new Window(800, 600);
		renderer  = new Renderer(40L);
		detection = new Detection();
		window.renderer(renderer);
		window.addKeyListener(renderer);
		pool.scheduleAtFixedRate(window, 0L, 1000L/renderer.getTargetFPS(), TimeUnit.MILLISECONDS);
		submit(detector);
		window.setVisible(true);
	}
	
	public static void submit(Runnable runnable) {
		logger.info("Submiting: " + runnable.getClass().getCanonicalName());
		pool.submit(runnable);
	}
	
	public static Detection getDetection() {
		return detection;
	}

	public static VideoCapture getVideo() {
		return video;
	}

	public static Detector getDetector() {
		return detector;
	}
	
	public static Renderer getRenderer() {
		return renderer;
	}

}
