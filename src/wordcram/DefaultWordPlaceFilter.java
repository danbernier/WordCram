package wordcram;


public class DefaultWordPlaceFilter implements WordPlaceFilter {

	public DefaultWordPlaceFilter() {
	}

	public boolean canFit(Word word) {
		return true;
	}

}
