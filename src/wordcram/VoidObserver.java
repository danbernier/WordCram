package wordcram;

class VoidObserver implements Observer {
	public void beginDraw() {}
	public void wordDrawn(Word word) {}
	public void wordSkipped(Word word) {}
	public void wordsCounted(Word[] words) {}
	public void wordsScaled(Word[] words) {}
	public void wordsShaped() {}
	public void endDraw(Word[] words) {}
}
