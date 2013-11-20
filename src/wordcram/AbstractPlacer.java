package wordcram;


public abstract class AbstractPlacer implements WordPlacer {

	SketchFilter filter;
	
	public AbstractPlacer() {
		filter = new DefaultSketchFilter();
	}

	public WordPlacer withFilter(SketchFilter filter) {
		this.filter = filter;
		return this;
	}

}
