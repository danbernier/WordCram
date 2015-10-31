package wordcram;

import java.awt.Color;  // awt: for fill color
import java.awt.Graphics2D; // awt: draws the word to the screen
import java.awt.RenderingHints;  // awt: for constants while rendering a word
import java.awt.geom.GeneralPath;  // awt: Shape -> Path, fill(path)

import processing.core.PGraphics;
import processing.awt.PGraphicsJava2D;

class ProcessingWordRenderer implements WordRenderer {
	PGraphics destination;

	ProcessingWordRenderer(PGraphics destination) {
		this.destination = destination;
	}

	public int getWidth() {
		return destination.width;
	}

	public int getHeight() {
		return destination.height;
	}

	public void drawWord(EngineWord word, Color color) {
        GeneralPath path2d = new GeneralPath(word.getShape());

//        Graphics2D g2 = (Graphics2D)destination.image.getGraphics();
        Graphics2D g2 = ((PGraphicsJava2D)destination).g2;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(color);
        g2.fill(path2d);
	}

	public void finish() {}
}
