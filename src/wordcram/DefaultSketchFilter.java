package wordcram;


public class DefaultSketchFilter implements SketchFilter {

	public DefaultSketchFilter() {
	}

	@Override
	public boolean filter(Word word) {
		return true;
	}

}
