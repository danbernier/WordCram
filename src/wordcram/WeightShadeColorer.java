package wordcram;

import processing.core.PApplet;

public class WeightShadeColorer implements WordColorer {
	
	PApplet host;
	int highest = 0;
	
	public WeightShadeColorer(PApplet host) {
		super();
		this.host = host;
	}

	public int colorFor(Word word) {
		int shade = new Float((int) highest - (highest*word.weight)).intValue();
		return host.color(shade);
	}

	public int getHighest() {
		return highest;
	}

	public void setHighest(int highest) {
		this.highest = highest;
	}

}
