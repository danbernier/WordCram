package wordcram;

import processing.core.PApplet;

public class WeightShadeColorer implements WordColorer {
	
	PApplet host;
	
	public WeightShadeColorer(PApplet host) {
		super();
		this.host = host;
	}

	public int colorFor(Word word) {
		return host.color(new Float((int) 255 - (255*word.weight)).intValue());
	}

}
