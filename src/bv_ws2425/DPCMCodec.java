// BV Ue6 WS2024/25 Vorgabe
//
// Copyright (C) 2024 by Klaus Jung
// All rights reserved.
// Date: 2024-09-30
 		   		   	  

package bv_ws2425;


public class DPCMCodec {

	public enum PredictionType {
		A("A (horizontal)"),
		B("B (vertical)"),
		C("C (diagonal)"),
		ABC("A+B-C"),
		AB_MEAN("(A+B)/2"),
		ADAPTIVE("adaptive");

		private final String name;
	    private PredictionType(String s) { name = s; }
	    public String toString() { return this.name; }
	};


	public void processDPCM(RasterImage originalImage, RasterImage errorImage, RasterImage reconstructedImage, double quantizationDelta, PredictionType type) {

		// TODO: Encode the originalImage with DPCM using the given prediction type, 
		// visualize the prediction error in errorImage, and
		// decode the prediction error into reconstructedImage.

		// Hint: You can implement encoding and decoding with a single iteration over the pixels of the given image.

		// Optional: Implement DPCM with quantization. The quantization step size is given in quantizationDelta.

		int width = originalImage.width;
		int height = originalImage.height;
		int[] original = originalImage.argb;
		int[] error = new int[original.length];
		int[] reconstructed = new int[original.length];

		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				int index = y * width + x;

				// Initialize predictors to a default value
				int A = 128;
				int B = 128;
				int C = 128;

				// Values from neighboring reconstructed pixels
				if (x > 0) {
					A = reconstructed[index - 1] & 255;
				}
				if (y > 0) {
					B = reconstructed[index - width] & 255;
				}
				if (x > 0 && y > 0) {
					C = reconstructed[index - width - 1] & 255;
				}

				int predicted = 0;
				switch (type) {
					case A:
						predicted = A;
						break;
					case B:
						predicted = B;
						break;
					case C:
						predicted = C;
						break;
					case ABC:
						predicted = A + B - C;
						break;
					case AB_MEAN:
						predicted = (A + B) / 2;
						break;
					case ADAPTIVE:
						predicted = Math.abs(A - C) < Math.abs(B - C) ? B : A;
						break;
				}

				int originalValue = original[index] & 255;
				int errorValue = originalValue - predicted;
				int quantizedError = (int) Math.round(errorValue / quantizationDelta) * (int) quantizationDelta;
				int reconstructedValue = predicted + quantizedError;
				reconstructedValue = Math.max(0, Math.min(255, reconstructedValue));
				int visualizedError = Math.max(0, Math.min(255, errorValue + 128));

				// Store the values in the respective arrays
				error[index] = 0xFF000000 | (visualizedError << 16) | (visualizedError << 8) | visualizedError;
				reconstructed[index] = 0xFF000000 | (reconstructedValue << 16) | (reconstructedValue << 8) | reconstructedValue;
			}
		}
		// Set the pixel data for the errorImage and reconstructedImage
		errorImage.argb = error;
		reconstructedImage.argb = reconstructed;
	}
	
	public double getMSE(RasterImage originalImage, RasterImage reconstructedImage) {
		
		// TODO: calculate and return the Mean Square Error between the given images

		double mse = 0.0;
		int width = originalImage.width;
		int height = originalImage.height;

		int totalPixels = width * height;
		int[] original = originalImage.argb;
		int[] reconstructed = reconstructedImage.argb;

		for (int i = 0; i < totalPixels; ++i) {
			// Extract the grayscale value
			int originalGray = original[i] & 255;
			int reconstructedGray = reconstructed[i] & 255;

			mse += Math.pow(originalGray - reconstructedGray, 2.0);
		}

		// Return the Mean Squared Error
		return mse / totalPixels;
		
	}
	
 		   		   	  
}
 		   		   	  







