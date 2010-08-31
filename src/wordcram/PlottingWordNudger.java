package wordcram;

import processing.core.PApplet;
import processing.core.PVector;

public class PlottingWordNudger implements WordNudger {
	
	private PApplet parent;
	private WordNudger wrappedNudger;
	
	public PlottingWordNudger(PApplet _parent, WordNudger _wrappedNudger) {
		parent = _parent;
		wrappedNudger = _wrappedNudger;
	}

	@Override
	public PVector nudge(Word word, int attempt) {
		PVector v = wrappedNudger.nudge(word, attempt);
		parent.pushStyle();
		parent.noStroke();
		parent.fill(0, 255, 255, 100);
		PVector wordLoc = PVector.add(v, word.getLocation());
		parent.ellipse(wordLoc.x, wordLoc.y, 1, 1);
		parent.popStyle();
		return v;
	}

}
