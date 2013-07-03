package wordcram;




public interface Observer  {
	
	public void beginDraw();
	
	public void wordDrawn(Word word);
	
	public void endDraw();

}
