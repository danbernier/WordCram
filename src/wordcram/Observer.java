package wordcram;

public interface Observer {
	public void wordsCounted(Word[] words);
	public void beginDraw();
	public void wordDrawn(Word word);
	public void wordSkipped(Word word);
	public void endDraw();
}
