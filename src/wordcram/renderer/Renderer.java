package wordcram.renderer;

import wordcram.EngineWord;
import wordcram.WordColorer;

public interface Renderer {
	
	public int getWidth();
	public int getHeight();
	public void setColorer(WordColorer colorer);
	public void drawEngineWord(EngineWord eWord);
	public void close();

}
