package wordcram;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PGraphicsJava2D;

public class ProcessingSketchRenderer extends AbstractGraphics2DRenderer {

	private PGraphics canvas;
	

	public ProcessingSketchRenderer(PApplet parent) {
		canvas = parent.g;
		graphics = ((PGraphicsJava2D) parent.g).g2;
	}

	public int getWidth() {
		return canvas.width;
	}

	public int getHeight() {
		return canvas.height;
	}

	public void close() {
	}

}
