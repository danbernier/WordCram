package wordcram;


public interface Renderer {
	
	public int getWidth();
	public int getHeight();
	public void setColorer(WordColorer colorer);
	public void drawEngineWord(EngineWord eWord);
	public void close();

}
