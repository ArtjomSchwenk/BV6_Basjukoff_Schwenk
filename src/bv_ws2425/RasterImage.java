// BV Ue6 WS2024/25 Vorgabe
//
// Copyright (C) 2024 by Klaus Jung
// All rights reserved.
// Date: 2024-09-30
 		   		   	  

package bv_ws2425;

import java.io.File;
import java.util.Arrays;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class RasterImage {
	 		   		   	  
	private static final int gray  = 0xffa0a0a0;
	    
	public int[] argb;	// pixels represented as ARGB values in scanline order
	public int width;	// image width in pixels
	public int height;	// image height in pixels
	
	public RasterImage(int width, int height) {
		// creates an empty RasterImage of given size
		this.width = width;
		this.height = height;
		argb = new int[width * height];
		Arrays.fill(argb, gray);
	}
	
	public RasterImage(RasterImage src) {
		// copy constructor
		this.width = src.width;
		this.height = src.height;
		argb = src.argb.clone();
	}
	
	public RasterImage(File file) {
		// creates an RasterImage by reading the given file
		Image image = null;
		if(file != null && file.exists()) {
			image = new Image(file.toURI().toString());
		}
		if(image != null && image.getPixelReader() != null) {
			width = (int)image.getWidth();
			height = (int)image.getHeight();
			argb = new int[width * height];
			image.getPixelReader().getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), argb, 0, width);
		} else {
			// file reading failed: create an empty RasterImage
			this.width = 256;
			this.height = 256;
			argb = new int[width * height];
			Arrays.fill(argb, gray);
		}
	}
	 		   		   	  
	public RasterImage(ImageView imageView) {
		// creates a RasterImage from that what is shown in the given ImageView
		Image image = imageView.getImage();
		width = (int)image.getWidth();
		height = (int)image.getHeight();
		argb = new int[width * height];
		image.getPixelReader().getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), argb, 0, width);
	}
	
	public void setToView(ImageView imageView) {
		// sets the current argb pixels to be shown in the given ImageView
		if(argb != null) {
			WritableImage wr = new WritableImage(width, height);
			PixelWriter pw = wr.getPixelWriter();
			pw.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), argb, 0, width);
			imageView.setImage(wr);
		}
	}
		
	// image point operations to be added here
	
	public void convertToGray() {
		
		// TODO: convert the image to grayscale
		for(int i = 0; i < argb.length; i ++){
			int a = (argb[i] >> 24) & 0xff;
			int r = (argb[i] >> 16) & 0xff;
			int g = (argb[i] >> 8) & 0xff;
			int b = (argb[i] ) & 0xff;

			int newGray = (r + g + b) / 3;
			argb[i] = (a<<24) | (newGray<<16) | (newGray<<8) | newGray;
		}
	}
	
	public double getEntropy() {

		// TODO: calculate and return the entropy of the image
		// Mit Unterstützung von Duncan <3

		int[] histogram = new int[256];
		int totalPixels = this.argb.length;

		for(int i = 0; i < this.argb.length; ++i) {
			int pixel = this.argb[i];
			int gray = pixel & 255;
			int var10002 = histogram[gray]++;
		}

		double entropy = 0.0;

		for(int i = 0; i < histogram.length; ++i) {
			if (histogram[i] > 0) {
				double p = (double)histogram[i] / (double)totalPixels;
				entropy -= p * (Math.log(p) / Math.log(2.0));
			}
		}

		return entropy;
	}
 		   		   	  
} 		   		   	  




 		   		   	  


