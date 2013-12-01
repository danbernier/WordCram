package wordcram;


public class DefaultSketchFilter implements WordPlaceFilter {

	public DefaultSketchFilter() {
	}

	public boolean canFit(Word word) {
		return true;
	}

}
