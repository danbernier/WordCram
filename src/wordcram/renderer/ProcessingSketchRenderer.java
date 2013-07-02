package wordcram.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PGraphicsJava2D;
import wordcram.EngineWord;
import wordcram.WordColorer;

public class ProcessingSketchRenderer implements Renderer {

	private PGraphics canvas;
	WordColorer colorer;

	public ProcessingSketchRenderer(PApplet parent) {
		canvas = parent.g;
	}

	public int getWidth() {
		return canvas.width;
	}

	public int getHeight() {
		return canvas.height;
	}

	public void setColorer(WordColorer colorer) {
		this.colorer = colorer;
	}

	public void drawEngineWord(EngineWord eWord) {
		GeneralPath path2d = new GeneralPath(eWord.getShape());
		Graphics2D g2 = ((PGraphicsJava2D) canvas).g2;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setPaint(new Color(eWord.getWord().getColor(colorer), true));
		g2.fill(path2d);
	}
	
	public void close() {
		
	}

}
