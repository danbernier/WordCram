package wordcram;

public abstract class AbstractRenderer implements Renderer {
	
	WordColorer colorer;

	public void setColorer(WordColorer colorer) {
		this.colorer = colorer;
	}

	
}
