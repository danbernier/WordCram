package wordcram;




public interface Observer  {
	
	public void beginDraw();
	
	public void wordDrawn(Word word);
	
	public void wordSkipped(Word word);
	
	public void endDraw(Word[] words);

	public void wordsCounted(Word[] words);

	public void wordsScaled(Word[] words);

	public void wordsShaped();

}
