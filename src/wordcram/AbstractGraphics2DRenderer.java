package wordcram;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;

public abstract class AbstractGraphics2DRenderer extends AbstractRenderer {
	
	Graphics2D graphics;

	public void drawEngineWord(EngineWord eWord) {
		GeneralPath path2d = new GeneralPath(eWord.getShape());
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setPaint(new Color(eWord.getWord().getColor(colorer), true));
		graphics.fill(path2d);
	}

}
