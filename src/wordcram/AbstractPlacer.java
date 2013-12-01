package wordcram;


public abstract class AbstractPlacer implements WordPlacer {

	WordPlaceFilter filter;
	
	public AbstractPlacer() {
		filter = new DefaultWordPlaceFilter();
	}

	public WordPlacer withFilter(WordPlaceFilter filter) {
		this.filter = filter;
		return this;
	}

}
